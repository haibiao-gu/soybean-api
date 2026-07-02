package com.infiext.soybean.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserRoleVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String home;

    private List<RouteVO> routes;
}
