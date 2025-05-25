package com.houyaozu.knowledge.server.service.impl;

import com.houyaozu.knowledge.server.service.ChatHistoryRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
@Component
public class InMemoryChatHistoryRepository implements ChatHistoryRepository {

    private final Map<String, List<String>> chatHistory = new ConcurrentHashMap<>();

    @Override
    public void saveHistory(String type, String chatId) {
//        if (!chatHistory.containsKey(type)) {
//            chatHistory.put(type, new ArrayList<>());
//        }
//        List<String> chatIds = chatHistory.get(type);
        List<String> chatIds = chatHistory.computeIfAbsent(type, k -> new ArrayList<>());
        if (chatIds.contains(chatId)) {
            return;
        }
        chatIds.add(chatId);
    }

    @Override
    public List<String> getChatIds(String type) {
        return chatHistory.getOrDefault(type, List.of());
    }
}
