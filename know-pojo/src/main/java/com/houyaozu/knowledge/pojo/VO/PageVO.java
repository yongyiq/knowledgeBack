package com.houyaozu.knowledge.pojo.VO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @ Author     ：侯耀祖
 * @ Description：分页结果视图对象
 */
@Data
@Builder
public class PageVO {
    private List content;
    private Long totalElements;
    private Integer totalPages;
    private Long size;
    private Long number;
    private Boolean first;
    private Boolean last;
    private Boolean empty;
}
