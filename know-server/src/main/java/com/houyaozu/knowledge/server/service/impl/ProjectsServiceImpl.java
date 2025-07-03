package com.houyaozu.knowledge.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.houyaozu.knowledge.pojo.DTO.PageDTO;
import com.houyaozu.knowledge.pojo.VO.PageVO;
import com.houyaozu.knowledge.pojo.domain.Articles;
import com.houyaozu.knowledge.pojo.domain.ProjectTags;
import com.houyaozu.knowledge.pojo.domain.Projects;
import com.houyaozu.knowledge.server.mapper.ProjectTagsMapper;
import com.houyaozu.knowledge.server.mapper.ProjectsMapper;
import com.houyaozu.knowledge.server.mapper.TagsMapper;
import com.houyaozu.knowledge.server.service.ProjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author yongyiq
* @description 针对表【projects】的数据库操作Service实现
* @createDate 2025-05-07 19:43:09
*/
@Service
public class ProjectsServiceImpl extends ServiceImpl<ProjectsMapper, Projects>
    implements ProjectsService {

    @Autowired
    private ProjectTagsMapper projectTagsMapper;
    @Autowired
    private TagsMapper tagsMapper;

    @Override
    public PageVO getPages(PageDTO pageDTO) {
        Page<Projects> page = new Page<>(pageDTO.getPage(), pageDTO.getSize());
        LambdaQueryWrapper<Projects> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!pageDTO.getKeyword().isEmpty(), Projects::getTitle, pageDTO.getKeyword());
        queryWrapper.eq(!pageDTO.getStatus().isEmpty(), Projects::getStatus, pageDTO.getStatus());
        IPage<Projects> iPage = page(page, queryWrapper);
        int totalPages = (int) Math.ceil((double) iPage.getTotal() / iPage.getSize());
        return PageVO.builder()
                .totalPages(totalPages)
                .totalElements(iPage.getTotal())
                .size(iPage.getSize())
                .number(iPage.getCurrent())
                .content(iPage.getRecords())
                .first(iPage.getCurrent() == 1)
                .last(iPage.getCurrent() >= totalPages)
                .empty(iPage.getRecords().isEmpty())
                .build();
    }

    @Override
    public List<String> getTagsById(Integer id) {
        LambdaQueryWrapper<ProjectTags> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectTags::getProjectId, id);
        List<ProjectTags> projectTags = projectTagsMapper.selectList(queryWrapper);
        return projectTags.stream().map(projectTags1 -> tagsMapper.selectById(projectTags1.getProjectId()).getName()).toList();
    }

    @Override
    public List<Projects> getHotProjects(int i) {
        LambdaQueryWrapper<Projects> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Projects::getViewCount);
        queryWrapper.last("limit " + i);
        return list(queryWrapper);
    }
}




