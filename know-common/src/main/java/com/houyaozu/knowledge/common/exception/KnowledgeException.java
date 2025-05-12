package com.houyaozu.knowledge.common.exception;

import com.houyaozu.knowledge.common.result.ResultCodeEnum;
import lombok.Data;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */

@Data
public class KnowledgeException extends RuntimeException{
    private Integer code;

    public KnowledgeException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    public KnowledgeException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }
}
