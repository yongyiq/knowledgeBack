package com.houyaozu.knowledge.server.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.houyaozu.knowledge.pojo.domain.UserFavorites;
import org.apache.ibatis.annotations.Mapper;

/**
* @author yongyiq
* @description 针对表【user_favorites】的数据库操作Mapper
* @createDate 2025-05-07 19:33:32
* @Entity generator.domain.UserFavorites
*/

@Mapper
public interface UserFavoritesMapper extends BaseMapper<UserFavorites> {

}




