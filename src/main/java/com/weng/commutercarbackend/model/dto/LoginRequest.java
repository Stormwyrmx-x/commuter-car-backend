package com.weng.commutercarbackend.model.dto;

import com.weng.commutercarbackend.common.RoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


public record LoginRequest(
        @NotBlank(message = "用户名不能为空")
        @Pattern(regexp = "^\\d{10}$", message = "用户名格式不正确")//太优雅了！elegant!
        String username,

        @NotBlank(message = "密码不能为空")
        @Pattern(regexp = "^[a-zA-Z0-9_]{4,30}$", message = "密码格式不正确")
        String password,

        @NotNull(message = "角色不能为空")
//        @Pattern(regexp = "^[01]$", message = "角色格式不正确")
        RoleEnum role
) {
}
