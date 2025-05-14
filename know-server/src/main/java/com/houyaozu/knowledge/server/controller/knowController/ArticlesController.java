package com.houyaozu.knowledge.server.controller.knowController;

import com.houyaozu.knowledge.common.result.Result;
import com.houyaozu.knowledge.pojo.DTO.PageDTO;
import com.houyaozu.knowledge.pojo.VO.ArticlesVO;
import com.houyaozu.knowledge.pojo.VO.PageVO;
import com.houyaozu.knowledge.pojo.domain.Articles;
import com.houyaozu.knowledge.server.service.ArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/{id}")
    public Result getArticle(@PathVariable Integer id) {
        Articles articles = articlesService.getByArticaleId(id);
        return Result.ok(articles);
    }
    @GetMapping("/hot")
    public Result getHotArticles(Integer limit) {
        List<Articles> articlesVO = articlesService.getHotArticles(limit);
        return Result.ok(articlesVO);
    }
    @GetMapping("/{id}/related")
    public Result relatedArticles(@PathVariable Integer id) {
        //TODO 查找相似文章接口实现
        List<Articles> articlesVO = articlesService.getHotArticles(3);
        return Result.ok(articlesVO);
    }
    @PostMapping("/{id}/favorite")
    public Result articleFavorite(@PathVariable Integer id) {
        articlesService.favorite(id);
        return Result.ok();
    }
    @DeleteMapping("/{id}/favorite")
    public Result articleUnfavorite(@PathVariable Integer id) {
        articlesService.unfavorite(id);
        return Result.ok();
    }
}
