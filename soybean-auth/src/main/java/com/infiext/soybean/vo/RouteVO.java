package com.infiext.soybean.vo;

import lombok.Data;

import java.util.List;

@Data
public class RouteVO {
    private String id;
    private String parentId;
    private String name;
    private String path;
    private String component;
    private Meta meta;
    private List<RouteVO> children;

    @Data
    public static class Meta {
        private String title;
        private String icon;
        private String iconType;
        private String i18nKey;
        private Boolean keepAlive;
        private Boolean constant;
        private Integer order;
        private String href;
        private Boolean hideInMenu;
        private String activeMenu;
        private Boolean multiTab;
        private Integer fixedIndexInTab;
    }
}
