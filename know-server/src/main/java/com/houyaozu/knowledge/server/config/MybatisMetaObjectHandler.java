package com.houyaozu.knowledge.server.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.houyaozu.knowledge.common.login.LoginUserHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ Author     ：侯耀祖
 * @ Description：MyBatis-Plus自动填充处理器
 */
@Component
public class MybatisMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        // 创建时间自动填充
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());

        // 创建人自动填充（如果有登录用户）
        try {
            if (LoginUserHolder.getLoginUser() != null) {
                this.strictInsertFill(metaObject, "createBy", String.class, LoginUserHolder.getLoginUser().getUsername());
            }
        } catch (Exception e) {
            // 如果获取登录用户失败，不进行填充
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间自动填充
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());

        // 更新人自动填充（如果有登录用户）
        try {
            if (LoginUserHolder.getLoginUser() != null) {
                this.strictUpdateFill(metaObject, "updateBy", String.class, LoginUserHolder.getLoginUser().getUsername());
            }
        } catch (Exception e) {
            // 如果获取登录用户失败，不进行填充
        }
    }
}
