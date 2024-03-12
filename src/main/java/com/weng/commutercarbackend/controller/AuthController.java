package com.weng.commutercarbackend.controller;

import com.weng.commutercarbackend.common.Result;
import com.weng.commutercarbackend.model.dto.auth.UserLoginRequest;
import com.weng.commutercarbackend.model.dto.auth.UserRegisterRequest;
import com.weng.commutercarbackend.model.vo.UserLoginVO;
import com.weng.commutercarbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody @Validated UserLoginRequest userLoginRequest) {
        UserLoginVO userLoginVO = userService.login(userLoginRequest);
        return Result.success(userLoginVO);
    }

    @PostMapping("/register")
    public Result<Long> register(@RequestBody @Validated UserRegisterRequest userRegisterRequest)
    {
        Long id=userService.register(userRegisterRequest);
        return Result.success(id);
    }

}
