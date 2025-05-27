package com.houyaozu.knowledge.server.service.impl;

import com.houyaozu.knowledge.server.config.RedisCache;
import com.houyaozu.knowledge.server.service.ChatHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
@Component
public class InMemoryChatHistoryRepository implements ChatHistoryRepository {
    @Autowired
    private RedisCache redisCache;

    @Override
    public void saveHistory(String userId, String chatId) {
        List<String> chatIds = redisCache.getCacheObject(userId);
        if (chatIds == null) {
            chatIds = new ArrayList<>();
        }
        if (chatIds.contains(chatId)) {
            return;
        }
        chatIds.add(chatId);
        redisCache.setCacheObject(userId, chatIds);
    }

    @Override
    public List<String> getChatIds(String userId) {
        List<String> chatIds = redisCache.getCacheObject(userId);
        return chatIds == null ? List.of() : chatIds;
    }

    @Override
    public void deleteHistory(String userId, String chatId) {
        List<String> chatIds = redisCache.getCacheObject(userId);
        chatIds.remove(chatId);
        redisCache.setCacheObject(userId, chatIds);
        redisCache.deleteObject(userId + ":" + chatId);
    }

    @Override
    public void deleteAllHistory(String userId) {
        List<String> chatIds = redisCache.getCacheObject(userId);
        for (String chatId : chatIds) {
            redisCache.deleteObject(userId + ":" + chatId);
        }
        redisCache.deleteObject(userId);
    }
}
