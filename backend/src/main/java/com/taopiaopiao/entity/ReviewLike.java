package com.taopiaopiao.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 评论点赞实体类
 */
@Data
public class ReviewLike {
    private Long id;
    private Long userId;
    private Long reviewId;
    private LocalDateTime createTime;
}
