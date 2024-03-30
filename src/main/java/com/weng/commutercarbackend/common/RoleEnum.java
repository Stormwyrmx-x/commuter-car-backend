package com.weng.commutercarbackend.common;

import lombok.Getter;

@Getter
public enum RoleEnum {
    PASSENGER(0),
    DRIVER(1);

    private final Integer value;

    RoleEnum(Integer value) {
        this.value = value;
    }
}
