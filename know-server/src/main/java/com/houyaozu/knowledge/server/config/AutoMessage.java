package com.houyaozu.knowledge.server.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.content.Media;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoMessage {
    private String id;
    private String type; // user / system / assistant
    private String text;
    private Map<String, Object> metadata;
    private List<AssistantMessage.ToolCall> toolCalls; // 用于 assistant message
    private List<Media> media; // 如果你用到了 media 的话
    private long timestamp;


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
        String type = msg.getMessageType().name();
        String text = msg.getText();
        String id = DigestUtils.md5DigestAsHex((type + ":" + text).getBytes(StandardCharsets.UTF_8));
        return AutoMessage.builder()
                .id(id)
                .type(type)
                .text(text)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}

