package com.weng.commutercarbackend.common;

import lombok.Data;

import java.io.Serializable;


@Data
public class Result<T> implements Serializable
{
    private Integer code;
    private T data;
    private String message;

    public Result(Integer code, T data, String message)
    {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static<T> Result<T> success()
    {
        return new Result<>(ResultCodeEnum.SUCCESS.getCode(), null,ResultCodeEnum.SUCCESS.getMessage());
    }

    public static<T> Result<T> success(T data)
    {
        return new Result<>(ResultCodeEnum.SUCCESS.getCode(),data,ResultCodeEnum.SUCCESS.getMessage());
    }

    public static<T> Result<T> error(ResultCodeEnum resultCodeEnum)
    {
        return new Result<>(resultCodeEnum.getCode(), null,resultCodeEnum.getMessage());
    }

    public static<T> Result<T> error(ResultCodeEnum resultCodeEnum,String message)
    {
        return new Result<>(resultCodeEnum.getCode(), null,message);
    }

}
