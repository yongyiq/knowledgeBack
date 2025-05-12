package com.houyaozu.knowledge.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.houyaozu.knowledge.pojo.domain.Tags;
import com.houyaozu.knowledge.server.mapper.TagsMapper;
import com.houyaozu.knowledge.server.service.TagsService;
import org.springframework.stereotype.Service;

/**
* @author yongyiq
* @description 针对表【tags】的数据库操作Service实现
* @createDate 2025-05-07 19:43:09
*/
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags>
    implements TagsService {

}




