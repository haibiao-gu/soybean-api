package com.infiext.soybean.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RouteVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private String id;
    @JsonIgnore
    private String parentId;
    private String name;
    private String path;
    private String component;
    private Meta meta;
    private List<RouteVO> children;

    @Data
    public static class Meta implements Serializable {
        private static final long serialVersionUID = 1L;
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
