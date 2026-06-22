package com.infiext.soybean.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.infiext.soybean.domain.SortParam;
import com.infiext.soybean.enums.StatusEnum;
import com.infiext.soybean.exception.BusinessException;
import com.infiext.soybean.mapper.SysMenuMapper;
import com.infiext.soybean.po.SysMenuPO;
import com.infiext.soybean.service.SysMenuPermissionService;
import com.infiext.soybean.service.SysMenuQueryService;
import com.infiext.soybean.service.SysMenuService;
import com.infiext.soybean.utils.SortResetService;
import com.infiext.soybean.utils.SortUtil;
import com.infiext.soybean.validator.sys.menu.SysMenuValidationContext;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.infiext.soybean.constant.SystemConstant.MENU_LIST_CACHE;
import static com.infiext.soybean.constant.SystemConstant.MENU_VERSION_CACHE;
import static com.infiext.soybean.po.table.SysMenuTableDef.SYS_MENU;
import static com.mybatisflex.core.query.QueryMethods.distinct;

@Service
public class SysMenuServiceImpl implements SysMenuService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private SortResetService sortResetService;

    @Resource
    private SysMenuMapper mapper;
    @Resource
    private SysMenuValidationContext validator;

    @Resource
    private SysMenuQueryService sysMenuQueryService;
    @Resource
    private SysMenuPermissionService sysMenuPermissionService;

    /**
     * 创建
     */
    @Transactional
    @Override
    public SysMenuPO create(SysMenuPO po) {
        validator.validateAll(po);
        if (po.getSortOrder() == null) {
            long count = SysMenuPO.create().where(SYS_MENU.PARENT_ID.eq(po.getParentId())).count();
            po.setSortOrder((int) (count + 1));
        }
        po.save();
        sysMenuQueryService.resetMenuQuery(po.getId(), po.getQuery());
        sysMenuPermissionService.resetMenuPermissions(po.getId(), po.getPermissions());
        updateVersion();
        return po;
    }

    /**
     * 更新
     */
    @Transactional
    @Override
    public SysMenuPO update(SysMenuPO po) {
        validator.validateAll(po);
        boolean status = po.updateById();
        if (!status) {
            throw new BusinessException("修改失败，数据已被他人更新！");
        }
        sysMenuQueryService.resetMenuQuery(po.getId(), po.getQuery());
        sysMenuPermissionService.resetMenuPermissions(po.getId(), po.getPermissions());
        updateVersion();
        return po;
    }

    /**
     * 逻辑删除
     */
    @Transactional
    @Override
    public void deleteByIds(List<String> ids) {
        List<String> parentIds = new ArrayList<>();
        if (!ids.isEmpty()) {
            parentIds = SysMenuPO.create().select(distinct(SYS_MENU.PARENT_ID)).where(SYS_MENU.ID.in(ids)).listAs(String.class);
        }
        mapper.deleteBatchByIds(ids);
        for (String id : ids) {
            sysMenuQueryService.resetMenuQuery(id, new ArrayList<>());
            sysMenuPermissionService.resetMenuPermissions(id, new ArrayList<>());
        }
        for (String parentId : parentIds) {
            List<String> list = SysMenuPO.create()
                    .select(SYS_MENU.ID)
                    .where(SYS_MENU.PARENT_ID.eq(parentId))
                    .orderBy(SYS_MENU.SORT_ORDER.asc())
                    .listAs(String.class);
            doResetSortOrder(list);
        }
    }

    /**
     * 获取
     */
    @Override
    public SysMenuPO getById(String id) {
        return SysMenuPO.create().setId(id).withRelations().oneById();
    }

    /**
     * 获取分页
     */
    @Override
    public Page<SysMenuPO> getPage(SysMenuPO query, Page<SysMenuPO> page, SortParam sort) {
        QueryWrapper queryWrapper = getQueryWrapper(query, sort);
        return mapper.paginateWithRelations(page, queryWrapper);
    }

    /**
     * 获取列表
     */
    @Override
    public List<SysMenuPO> getList(SysMenuPO query, SortParam sort) {
        QueryWrapper queryWrapper = getQueryWrapper(query, sort);
        return mapper.selectListWithRelationsByQuery(queryWrapper);
    }

    /**
     * 获取查询条件
     */
    private QueryWrapper getQueryWrapper(SysMenuPO query, SortParam sort) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select(SYS_MENU.DEFAULT_COLUMNS);

        queryWrapper.and(SYS_MENU.CONSTANT.eq(query.getConstant()));
        queryWrapper.and(SYS_MENU.STATUS.eq(query.getStatus()));

        return SortUtil.orderBy(queryWrapper, sort, SysMenuPO.class, SYS_MENU.SORT_ORDER.asc());
    }

    /**
     * 重置排序
     */
    @Transactional
    @Override
    public void resetSortOrder(List<String> ids) {
        doResetSortOrder(ids);
    }

    /**
     * 执行重置排序的核心逻辑
     *
     * @param ids ID列表，按期望的排序顺序排列
     */
    private void doResetSortOrder(List<String> ids) {
        List<String> parentIds = new ArrayList<>();
        if (!ids.isEmpty()) {
            parentIds = SysMenuPO.create().select(distinct(SYS_MENU.PARENT_ID)).where(SYS_MENU.ID.in(ids)).listAs(String.class);
        }
        parentIds.forEach(parentId -> {
            sortResetService.resetSortOrder(
                    ids,
                    SysMenuPO::create,
                    idList -> SysMenuPO.create()
                            .select(SYS_MENU.ID, SYS_MENU.VERSION, SYS_MENU.SORT_ORDER)
                            .where(SYS_MENU.PARENT_ID.eq(parentId))
                            .orderBy(SYS_MENU.SORT_ORDER.asc())
                            .list(),
                    SysMenuPO::getId,
                    (entity, sortOrder) -> {
                        if (!Objects.equals(entity.getSortOrder(), sortOrder)) {
                            entity.setSortOrder(sortOrder);
                            entity.updateById();
                        }
                    },
                    this::updateVersion
            );
        });
    }

    /**
     * 更新版本
     */
    private List<SysMenuPO> updateVersion() {
        List<SysMenuPO> list = SysMenuPO.create().select(SYS_MENU.DEFAULT_COLUMNS)
                .where(SYS_MENU.STATUS.eq(StatusEnum.ENABLED))
                .orderBy(SYS_MENU.SORT_ORDER.asc())
                .list();
        stringRedisTemplate.opsForValue().set(MENU_LIST_CACHE, JSONUtil.toJsonStr(list));
        String randomVersion = RandomUtil.randomString(32);
        stringRedisTemplate.opsForValue().set(MENU_VERSION_CACHE, randomVersion);
        return list;
    }

    /**
     * 获取版本
     */
    @Override
    public String getVersion() {
        String version = stringRedisTemplate.opsForValue().get(MENU_VERSION_CACHE);
        return version != null ? version : "";
    }

    /**
     * 获取缓存列表
     */
    @Override
    public List<SysMenuPO> getCachedList() {
        String cachedJson = stringRedisTemplate.opsForValue().get(MENU_LIST_CACHE);
        if (StrUtil.isBlankIfStr(cachedJson)) {
            return updateVersion();
        }
        return JSONUtil.toList(cachedJson, SysMenuPO.class);
    }

    /**
     * 获取树形结构
     *
     * @return 树形结构
     */
    @Override
    public List<SysMenuPO> tree() {
        List<SysMenuPO> list = SysMenuPO.create()
                .select(SYS_MENU.DEFAULT_COLUMNS)
                .orderBy(SYS_MENU.SORT_ORDER.asc())
                .list();

        return buildTree(list, "0");
    }

    /**
     * 获取所有页面
     *
     * @return 所有页面
     */
    @Override
    public List<String> allPages() {
        return SysMenuPO.create()
                .select(SYS_MENU.ROUTE_NAME)
                .where(SYS_MENU.MENU_TYPE.eq("2"))
                .orderBy(SYS_MENU.SORT_ORDER.asc())
                .listAs(String.class);
    }

    /**
     * 构建树形结构
     *
     * @param allNodes 所有节点列表
     * @param parentId 父节点ID
     * @return 树形结构列表
     */
    private List<SysMenuPO> buildTree(List<SysMenuPO> allNodes, String parentId) {
        return allNodes.stream()
                .filter(node -> Objects.equals(node.getParentId(), parentId))
                .peek(node -> node.setChildren(buildTree(allNodes, node.getId())))
                .toList();
    }
}