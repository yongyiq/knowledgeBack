package com.houyaozu.knowledge.server.service;

import java.util.List;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
public interface ChatHistoryRepository {
    /**
     * 保存聊天记录
     * @param userId 用户id
     * @param chatId
     */
    void saveHistory(String userId, String chatId);
    /**
     * 获取聊天记录
     * @param type 业务类型
     * @return
     */
    List<String> getChatIds(String type);
}
