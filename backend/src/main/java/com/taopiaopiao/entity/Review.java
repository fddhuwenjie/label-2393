package com.taopiaopiao.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 评论实体类
 */
@Data
public class Review {
    private Long id;
    private Long userId;
    private Long movieId;
    private Integer rating;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 关联字段
    private String username;
    private String nickname;
    private String avatar;
    private String movieTitle;
    private String moviePoster;
}
