package com.houyaozu.knowledge.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.houyaozu.knowledge.pojo.domain.Articles;
import com.houyaozu.knowledge.pojo.domain.Categories;
import com.houyaozu.knowledge.server.mapper.ArticlesMapper;
import com.houyaozu.knowledge.server.mapper.CategoriesMapper;
import com.houyaozu.knowledge.server.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author yongyiq
* @description 针对表【categories】的数据库操作Service实现
* @createDate 2025-05-07 19:43:08
*/
@Service
public class CategoriesServiceImpl extends ServiceImpl<CategoriesMapper, Categories>
    implements CategoriesService {

    @Autowired
    private ArticlesMapper articlesMapper;
    @Override
    public List<Categories> getCategorieList() {
        List<Categories> categories = list();
        categories.forEach(category -> {
            category.setCount(articlesMapper.selectCount(new LambdaQueryWrapper<Articles>()
                    .eq(Articles::getCategoryId, category.getId())));
        });
        return categories;
    }
}




