package com.houyaozu.knowledge.server.config;

import org.springframework.ai.chat.messages.*;

import java.lang.reflect.Constructor;
import java.util.Map;

public class MessageFactory {

    public static Message createMessage(MessageType type, String text, Map<String, Object> metadata) {
        try {
            Class<? extends Message> clazz;
            switch (type) {
                case USER -> clazz = UserMessage.class;
                case ASSISTANT -> clazz = AssistantMessage.class;
                case SYSTEM -> clazz = SystemMessage.class;
                default -> throw new IllegalArgumentException("Unsupported message type: " + type);
            }

            Constructor<? extends Message> constructor = clazz.getDeclaredConstructor(String.class, Map.class);
            constructor.setAccessible(true); // 👈 解锁 private 构造器
            return constructor.newInstance(text, metadata);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create Message via reflection", e);
        }
    }
}
