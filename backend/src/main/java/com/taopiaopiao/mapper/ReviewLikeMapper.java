package com.taopiaopiao.mapper;

import com.taopiaopiao.entity.ReviewLike;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 评论点赞Mapper接口
 */
public interface ReviewLikeMapper {

    /**
     * 点赞（INSERT IGNORE利用唯一索引保证幂等性）
     */
    int insertIgnore(ReviewLike reviewLike);

    /**
     * 取消点赞
     */
    int deleteByUserAndReview(@Param("userId") Long userId, @Param("reviewId") Long reviewId);

    /**
     * 查询用户对某评论的点赞记录
     */
    ReviewLike findByUserAndReview(@Param("userId") Long userId, @Param("reviewId") Long reviewId);

    /**
     * 查询某评论的点赞数
     */
    Integer countByReviewId(@Param("reviewId") Long reviewId);

    /**
     * 批量查询用户对多条评论的点赞记录
     */
    List<ReviewLike> findByUserIdAndReviewIds(@Param("userId") Long userId, @Param("reviewIds") List<Long> reviewIds);

    /**
     * 删除某评论的所有点赞记录
     */
    int deleteByReviewId(@Param("reviewId") Long reviewId);
}
