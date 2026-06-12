package com.neusoft.environment.config;

import com.neusoft.environment.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResultVO<?> handleBindException(BindException e) {
        String msg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResultVO.error(400, msg);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResultVO<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResultVO.error(400, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResultVO<?> handleAccessDeniedException(AccessDeniedException e) {
        return ResultVO.error(403, "无权限访问");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResultVO<?> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常: ", e);
        return ResultVO.error(500, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResultVO<?> handleException(Exception e) {
        log.error("系统异常: ", e);
        return ResultVO.error(500, "系统内部错误");
    }
}
