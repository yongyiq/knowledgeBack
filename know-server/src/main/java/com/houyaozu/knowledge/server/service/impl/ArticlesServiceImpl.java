package com.houyaozu.knowledge.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.houyaozu.knowledge.common.utils.BeanCopyUtils;
import com.houyaozu.knowledge.pojo.DTO.PageDTO;
import com.houyaozu.knowledge.pojo.VO.ArticlesVO;
import com.houyaozu.knowledge.pojo.VO.PageVO;
import com.houyaozu.knowledge.pojo.domain.Articles;
import com.houyaozu.knowledge.pojo.domain.Categories;
import com.houyaozu.knowledge.server.mapper.ArticlesMapper;
import com.houyaozu.knowledge.server.mapper.CategoriesMapper;
import com.houyaozu.knowledge.server.service.ArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 分页获取文章数据，并支持根据分类名称和关键词进行过滤
     *
     * @param pageDTO 包含分页参数及筛选条件（如分类名、关键词）
     * @return PageVO 返回封装后的分页结果，包含总页数、当前页数据等信息
     */
    @Override
    public PageVO getPages(PageDTO pageDTO) {
        // 创建分页对象，基于传入的页码和每页大小
        Page<Articles> page = new Page<>(pageDTO.getPage(), pageDTO.getSize());
        String category = pageDTO.getCategory();
        // 构建查询条件：根据分类名称查找对应的分类ID
        LambdaQueryWrapper<Categories> categoriesQueryWrapper = new LambdaQueryWrapper<>();
        categoriesQueryWrapper.eq(Categories::getName, category);
        Categories categories = categoriesMapper.selectOne(categoriesQueryWrapper);
        LambdaQueryWrapper<Articles> queryWrapper = new LambdaQueryWrapper<>();
        // 如果存在匹配的分类，则在文章查询中添加分类ID作为过滤条件
        if (categories != null){
            queryWrapper.eq(Articles::getCategoryId, categories.getId());
        }
        // 添加标题模糊匹配查询条件，如果关键词不为空
        queryWrapper.like(pageDTO.getKeyword() != null, Articles::getTitle, pageDTO.getKeyword());
        queryWrapper.eq(Articles::getStatus, "published");
        IPage<Articles> iPage = page(page, queryWrapper);
        // 为每篇文章设置其所属分类的名称，用于返回给前端展示
        iPage.getRecords().forEach(article -> {
            article.setCategory(categoriesMapper.selectById(article.getCategoryId()).getName());
        });
        // 计算总页数
        int totalPages = (int) Math.ceil((double) iPage.getTotal() / iPage.getSize());
        // 构建并返回分页视图对象
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
    public Articles getByArticaleId(Integer id) {
        LambdaQueryWrapper<Articles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Articles::getId, id);
        Articles articles = getOne(queryWrapper);
        if (articles != null) {
            articles.setCategory(categoriesMapper.selectById(articles.getCategoryId()).getName());
            return articles;
        }
        return null;
    }

    @Override
    public List<Articles> getHotArticles(Integer limit) {
        LambdaQueryWrapper<Articles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Articles::getStatus, "published");
        queryWrapper.orderByDesc(Articles::getViewCount);
        queryWrapper.last("limit " + limit);
        List<Articles> articles = list(queryWrapper);
        articles.forEach(article -> {
            article.setCategory(categoriesMapper.selectById(article.getCategoryId()).getName());
        });
        return articles;
    }
}




