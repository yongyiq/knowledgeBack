package com.houyaozu.knowledge.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.houyaozu.knowledge.pojo.DTO.PageDTO;
import com.houyaozu.knowledge.pojo.VO.PageVO;
import com.houyaozu.knowledge.pojo.domain.Articles;
import com.houyaozu.knowledge.pojo.domain.Categories;
import com.houyaozu.knowledge.server.mapper.ArticlesMapper;
import com.houyaozu.knowledge.server.mapper.CategoriesMapper;
import com.houyaozu.knowledge.server.service.ArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author yongyiq
* @description 针对表【articles】的数据库操作Service实现
* @createDate 2025-05-07 19:43:08
*/
@Service
public class ArticlesServiceImpl extends ServiceImpl<ArticlesMapper, Articles>
    implements ArticlesService {
    @Autowired
    private CategoriesMapper categoriesMapper;
    @Override
    public PageVO getPages(PageDTO pageDTO) {
        Page<Articles> page = new Page<>(pageDTO.getPage(), pageDTO.getSize());
        String category = pageDTO.getCategory();
        LambdaQueryWrapper<Categories> categoriesQueryWrapper = new LambdaQueryWrapper<>();
        categoriesQueryWrapper.eq(Categories::getName, category);
        Categories categories = categoriesMapper.selectOne(categoriesQueryWrapper);
        LambdaQueryWrapper<Articles> queryWrapper = new LambdaQueryWrapper<>();
        if (categories != null){
            queryWrapper.eq(Articles::getCategoryId, categories.getId());
        }
        queryWrapper.like(pageDTO.getKeyword() != null, Articles::getTitle, pageDTO.getKeyword());
        IPage<Articles> iPage = page(page, queryWrapper);
        iPage.getRecords().forEach(article -> {
            article.setCategory(categoriesMapper.selectById(article.getCategoryId()).getName());
        });
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
}




