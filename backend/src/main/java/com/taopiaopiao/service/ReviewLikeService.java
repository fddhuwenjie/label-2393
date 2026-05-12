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
     * 点赞/取消点赞
     * @param userId 用户ID
     * @param reviewId 评论ID
     * @return true表示点赞成功，false表示取消点赞成功
     */
    @Transactional
    public boolean toggleLike(Long userId, Long reviewId) {
        ReviewLike existing = reviewLikeMapper.findByUserAndReview(userId, reviewId);
        
        if (existing != null) {
            // 已点赞，取消点赞
            reviewLikeMapper.delete(userId, reviewId);
            logger.info("用户取消点赞: userId={}, reviewId={}", userId, reviewId);
            return false;
        } else {
            // 未点赞，添加点赞
            try {
                ReviewLike reviewLike = new ReviewLike();
                reviewLike.setUserId(userId);
                reviewLike.setReviewId(reviewId);
                reviewLikeMapper.insert(reviewLike);
                logger.info("用户点赞: userId={}, reviewId={}", userId, reviewId);
                return true;
            } catch (DuplicateKeyException e) {
                // 并发场景下，数据库唯一索引保证幂等性
                logger.warn("并发点赞冲突，数据库已保证幂等性: userId={}, reviewId={}", userId, reviewId);
                return true;
            }
        }
    }
    
    /**
     * 获取评论点赞数
     */
    public Integer getLikeCount(Long reviewId) {
        Integer count = reviewLikeMapper.countByReviewId(reviewId);
        return count != null ? count : 0;
    }
    
    /**
     * 检查用户是否已点赞
     */
    public boolean isLiked(Long userId, Long reviewId) {
        if (userId == null) {
            return false;
        }
        return reviewLikeMapper.findByUserAndReview(userId, reviewId) != null;
    }
}
