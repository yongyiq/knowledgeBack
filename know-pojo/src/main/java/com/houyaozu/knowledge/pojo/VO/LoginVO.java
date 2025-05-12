package com.houyaozu.knowledge.pojo.VO;

import com.houyaozu.knowledge.pojo.domain.Users;
import lombok.Data;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
@Data
public class LoginVO {
    String token;
    UserInfoVO userInfo;
}
