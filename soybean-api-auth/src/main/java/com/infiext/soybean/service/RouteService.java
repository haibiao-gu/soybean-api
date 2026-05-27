package com.infiext.soybean.service;

import com.infiext.soybean.dto.RouteDTO;
import com.infiext.soybean.dto.UserRoleDTO;

import java.util.List;

public interface RouteService {
    List<RouteDTO> getConstantRoutes();

    UserRoleDTO getUserRoutes(String userId);
}
