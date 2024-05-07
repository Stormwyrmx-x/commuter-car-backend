package com.weng.commutercarbackend.exception;

import com.weng.commutercarbackend.common.ResultCodeEnum;
import lombok.Data;

@Data
public class BusinessException extends RuntimeException {
    private final ResultCodeEnum resultCodeEnum;

    /**
     * 如果只传递了错误码，那么就使用错误码对应的默认错误信息
     *
     * @param resultCodeEnum 错误码
     */
    public BusinessException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.resultCodeEnum = resultCodeEnum;
    }

    /**
     * 如果传递了错误码和错误信息，那么就使用传递的错误信息
     *
     * @param resultCodeEnum 错误码
     * @param message        错误信息
     */
    public BusinessException(ResultCodeEnum resultCodeEnum, String message) {
        super(message);
        this.resultCodeEnum = resultCodeEnum;
    }
}
