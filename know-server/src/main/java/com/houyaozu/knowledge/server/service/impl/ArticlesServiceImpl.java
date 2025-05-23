package com.houyaozu.knowledge.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.houyaozu.knowledge.common.exception.KnowledgeException;
import com.houyaozu.knowledge.common.login.LoginUserHolder;
import com.houyaozu.knowledge.common.result.ResultCodeEnum;
import com.houyaozu.knowledge.pojo.DTO.PageDTO;
import com.houyaozu.knowledge.pojo.VO.PageVO;
import com.houyaozu.knowledge.pojo.domain.ArticleTags;
import com.houyaozu.knowledge.pojo.domain.Articles;
import com.houyaozu.knowledge.pojo.domain.Categories;
import com.houyaozu.knowledge.pojo.domain.UserFavorites;
import com.houyaozu.knowledge.server.mapper.*;
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
    @Autowired
    private UserFavoritesMapper userFavoritesMapper;
    @Autowired
    private ArticleTagsMapper articleTagsMapper;
    @Autowired
    private TagsMapper tagsMapper;

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
            extracted(article);
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

    /**
     * 根据文章ID获取文章详情
     *
     * @param id 文章ID
     * @return 文章对象，如果不存在则返回null
     */
    @Override
    public Articles getByArticaleId(Integer id) {
        LambdaQueryWrapper<Articles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Articles::getId, id);
        Articles articles = getOne(queryWrapper);
        if (articles != null) {
            // 设置文章类别名称
            articles.setCategory(categoriesMapper.selectById(articles.getCategoryId()).getName());
            // 提取公共方法，用于设置文章是否被当前用户收藏
            extracted(articles);
            return articles;
        }
        return null;
    }

    /**
     * 获取热门文章列表
     *
     * @param limit 限制返回的文章数量
     * @return 热门文章列表
     */
    @Override
    public List<Articles> getHotArticles(Integer limit) {
        LambdaQueryWrapper<Articles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Articles::getStatus, "published");
        queryWrapper.orderByDesc(Articles::getViewCount);
        queryWrapper.last("limit " + limit);
        List<Articles> articles = list(queryWrapper);
        articles.forEach(article -> {
            // 设置文章类别名称
            article.setCategory(categoriesMapper.selectById(article.getCategoryId()).getName());
            // 提取公共方法，用于设置文章是否被当前用户收藏
            extracted(article);
        });
        return articles;
    }

    /**
     * 收藏文章
     *
     * @param id 文章ID
     */
    @Override
    public void favorite(Integer id) {
        // 检查用户是否已登录
        if (LoginUserHolder.getLoginUser() == null) {
            throw new KnowledgeException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }
        UserFavorites userFavorites = userFavoritesMapper.existOne(LoginUserHolder.getLoginUser().getUserId(), "article", id);
        if (userFavorites != null && userFavorites.getDelFlag() == 1) {
            userFavorites.setDelFlag(0);
            userFavoritesMapper.updateFlag(userFavorites);
            return;
        }
        userFavoritesMapper.insert(UserFavorites.builder()
                .userId(LoginUserHolder.getLoginUser().getUserId())
                .contentType("article")
                .contentId(id)
                .build());
    }

    /**
     * 取消收藏文章
     *
     * @param id 文章ID
     */
    @Override
    public void unfavorite(Integer id) {
        // 检查用户是否已登录
        if (LoginUserHolder.getLoginUser() == null) {
            throw new KnowledgeException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }
        LambdaQueryWrapper<UserFavorites> article = new LambdaQueryWrapper<UserFavorites>().eq(UserFavorites::getUserId, LoginUserHolder.getLoginUser().getUserId())
                .eq(UserFavorites::getContentType, "article")
                .eq(UserFavorites::getContentId, id);
        if (userFavoritesMapper.exists(article)) {
            userFavoritesMapper.delete(new LambdaQueryWrapper<UserFavorites>().eq(UserFavorites::getUserId, LoginUserHolder.getLoginUser().getUserId())
                    .eq(UserFavorites::getContentType, "article")
                    .eq(UserFavorites::getContentId, id));
        }
    }

    @Override
    public List<String> getTagsById(Integer id) {
        return articleTagsMapper.selectList(new LambdaQueryWrapper<ArticleTags>().eq(ArticleTags::getArticleId, id))
                .stream().map(articleTags -> tagsMapper.selectById(articleTags.getTagId()).getName()).toList();
    }

    /**
     * 判断用户是否收藏了某篇文章
     *
     * @param articles 文章对象
     */
    public void extracted(Articles articles) {
        if (LoginUserHolder.getLoginUser() != null) {
            // 设置文章是否被当前用户收藏的标志
            articles.setIsFeatured(userFavoritesMapper.exists(new LambdaQueryWrapper<UserFavorites>().eq(UserFavorites::getUserId, LoginUserHolder.getLoginUser().getUserId())
                    .eq(UserFavorites::getContentType, "article")
                    .eq(UserFavorites::getContentId, articles.getId())) ? 1 : 0);
        }
    }
}




