package com.infiext.soybean.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserRoleVO {
    private String home;

    private List<RouteVO> routes;
}
