package com.weng.commutercarbackend.aop;

import com.weng.commutercarbackend.common.Result;
import com.weng.commutercarbackend.common.ResultCodeEnum;
import com.weng.commutercarbackend.exception.BusinessException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler<T>
{
    /**
     * 业务异常
     * @param businessException
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public Result<T> businessExceptionHandler(BusinessException businessException)
    {
        log.error("BusinessException:",businessException);
        return Result.error(businessException.getResultCodeEnum(),businessException.getMessage());
    }

    /**
     * 参数校验失败的异常处理器
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)//extends BindException
    public Result<T> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException methodArgumentNotValidException) {
        String message = Objects.requireNonNull(methodArgumentNotValidException.getBindingResult().getFieldError()).getDefaultMessage();
        log.error("MethodArgumentNotValidException:",methodArgumentNotValidException);
        return Result.error(ResultCodeEnum.PARAMS_ERROR,message);
    }

    /**
     * 参数校验失败的异常处理器
     */
    @ExceptionHandler(ConstraintViolationException.class)//extends ValidationException
    public Result<T> ConstraintViolationExceptionHandler(ConstraintViolationException constraintViolationException)
    {
        Set<ConstraintViolation<?>> constraintViolations =
                constraintViolationException.getConstraintViolations();
        StringBuilder stringBuilder = new StringBuilder();
        for (ConstraintViolation<?> constraintViolation : constraintViolations)
        {
            stringBuilder.append(constraintViolation.getMessage()).append(",");
        }
        log.error("ConstraintViolationException:",constraintViolationException);
        return Result.error(ResultCodeEnum.PARAMS_ERROR, stringBuilder.toString());
    }

    /**
     *  请求参数格式错误
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public Result<T> badRequestHandler(Exception exception) {

        log.error("BadRequestException:",exception);
        return Result.error(ResultCodeEnum.PARAMS_ERROR);
    }

    /**
     *  用户没有权限
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<T> accessDeniedExceptionHandler(AccessDeniedException exception) {
        log.error("AccessDeniedException:",exception);
        return Result.error(ResultCodeEnum.NO_AUTH_ERROR);
    }
    /**
     * 兜底
     */
    @ExceptionHandler(Exception.class)
    public Result<T> exceptionHandler(Exception exception)
    {
        log.error("Exception:{}",exception.getClass());
        return Result.error(ResultCodeEnum.SYSTEM_ERROR);
    }

}
