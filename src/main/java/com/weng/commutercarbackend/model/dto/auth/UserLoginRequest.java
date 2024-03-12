package com.weng.commutercarbackend.model.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record UserLoginRequest(
        @NotBlank(message = "用户名不能为空")
        @Pattern(regexp = "^\\d{10}$", message = "用户名格式不正确")//太优雅了！elegant!
        String username,

        @NotBlank(message = "密码不能为空")
        @Pattern(regexp = "^[a-zA-Z0-9_]{4,30}$", message = "密码格式不正确")
        String password
)
{
}
