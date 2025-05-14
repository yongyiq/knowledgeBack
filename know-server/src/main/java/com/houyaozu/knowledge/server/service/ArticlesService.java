package com.houyaozu.knowledge.server.service;

import com.houyaozu.knowledge.pojo.DTO.PageDTO;
import com.houyaozu.knowledge.pojo.VO.ArticlesVO;
import com.houyaozu.knowledge.pojo.VO.PageVO;
import com.houyaozu.knowledge.pojo.domain.Articles;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author yongyiq
* @description 针对表【articles】的数据库操作Service
* @createDate 2025-05-07 19:15:44
*/
public interface ArticlesService extends IService<Articles> {

    PageVO getPages(PageDTO pageDTO);

    Articles getByArticaleId(Integer id);

    List<Articles> getHotArticles(Integer limit);

    void favorite(Integer id);

    void unfavorite(Integer id);
}
