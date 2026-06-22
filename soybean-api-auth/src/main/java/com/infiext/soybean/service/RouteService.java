package com.infiext.soybean.service;

import com.infiext.soybean.vo.RouteVO;
import com.infiext.soybean.vo.UserRoleVO;

import java.util.List;

public interface RouteService {
    List<RouteVO> getConstantRoutes();

    UserRoleVO getUserRoutes(String userId);
}
