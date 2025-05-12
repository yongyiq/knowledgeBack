package com.houyaozu.knowledge.pojo.VO;

import lombok.Data;

import java.util.Date;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
@Data
public class UserInfoVO {
    private Integer id;

    /**
     *
     */
    private String username;


    /**
     *
     */
    private String email;

    /**
     *
     */
    private String avatar;

    /**
     *
     */
    private Object role;

    /**
     *
     */
    private String bio;

    /**
     *
     */
    private Date createTime;
}
