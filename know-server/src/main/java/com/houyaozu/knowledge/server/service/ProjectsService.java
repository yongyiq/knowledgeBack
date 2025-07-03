package com.houyaozu.knowledge.server.service;

import com.houyaozu.knowledge.pojo.DTO.PageDTO;
import com.houyaozu.knowledge.pojo.VO.PageVO;
import com.houyaozu.knowledge.pojo.domain.Projects;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author yongyiq
* @description 针对表【projects】的数据库操作Service
* @createDate 2025-05-07 19:15:45
*/
public interface ProjectsService extends IService<Projects> {

    PageVO getPages(PageDTO pageDTO);

    List<String> getTagsById(Integer id);

    List<Projects> getHotProjects(int i);
}
