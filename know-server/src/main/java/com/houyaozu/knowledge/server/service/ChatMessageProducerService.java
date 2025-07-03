package com.houyaozu.knowledge.server.service;

import com.houyaozu.knowledge.pojo.DTO.ChatMessageDTO;

/**
 * 聊天消息生产者服务接口
 * @ Author     ：侯耀祖
 * @ Description：负责发送聊天消息到RabbitMQ队列
 */
public interface ChatMessageProducerService {
    
    /**
     * 发送聊天消息到队列进行异步持久化
     * @param chatMessageDTO 聊天消息DTO
     */
    void sendChatMessage(ChatMessageDTO chatMessageDTO);
    
    /**
     * 批量发送聊天消息
     * @param userId 用户ID
     * @param chatId 聊天ID
     */
    void sendBatchChatMessages(String userId, String chatId);
}
