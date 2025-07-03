package com.houyaozu.knowledge.server.controller.projectController;

import com.houyaozu.knowledge.common.result.Result;
import com.houyaozu.knowledge.pojo.DTO.PageDTO;
import com.houyaozu.knowledge.pojo.VO.PageVO;
import com.houyaozu.knowledge.pojo.domain.Articles;
import com.houyaozu.knowledge.pojo.domain.Projects;
import com.houyaozu.knowledge.server.service.ProjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectsService projectsService;
    @GetMapping("/list")
    public Result getProjectPages(PageDTO pageDTO) {
        PageVO pageVO = projectsService.getPages(pageDTO);
        return Result.ok(pageVO);
    }
    @GetMapping("/{id}")
    public Result getProjectDetail(@PathVariable Integer id) {
        Projects projects = projectsService.getById(id);
        return Result.ok(projects);
    }
    @GetMapping("/{id}/tags")
    public Result getTags(@PathVariable Integer id) {
        List<String> tags = projectsService.getTagsById(id);
        return Result.ok(tags);
    }
    @GetMapping("/{id}/related")
    public Result relatedProjects(@PathVariable Integer id) {
        //TODO 查找相似文章接口实现
        List<Projects> projectsList = projectsService.getHotProjects(3);
        return Result.ok(projectsList);
    }
    @GetMapping("/hot")
    public Result getHotProjects(Integer limit) {
        List<Projects> projectsList = projectsService.getHotProjects(limit);
        return Result.ok(projectsList);
    }
}
