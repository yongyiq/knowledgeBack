package com.houyaozu.knowledge.server.controller.user;

import com.houyaozu.knowledge.common.result.Result;
import com.houyaozu.knowledge.pojo.DTO.PasswordDTO;
import com.houyaozu.knowledge.pojo.DTO.UserDTO;
import com.houyaozu.knowledge.pojo.DTO.UserInfoDTO;
import com.houyaozu.knowledge.pojo.VO.LoginVO;
import com.houyaozu.knowledge.pojo.VO.UserInfoVO;
import com.houyaozu.knowledge.pojo.domain.Users;
import com.houyaozu.knowledge.server.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UsersService usersService;
    @PostMapping("/register")
    public Result register(@RequestBody Users users) {
        usersService.register(users);
        return Result.ok();
    }
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        LoginVO loginVO = usersService.login(userDTO);
        return Result.ok(loginVO);
    }
    @PostMapping("/logout")
    public Result login() {
        usersService.logout();
        return Result.ok();
    }
    @GetMapping("/info")
    public Result getUserInfo() {
        UserInfoVO userInfoVO = usersService.getUserInfo();
        return Result.ok(userInfoVO);
    }
    @PutMapping("/update")
    public Result updateUserInfo(@RequestBody UserInfoDTO userInfo) {
        usersService.updateUserInfo(userInfo);
        return Result.ok();
    }
    @PutMapping("/change-password")
    public Result updatePassword(@RequestBody PasswordDTO passwordDTO) {
        usersService.updatePassword(passwordDTO);
        return Result.ok();
    }
}
