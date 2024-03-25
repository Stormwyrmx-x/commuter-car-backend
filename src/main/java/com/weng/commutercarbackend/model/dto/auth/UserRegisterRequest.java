package com.weng.commutercarbackend.model.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserRegisterRequest(
        @NotBlank(message = "用户名不能为空")
        @Pattern(regexp = "^\\d{10}$", message = "用户名格式不正确")
        String username,

        @NotBlank(message = "密码不能为空")
        @Pattern(regexp = "^[a-zA-Z0-9_]{4,30}$", message = "密码格式不正确")
        String password,

        @NotBlank(message = "确认密码不能为空")
        @Pattern(regexp = "^[a-zA-Z0-9_]{4,30}$", message = "确认密码格式不正确")
        String checkPassword,

        @NotBlank(message = "姓名不能为空")
        String name,

        @NotBlank(message = "电话号不能为空")
        @Pattern(regexp = "^1[3-9]\\d{9}$", message = "电话号格式不正确")
        String phone,

        @NotNull(message = "角色不能为空")
//        @Pattern(regexp = "",message = "角色格式不正确")
        @Pattern(regexp = "^[01]$", message = "角色格式不正确")
        String role

) {
}
