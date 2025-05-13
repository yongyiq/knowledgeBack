package com.houyaozu.knowledge.server.controller.knowController;

import com.houyaozu.knowledge.common.result.Result;
import com.houyaozu.knowledge.pojo.domain.Categories;
import com.houyaozu.knowledge.server.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
@RestController
@RequestMapping("/knowledge/categories")
public class CategoriesController {

    @Autowired
    private CategoriesService categoriesService;

    @GetMapping()
    public Result getCategories() {
        List<Categories> list = categoriesService.getCategorieList();
        return Result.ok(list);
    }
}
