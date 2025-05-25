package com.houyaozu.knowledge.server.service;

import java.util.List;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
public interface ChatHistoryRepository {
    /**
     * 保存聊天记录
     * @param type 业务类型
     * @param chatId
     */
    void saveHistory(String type, String chatId);
    /**
     * 获取聊天记录
     * @param type 业务类型
     * @return
     */
    List<String> getChatIds(String type);
}
