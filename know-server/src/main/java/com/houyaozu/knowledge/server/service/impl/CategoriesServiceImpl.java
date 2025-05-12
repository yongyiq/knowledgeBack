package com.houyaozu.knowledge.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.houyaozu.knowledge.pojo.domain.Categories;
import com.houyaozu.knowledge.server.mapper.CategoriesMapper;
import com.houyaozu.knowledge.server.service.CategoriesService;
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


    @Override
    public List<Categories> getList() {
        return list();
    }
}




