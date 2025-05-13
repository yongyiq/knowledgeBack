package com.houyaozu.knowledge.pojo.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
@Data
public class ArticlesVO {
    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private String title;


    /**
     *
     */
    private String summary;

    /**
     *
     */
    private String category;

    /**
     *
     */
    private Integer viewCount;


    /**
     *
     */
    private Date createTime;

}
