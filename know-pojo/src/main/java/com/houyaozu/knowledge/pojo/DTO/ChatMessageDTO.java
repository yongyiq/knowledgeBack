package com.houyaozu.knowledge.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 聊天消息传输对象
 * @ Author     ：侯耀祖
 * @ Description：用于RabbitMQ消息传输的DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 聊天会话ID
     */
    private String chatId;
    
    /**
     * 消息角色：user/assistant/system
     */
    private String role;
    
    /**
     * 消息内容
     */
    private String message;
    
    /**
     * 消息时间戳
     */
    private Long timestamp;
    
    /**
     * 重试次数（用于死信队列处理）
     */
    private Integer retryCount = 0;
}
