package com.houyaozu.knowledge.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.houyaozu.knowledge.pojo.domain.Projects;
import com.houyaozu.knowledge.server.mapper.ProjectsMapper;
import com.houyaozu.knowledge.server.service.ProjectsService;
import org.springframework.stereotype.Service;

/**
* @author yongyiq
* @description 针对表【projects】的数据库操作Service实现
* @createDate 2025-05-07 19:43:09
*/
@Service
public class ProjectsServiceImpl extends ServiceImpl<ProjectsMapper, Projects>
    implements ProjectsService {

}




