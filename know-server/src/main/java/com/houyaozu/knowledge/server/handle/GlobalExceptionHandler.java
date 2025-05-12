package com.houyaozu.knowledge.server.handle;

import com.houyaozu.knowledge.common.exception.KnowledgeException;
import com.houyaozu.knowledge.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(KnowledgeException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.fail(ex.getCode(), ex.getMessage());
    }
}
