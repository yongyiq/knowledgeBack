package com.houyaozu.knowledge.server.service;

import com.houyaozu.knowledge.pojo.DTO.PasswordDTO;
import com.houyaozu.knowledge.pojo.DTO.UserDTO;
import com.houyaozu.knowledge.pojo.DTO.UserInfoDTO;
import com.houyaozu.knowledge.pojo.VO.LoginVO;
import com.houyaozu.knowledge.pojo.VO.UserInfoVO;
import com.houyaozu.knowledge.pojo.domain.Users;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author yongyiq
* @description 针对表【users】的数据库操作Service
* @createDate 2025-05-07 19:15:45
*/
public interface UsersService extends IService<Users> {

    void register(Users users);

    LoginVO login(UserDTO userDTO);

    void logout();

    UserInfoVO getUserInfo();

    void updateUserInfo(UserInfoDTO userInfo);

    void updatePassword(PasswordDTO passwordDTO);
}
