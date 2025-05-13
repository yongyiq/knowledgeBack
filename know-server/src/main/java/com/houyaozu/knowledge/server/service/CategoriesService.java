package com.houyaozu.knowledge.server.service;

import com.houyaozu.knowledge.pojo.domain.Categories;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author yongyiq
* @description 针对表【categories】的数据库操作Service
* @createDate 2025-05-07 19:15:45
*/
public interface CategoriesService extends IService<Categories> {

    List<Categories> getCategorieList();
}
