package com.infiext.soybean.service.impl;

import com.infiext.soybean.enums.StatusEnum;
import com.infiext.soybean.enums.YesOrNoEnum;
import com.infiext.soybean.po.SysMenuPO;
import com.infiext.soybean.service.RouteService;
import com.infiext.soybean.service.SysMenuService;
import com.infiext.soybean.service.SysRoleService;
import com.infiext.soybean.service.SysUserService;
import com.infiext.soybean.utils.TreeUtil;
import com.infiext.soybean.vo.RouteVO;
import com.infiext.soybean.vo.UserRoleVO;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class RouteServiceImpl implements RouteService {
    @Resource
    private SysMenuService sysMenuService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysRoleService sysRoleService;

    /**
     * 获取常量路由
     *
     * @return 常量路由
     */
    @Override
    public List<RouteVO> getConstantRoutes() {
        SysMenuPO query = SysMenuPO.create().setConstant(YesOrNoEnum.Y).setStatus(StatusEnum.ENABLED);
        List<SysMenuPO> list = sysMenuService.getList(query, null);
        List<RouteVO> routes = convertToRoute(list);
        routes.add(getLoginPage());
        return routes;
    }

    /**
     * 获取用户路由
     *
     * @return 用户路由
     */
    @Override
    @Cacheable(value = "userRoutes", key = "#userId")
    public UserRoleVO getUserRoutes(String userId) {
        // 获取用户角色ID
        List<String> roleIds = sysUserService.getUserRoleIds(userId);
        // 获取角色菜单ID
        List<String> menuIds = sysRoleService.getRoleMenuIds(roleIds);
        // 获取菜单缓存数据
        List<SysMenuPO> menuCacheList = sysMenuService.getCachedList();
        // 转换成菜单Map（线程安全）
        ConcurrentHashMap<String, SysMenuPO> menuMap = new ConcurrentHashMap<>(
                menuCacheList.stream()
                        .collect(Collectors.toMap(SysMenuPO::getId, menu -> menu)));

        // 用户菜单
        List<SysMenuPO> userMenus = new ArrayList<>();

        for (String menuId : menuIds) {
            pushMenu(userMenus, menuId, menuMap);
        }

        List<RouteVO> routes = convertToRoute(userMenus);

        String home = findFistPage(routes);

        UserRoleVO res = new UserRoleVO();
        res.setHome(home);
        res.setRoutes(routes);
        return res;
    }

    /**
     * 添加菜单
     *
     * @param menuList 菜单列表
     * @param menuId   菜单ID
     */
    private void pushMenu(List<SysMenuPO> menuList, String menuId, ConcurrentHashMap<String, SysMenuPO> menuMap) {
        SysMenuPO menu = menuMap.get(menuId);
        if (menu == null || menu.getConstant().equals(YesOrNoEnum.Y)) return;
        menuList.add(menu);
        if (!menu.getParentId().equals("0")) {
            pushMenu(menuList, menu.getParentId(), menuMap);
        }
    }

    /**
     * 获取登录页面
     *
     * @return 登录页面
     */
    private RouteVO getLoginPage() {
        RouteVO route = new RouteVO();
        route.setName("login");
        route.setPath("/login/:module(pwd-login|code-login|register|reset-pwd|bind-wechat)?");
        route.setComponent("layout.blank$view.login");

        RouteVO.Meta meta = new RouteVO.Meta();
        meta.setTitle("login");
        meta.setI18nKey("登录");
        meta.setConstant(true);
        meta.setHideInMenu(true);
        route.setMeta(meta);
        return route;
    }

    /**
     * 获取第一个页面
     *
     * @param routes 路由列表
     * @return 第一个页面
     */
    private String findFistPage(List<RouteVO> routes) {
        for (RouteVO route : routes) {
            if (route.getChildren() != null && !route.getChildren().isEmpty()) {
                return findFistPage(route.getChildren());
            } else {
                return route.getName();
            }
        }
        return null;
    }

    private List<RouteVO> convertToRoute(List<SysMenuPO> menuList) {
        List<SysMenuPO> sortedMenuList = menuList.stream()
                .sorted((m1, m2) -> {
                    Integer order1 = m1.getSortOrder() != null ? m1.getSortOrder() : Integer.MAX_VALUE;
                    Integer order2 = m2.getSortOrder() != null ? m2.getSortOrder() : Integer.MAX_VALUE;
                    return order1.compareTo(order2);
                })
                .toList();

        List<RouteVO> routeList = sortedMenuList.stream()
                .map(menu -> {
                    RouteVO route = new RouteVO();
                    route.setId(menu.getId());
                    route.setParentId(menu.getParentId());
                    route.setName(menu.getRouteName());
                    route.setPath(menu.getRoutePath());
                    route.setComponent(menu.getComponent());
                    route.setMeta(convertToMeta(menu));
                    return route;
                })
                .collect(Collectors.toList());
        return TreeUtil.buildTreeByMap(routeList);
    }

    private RouteVO.Meta convertToMeta(SysMenuPO menu) {
        RouteVO.Meta meta = new RouteVO.Meta();
        meta.setTitle(menu.getMenuName());
        meta.setIcon(menu.getIcon());
        meta.setIconType(menu.getIconType());
        meta.setI18nKey(menu.getI18nKey());
        meta.setKeepAlive(menu.getKeepAlive().equals(YesOrNoEnum.Y));
        meta.setConstant(menu.getConstant().equals(YesOrNoEnum.Y));
        meta.setOrder(menu.getSortOrder());
        meta.setHref(menu.getHref());
        meta.setHideInMenu(menu.getHideInMenu().equals(YesOrNoEnum.Y));
        meta.setActiveMenu(menu.getActiveMenu());
        meta.setMultiTab(menu.getMultiTab().equals(YesOrNoEnum.Y));
        meta.setFixedIndexInTab(menu.getFixedIndexInTab());
        return meta;
    }
}
