package com.weng.commutercarbackend.controller;

import com.weng.commutercarbackend.common.Result;
import com.weng.commutercarbackend.model.dto.LoginRequest;
import com.weng.commutercarbackend.model.dto.RegisterRequest;
import com.weng.commutercarbackend.model.vo.LoginVO;
import com.weng.commutercarbackend.service.DriverService;
import com.weng.commutercarbackend.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final DriverService driverService;
    private final PassengerService passengerService;

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody @Validated LoginRequest loginRequest) {
        // 根据角色进行不同的处理
        LoginVO loginVO = switch (loginRequest.role()) {
            case PASSENGER ->
                // 如果是乘客，执行乘客的登录逻辑
                    passengerService.login(loginRequest);
            case DRIVER ->
                // 如果是司机，执行司机的登录逻辑
                    driverService.login(loginRequest);
        };
        return Result.success(loginVO);
    }

    @PostMapping("/register")
    public Result<Long> register(@RequestBody @Validated RegisterRequest registerRequest)
    {
        // 根据角色进行不同的处理
        Long id = switch (registerRequest.role()) {
            case PASSENGER ->
                // 如果是乘客，执行乘客的登录逻辑
                    passengerService.register(registerRequest);
            case DRIVER ->
                // 如果是司机，执行司机的登录逻辑
                    driverService.register(registerRequest);
        };
        return Result.success(id);
    }

}
