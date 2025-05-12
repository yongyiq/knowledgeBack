package com.houyaozu.knowledge.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.houyaozu.knowledge.pojo.domain.UserFavorites;
import com.houyaozu.knowledge.server.mapper.UserFavoritesMapper;
import com.houyaozu.knowledge.server.service.UserFavoritesService;
import org.springframework.stereotype.Service;

/**
* @author yongyiq
* @description 针对表【user_favorites】的数据库操作Service实现
* @createDate 2025-05-07 19:43:09
*/
@Service
public class UserFavoritesServiceImpl extends ServiceImpl<UserFavoritesMapper, UserFavorites>
    implements UserFavoritesService {

}




