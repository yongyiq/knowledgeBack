package com.houyaozu.knowledge.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.houyaozu.knowledge.pojo.domain.Competitions;
import com.houyaozu.knowledge.server.mapper.CompetitionsMapper;
import com.houyaozu.knowledge.server.service.CompetitionsService;
import org.springframework.stereotype.Service;

/**
* @author yongyiq
* @description 针对表【competitions】的数据库操作Service实现
* @createDate 2025-05-07 19:43:08
*/
@Service
public class CompetitionsServiceImpl extends ServiceImpl<CompetitionsMapper, Competitions>
    implements CompetitionsService {

}




