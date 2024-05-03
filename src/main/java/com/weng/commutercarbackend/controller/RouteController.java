package com.weng.commutercarbackend.controller;

import com.weng.commutercarbackend.common.Result;
import com.weng.commutercarbackend.model.entity.Route;
import com.weng.commutercarbackend.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/route")
@RequiredArgsConstructor
public class RouteController {
    private final RouteService routeService;

    @GetMapping("/{id}")
    public Result<Route>getRouteById(@PathVariable Long id){
        Route route = routeService.getById(id);
        return Result.success(route);
    }


}
