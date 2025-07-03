package com.houyaozu.knowledge.server.controller;

import com.houyaozu.knowledge.common.login.LoginUser;
import com.houyaozu.knowledge.common.login.LoginUserHolder;
import com.houyaozu.knowledge.common.result.Result;
import com.houyaozu.knowledge.server.service.ChatMessageProducerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 聊天消息控制器
 * @ Author     ：侯耀祖
 * @ Description：提供聊天消息相关的API接口
 */
@RestController
@RequestMapping("/chat-message")
@Tag(name = "聊天消息管理", description = "聊天消息相关接口")
@Slf4j
public class ChatMessageController {

    @Autowired
    private ChatMessageProducerService chatMessageProducerService;

    /**
     * 手动触发批量持久化聊天记录
     */
    @PostMapping("/persist/{chatId}")
    @Operation(summary = "批量持久化聊天记录", description = "将Redis中的聊天记录批量发送到队列进行数据库持久化")
    public Result<String> persistChatMessages(@PathVariable String chatId) {
        try {
            LoginUser loginUser = LoginUserHolder.getLoginUser();
            if (loginUser == null) {
                return Result.fail();
            }

            String userId = loginUser.getUserId().toString();
            log.info("手动触发批量持久化: userId={}, chatId={}", userId, chatId);

            chatMessageProducerService.sendBatchChatMessages(userId, chatId);

            return Result.ok("批量持久化任务已提交");
        } catch (Exception e) {
            log.error("批量持久化失败: chatId={}, error={}", chatId, e.getMessage(), e);
            return Result.fail();
        }
    }

    /**
     * 获取队列状态信息（可选功能）
     */
    @GetMapping("/queue-status")
    @Operation(summary = "获取队列状态", description = "获取RabbitMQ队列的状态信息")
    public Result<String> getQueueStatus() {
        try {
            // 这里可以实现获取队列状态的逻辑
            // 比如队列中的消息数量、消费者数量等
            return Result.ok("队列状态正常");
        } catch (Exception e) {
            log.error("获取队列状态失败: {}", e.getMessage(), e);
            return Result.fail();
        }
    }
}
