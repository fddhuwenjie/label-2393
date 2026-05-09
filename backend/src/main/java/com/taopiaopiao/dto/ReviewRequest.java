package com.taopiaopiao.dto;

import lombok.Data;
import javax.validation.constraints.*;

/**
 * 评论请求DTO
 */
@Data
public class ReviewRequest {
    
    @NotNull(message = "电影ID不能为空")
    private Long movieId;
    
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低为1")
    @Max(value = 5, message = "评分最高为5")
    private Integer rating;
    
    @Size(max = 1000, message = "评论内容不能超过1000字")
    private String content;
}
