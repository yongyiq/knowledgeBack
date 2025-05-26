package com.houyaozu.knowledge.server.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.content.Media;

import java.util.List;
import java.util.Map;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */

@Data
public class AutoMessage {
    private String type; // user / system / assistant
    private String text;
    private Map<String, Object> metadata;
    private List<AssistantMessage.ToolCall> toolCalls; // 用于 assistant message
    private List<Media> media; // 如果你用到了 media 的话


    // getters / setters

    public Message toSpringMessage() {
        return switch (type.toLowerCase()) {
            case "user" -> UserMessage.builder()
                .text(text)
                .metadata(metadata == null ? Map.of() : metadata)
                .build();

            case "assistant" -> new AssistantMessage(
                text,
                metadata == null ? Map.of() : metadata,
                toolCalls == null ? List.of() : toolCalls,
                List.of() // media 可选填
            );

            case "system" -> SystemMessage.builder()
                .text(text)
                .metadata(metadata == null ? Map.of() : metadata)
                .build();

            default -> throw new IllegalArgumentException("Unknown message type: " + type);
        };
    }

    public static AutoMessage fromSpringMessage(Message msg) {
        AutoMessage auto = new AutoMessage();
        auto.setText(msg.getText());
        auto.setMetadata(msg.getMetadata());
        auto.setType(msg.getMessageType().name().toLowerCase());
        return auto;
    }
}

