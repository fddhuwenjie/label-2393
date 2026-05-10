package com.taopiaopiao.service;

import com.taopiaopiao.entity.ReviewLike;
import com.taopiaopiao.mapper.ReviewLikeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 评论点赞服务类
 */
@Service
public class ReviewLikeService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewLikeService.class);

    @Autowired
    private ReviewLikeMapper reviewLikeMapper;

    /**
     * 点赞或取消点赞（toggle操作）
     * 利用数据库唯一索引uk_user_review保证幂等性：
     * - 点赞：INSERT IGNORE，若已存在则静默忽略
     * - 取消：DELETE WHERE user_id AND review_id
     *
     * @param userId   当前用户ID
     * @param reviewId 评论ID
     * @return true=已点赞, false=已取消点赞
     */
    @Transactional
    public boolean toggleLike(Long userId, Long reviewId) {
        ReviewLike existing = reviewLikeMapper.findByUserAndReview(userId, reviewId);

        if (existing != null) {
            reviewLikeMapper.deleteByUserAndReview(userId, reviewId);
            logger.info("取消点赞: userId={}, reviewId={}", userId, reviewId);
            return false;
        } else {
            ReviewLike like = new ReviewLike();
            like.setUserId(userId);
            like.setReviewId(reviewId);
            try {
                reviewLikeMapper.insertIgnore(like);
            } catch (DuplicateKeyException e) {
                logger.info("重复点赞已忽略(唯一索引): userId={}, reviewId={}", userId, reviewId);
            }
            logger.info("点赞成功: userId={}, reviewId={}", userId, reviewId);
            return true;
        }
    }

    /**
     * 查询用户是否已点赞某评论
     */
    public boolean hasLiked(Long userId, Long reviewId) {
        return reviewLikeMapper.findByUserAndReview(userId, reviewId) != null;
    }

    /**
     * 查询某评论的点赞数
     */
    public Integer getLikeCount(Long reviewId) {
        Integer count = reviewLikeMapper.countByReviewId(reviewId);
        return count != null ? count : 0;
    }

    /**
     * 批量查询用户对多条评论的点赞状态
     *
     * @param userId    当前用户ID
     * @param reviewIds 评论ID列表
     * @return 已点赞的评论ID集合
     */
    public Set<Long> getLikedReviewIds(Long userId, List<Long> reviewIds) {
        if (reviewIds == null || reviewIds.isEmpty()) {
            return Set.of();
        }
        List<ReviewLike> likes = reviewLikeMapper.findByUserIdAndReviewIds(userId, reviewIds);
        return likes.stream().map(ReviewLike::getReviewId).collect(Collectors.toSet());
    }

    /**
     * 删除某评论的所有点赞记录
     */
    public void deleteByReviewId(Long reviewId) {
        reviewLikeMapper.deleteByReviewId(reviewId);
    }
}
