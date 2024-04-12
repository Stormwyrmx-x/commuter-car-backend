package com.weng.commutercarbackend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordChangeRequest(
        @NotBlank(message = "旧密码不能为空")
        @Pattern(regexp = "^[a-zA-Z0-9_]{4,30}$", message = "旧密码格式不正确")
        String oldPassword,

        @NotBlank(message = "新密码不能为空")
        @Pattern(regexp = "^[a-zA-Z0-9_]{4,30}$", message = "新密码格式不正确")
        String newPassword
) {
}
