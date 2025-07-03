-- 聊天消息表结构
CREATE TABLE IF NOT EXISTS `chat_message` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` varchar(50) NOT NULL COMMENT '用户ID',
  `chat_id` varchar(100) NOT NULL COMMENT '聊天会话ID',
  `role` varchar(20) NOT NULL COMMENT '消息角色：user/assistant/system',
  `message` text NOT NULL COMMENT '消息内容',
  `timestamp` bigint NOT NULL COMMENT '消息时间戳',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint DEFAULT '0' COMMENT '删除标志：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天消息表';
