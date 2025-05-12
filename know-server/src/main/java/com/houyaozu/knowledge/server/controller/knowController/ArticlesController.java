package com.houyaozu.knowledge.server.controller.knowController;

import com.houyaozu.knowledge.common.result.Result;
import com.houyaozu.knowledge.pojo.DTO.PageDTO;
import com.houyaozu.knowledge.pojo.VO.PageVO;
import com.houyaozu.knowledge.pojo.domain.Articles;
import com.houyaozu.knowledge.server.service.ArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
@RestController
@RequestMapping("/knowledge/articles")
public class ArticlesController {

    @Autowired
    private ArticlesService articlesService;
    @GetMapping
    public Result getArticlesPages(PageDTO pageDTO) {
        PageVO pageVO = articlesService.getPages(pageDTO);
        return Result.ok(pageVO);
    }
}
