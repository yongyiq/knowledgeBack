package com.houyaozu.knowledge.pojo.DTO;

import lombok.Data;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
@Data
public class PageDTO {
    private Integer page;
    private Integer size;
    private String category;
    private String status;
    private String keyword;
    private String sort;
}
