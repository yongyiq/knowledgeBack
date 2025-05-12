package com.houyaozu.knowledge.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.houyaozu.knowledge.pojo.domain.Comments;
import com.houyaozu.knowledge.server.mapper.CommentsMapper;
import com.houyaozu.knowledge.server.service.CommentsService;
import org.springframework.stereotype.Service;

/**
* @author yongyiq
* @description 针对表【comments】的数据库操作Service实现
* @createDate 2025-05-07 19:43:08
*/
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments>
    implements CommentsService {

}




