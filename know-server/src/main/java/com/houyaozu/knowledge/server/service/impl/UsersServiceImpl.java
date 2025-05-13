package com.houyaozu.knowledge.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.houyaozu.knowledge.common.exception.KnowledgeException;
import com.houyaozu.knowledge.common.login.LoginUser;
import com.houyaozu.knowledge.common.login.LoginUserHolder;
import com.houyaozu.knowledge.common.result.ResultCodeEnum;
import com.houyaozu.knowledge.common.utils.JwtUtil;
import com.houyaozu.knowledge.pojo.DTO.PasswordDTO;
import com.houyaozu.knowledge.pojo.DTO.UserDTO;
import com.houyaozu.knowledge.pojo.DTO.UserInfoDTO;
import com.houyaozu.knowledge.pojo.VO.LoginVO;
import com.houyaozu.knowledge.pojo.VO.UserInfoVO;
import com.houyaozu.knowledge.pojo.domain.Users;
import com.houyaozu.knowledge.server.mapper.UsersMapper;
import com.houyaozu.knowledge.server.service.UsersService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
* @author yongyiq
* @description 针对表【users】的数据库操作Service实现
* @createDate 2025-05-07 19:43:09
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService {

    @Override
    public void register(Users users) {
        String password = DigestUtils.md5Hex(users.getPassword());
        users.setPassword(password);
        save(users);
    }

    @Override
    public LoginVO login(UserDTO userDTO) {
        String username = userDTO.getUsername();
        if (username == null) {
            throw new KnowledgeException(ResultCodeEnum.PARAM_ERROR);
        }
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUsername, username);
        Users users = getOne(queryWrapper);
        if (users == null) {
            throw new KnowledgeException(ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR);
        }
        if (!users.getPassword().equals(DigestUtils.md5Hex(userDTO.getPassword()))) {
            throw new KnowledgeException(ResultCodeEnum.ADMIN_PASSWORD_ERROR);
        }
        String token = JwtUtil.createToken(users.getId(), users.getNickname());
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(users, userInfoVO);
        loginVO.setUserInfo(userInfoVO);
        return loginVO;
        // TODO redis保存登录信息
    }

    @Override
    public void logout() {
        // TODO redis清除登录信息
        LoginUserHolder.clear();
    }

    @Override
    public UserInfoVO getUserInfo() {
        LoginUser loginUser = LoginUserHolder.getLoginUser();
        if (Objects.isNull(loginUser)){
            throw new KnowledgeException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }
        Integer userId = loginUser.getUserId();
        Users user = getById(userId);
        UserInfoVO userInfo = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfo);
        return userInfo;
    }

    @Override
    public void updateUserInfo(UserInfoDTO userInfo) {
        LoginUser loginUser = LoginUserHolder.getLoginUser();
        if (Objects.isNull(loginUser)){
            throw new KnowledgeException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }
        Integer userId = loginUser.getUserId();
        Users user = getById(userId);
        BeanUtils.copyProperties(userInfo, user);
        updateById(user);
    }

    @Override
    public void updatePassword(PasswordDTO passwordDTO) {
        LoginUser loginUser = LoginUserHolder.getLoginUser();
        if (Objects.isNull(loginUser)){
            throw new KnowledgeException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }
        Integer userId = loginUser.getUserId();
        Users user = getById(userId);
        if (!user.getPassword().equals(passwordDTO.getOldPassword())){
            throw new KnowledgeException(ResultCodeEnum.ADMIN_PASSWORD_ERROR);
        }
        user.setPassword(passwordDTO.getNewPassword());
        updateById(user);
    }

}




