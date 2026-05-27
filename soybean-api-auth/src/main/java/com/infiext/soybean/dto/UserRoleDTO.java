package com.infiext.soybean.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserRoleDTO {
    private String home;

    private List<RouteDTO> routes;
}
