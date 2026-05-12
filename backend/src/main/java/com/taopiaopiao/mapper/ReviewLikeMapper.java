package com.taopiaopiao.mapper;

import com.taopiaopiao.entity.ReviewLike;
import org.apache.ibatis.annotations.Param;

/**
 * 评论点赞Mapper接口
 */
public interface ReviewLikeMapper {
    
    /**
     * 插入点赞记录
     */
    int insert(ReviewLike reviewLike);
    
    /**
     * 删除点赞记录
     */
    int delete(@Param("userId") Long userId, @Param("reviewId") Long reviewId);
    
    /**
     * 统计评论点赞数
     */
    Integer countByReviewId(@Param("reviewId") Long reviewId);
    
    /**
     * 检查用户是否已点赞
     */
    ReviewLike findByUserAndReview(@Param("userId") Long userId, @Param("reviewId") Long reviewId);
}
