package com.taopiaopiao.service;

import com.taopiaopiao.entity.ReviewLike;
import com.taopiaopiao.mapper.ReviewLikeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 评论点赞服务类
 */
@Service
public class ReviewLikeService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewLikeService.class);

    @Autowired
    private ReviewLikeMapper reviewLikeMapper;

    /**
     * 点赞（幂等性：使用数据库唯一索引保证）
     * @return true 表示点赞成功，false 表示已点赞（通过唯一索引保证幂等性）
     */
    @Transactional
    public boolean like(Long userId, Long reviewId) {
        ReviewLike like = new ReviewLike();
        like.setUserId(userId);
        like.setReviewId(reviewId);

        try {
            int rows = reviewLikeMapper.insert(like);
            if (rows > 0) {
                logger.info("点赞成功: userId={}, reviewId={}", userId, reviewId);
                return true;
            }
        } catch (DuplicateKeyException e) {
            // 唯一索引冲突，说明已点赞，返回 false
            logger.info("已点赞（通过唯一索引保证幂等）: userId={}, reviewId={}", userId, reviewId);
        }

        return false;
    }

    /**
     * 取消点赞
     */
    @Transactional
    public boolean unlike(Long userId, Long reviewId) {
        int rows = reviewLikeMapper.delete(userId, reviewId);
        if (rows > 0) {
            logger.info("取消点赞成功: userId={}, reviewId={}", userId, reviewId);
            return true;
        }
        return false;
    }

    /**
     * 检查用户是否已点赞
     */
    public boolean isLiked(Long userId, Long reviewId) {
        return reviewLikeMapper.findByUserAndReview(userId, reviewId) != null;
    }

    /**
     * 统计评论点赞数
     */
    public Long getLikeCount(Long reviewId) {
        Long count = reviewLikeMapper.countByReviewId(reviewId);
        return count != null ? count : 0L;
    }

    /**
     * 根据评论ID删除所有点赞记录（删除评论时调用）
     */
    @Transactional
    public void deleteByReviewId(Long reviewId) {
        reviewLikeMapper.deleteByReviewId(reviewId);
    }
}
