# AI对话异步数据库持久化方案

## 概述

本方案实现了使用RabbitMQ死信交换机进行AI对话异步数据库持久化的功能。当用户与AI进行对话时，对话内容首先保存到Redis中用于快速访问，同时异步发送到RabbitMQ队列进行数据库持久化。

## 架构设计

```
用户对话 -> Redis缓存 -> RabbitMQ队列 -> 数据库持久化
                    ↓
                死信队列（重试机制）
```

## 核心组件

### 1. 数据模型

- **ChatMessage**: 聊天消息实体类
- **ChatMessageDTO**: 消息传输对象，用于RabbitMQ消息传输

### 2. RabbitMQ配置

- **主交换机**: `chat.message.exchange`
- **主队列**: `chat.message.queue`
- **死信交换机**: `chat.message.dlx.exchange`
- **死信队列**: `chat.message.dlx.queue`

### 3. 服务组件

- **RedisChatMemoryRepository**: Redis聊天记忆仓库，集成消息发送功能
- **ChatMessageProducerService**: 消息生产者服务
- **ChatMessageConsumer**: 消息消费者，处理数据库持久化

## 工作流程

### 1. 对话保存流程

1. 用户发送消息给AI
2. AI响应消息
3. `RedisChatMemoryRepository.saveAll()` 被调用
4. 新消息保存到Redis
5. 异步发送消息到RabbitMQ主队列
6. 消费者接收消息并保存到数据库

### 2. 失败重试机制

1. 如果数据库保存失败，消息被拒绝
2. 消息进入死信队列
3. 死信队列消费者处理消息
4. 如果重试次数未超过限制，重新发送到主队列
5. 如果超过重试限制，记录错误日志

## 配置说明

### RabbitMQ配置

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    virtual-host: /yongyiq
    username: yongyiq
    password: 134513
    publisher-confirm-type: correlated
    publisher-returns: true
    listener:
      simple:
        acknowledge-mode: manual
        concurrency: 1
        max-concurrency: 3
        prefetch: 1
```

### 队列参数

- **TTL时间**: 60秒（死信队列中消息的存活时间）
- **最大重试次数**: 3次
- **消费者并发数**: 1-3个

## API接口

### 手动触发批量持久化

```http
POST /api/chat-message/persist/{chatId}
```

将Redis中指定chatId的所有聊天记录批量发送到队列进行持久化。

### 获取队列状态

```http
GET /api/chat-message/queue-status
```

获取RabbitMQ队列的状态信息。

## 数据库表结构

```sql
CREATE TABLE `chat_message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) NOT NULL,
  `chat_id` varchar(100) NOT NULL,
  `role` varchar(20) NOT NULL,
  `message` text NOT NULL,
  `timestamp` bigint NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `del_flag` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_user_chat` (`user_id`, `chat_id`),
  KEY `idx_timestamp` (`timestamp`)
);
```

## 监控和日志

### 日志级别

```yaml
logging:
  level:
    com.houyaozu.knowledge: debug
```

### 关键日志

- 消息发送日志
- 消息消费日志
- 数据库保存日志
- 重试机制日志
- 错误处理日志

## 优势特点

1. **异步处理**: 不影响用户对话体验
2. **可靠性**: 使用死信队列确保消息不丢失
3. **重试机制**: 自动重试失败的消息
4. **监控友好**: 完整的日志记录
5. **扩展性**: 支持水平扩展消费者

## 使用说明

1. 确保RabbitMQ服务正常运行
2. 执行数据库表创建脚本
3. 启动应用程序
4. 队列和交换机会自动创建
5. 开始AI对话，消息会自动持久化

## 故障排查

### 常见问题

1. **消息发送失败**: 检查RabbitMQ连接配置
2. **数据库保存失败**: 检查数据库连接和表结构
3. **消息堆积**: 检查消费者性能和数据库性能
4. **重试次数过多**: 检查业务逻辑和数据完整性

### 监控指标

- 队列消息数量
- 消费者处理速度
- 数据库保存成功率
- 重试次数统计
