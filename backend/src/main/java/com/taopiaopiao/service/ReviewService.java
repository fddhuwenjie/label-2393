package com.taopiaopiao.service;

import com.taopiaopiao.common.PageResult;
import com.taopiaopiao.entity.Review;
import com.taopiaopiao.mapper.ReviewMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 评论服务类
 */
@Service
public class ReviewService {
    
    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);
    
    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewLikeService reviewLikeService;
    
    /**
     * 根据ID获取评论
     */
    public Review getById(Long id) {
        return reviewMapper.findById(id);
    }
    
    /**
     * 获取电影的所有评论
     */
    public List<Review> getByMovieId(Long movieId) {
        return reviewMapper.findByMovieId(movieId);
    }
    
    /**
     * 分页获取电影评论（支持排序和点赞状态）
     *
     * @param movieId 电影ID
     * @param page    页码
     * @param size    每页数量
     * @param sortBy  排序方式：latest-按时间降序，hot-按点赞数降序
     * @param userId  当前登录用户ID（可为null，用于填充liked字段）
     */
    public PageResult<Review> getByMovieIdPaged(Long movieId, Integer page, Integer size, String sortBy, Long userId) {
        int offset = (page - 1) * size;
        List<Review> list = reviewMapper.findByMovieIdPaged(movieId, offset, size, sortBy);
        Long total = reviewMapper.countByMovieId(movieId);

        if (userId != null && !list.isEmpty()) {
            List<Long> reviewIds = list.stream().map(Review::getId).collect(Collectors.toList());
            Set<Long> likedIds = reviewLikeService.getLikedReviewIds(userId, reviewIds);
            for (Review review : list) {
                review.setLiked(likedIds.contains(review.getId()));
            }
        } else {
            for (Review review : list) {
                review.setLiked(false);
            }
        }

        return PageResult.of(list, total, page, size);
    }
    
    /**
     * 获取用户的所有评论
     */
    public List<Review> getByUserId(Long userId) {
        return reviewMapper.findByUserId(userId);
    }
    
    /**
     * 分页获取用户评论
     */
    public PageResult<Review> getByUserIdPaged(Long userId, Integer page, Integer size) {
        int offset = (page - 1) * size;
        List<Review> list = reviewMapper.findByUserIdPaged(userId, offset, size);
        Long total = reviewMapper.countByUserId(userId);
        return PageResult.of(list, total, page, size);
    }
    
    /**
     * 获取用户对某电影的评论
     */
    public Review getByUserAndMovie(Long userId, Long movieId) {
        return reviewMapper.findByUserAndMovie(userId, movieId);
    }
    
    /**
     * 添加或更新评论
     */
    @Transactional
    public Review saveOrUpdate(Long userId, Long movieId, Integer rating, String content) {
        Review existing = reviewMapper.findByUserAndMovie(userId, movieId);
        
        if (existing != null) {
            // 更新评论
            existing.setRating(rating);
            existing.setContent(content);
            reviewMapper.update(existing);
            logger.info("更新评论: userId={}, movieId={}, reviewId={}", userId, movieId, existing.getId());
        } else {
            // 新增评论
            existing = new Review();
            existing.setUserId(userId);
            existing.setMovieId(movieId);
            existing.setRating(rating);
            existing.setContent(content);
            reviewMapper.insert(existing);
            logger.info("新增评论: userId={}, movieId={}, reviewId={}", userId, movieId, existing.getId());
        }
        
        // 更新电影评分
        updateMovieRating(movieId);
        
        return existing;
    }
    
    /**
     * 删除评论
     */
    @Transactional
    public void delete(Long id) {
        Review review = reviewMapper.findById(id);
        if (review != null) {
            Long movieId = review.getMovieId();
            reviewLikeService.deleteByReviewId(id);
            reviewMapper.delete(id);
            logger.info("删除评论: reviewId={}", id);
            updateMovieRating(movieId);
        }
    }
    
    /**
     * 更新电影评分统计
     */
    private void updateMovieRating(Long movieId) {
        Double avgRating = reviewMapper.getAvgRating(movieId);
        Integer count = reviewMapper.getCount(movieId);
        movieService.updateRating(movieId, 
            avgRating != null ? Math.round(avgRating * 10) / 10.0 : 0.0, 
            count != null ? count : 0);
    }
}
