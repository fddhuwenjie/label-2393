package com.taopiaopiao.controller;

import com.taopiaopiao.common.PageResult;
import com.taopiaopiao.common.Result;
import com.taopiaopiao.dto.ReviewRequest;
import com.taopiaopiao.entity.Review;
import com.taopiaopiao.exception.ForbiddenException;
import com.taopiaopiao.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 评论控制器
 */
@RestController
@RequestMapping("/api/review")
@Validated
public class ReviewController {
    
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);
    
    @Autowired
    private ReviewService reviewService;
    
    /**
     * 获取电影的评论（分页，支持排序）
     *
     * @param movieId 电影ID
     * @param page    页码
     * @param size    每页数量
     * @param sortBy  排序方式：latest-按时间降序（默认），hot-按点赞数降序
     */
    @GetMapping("/movie/{movieId}")
    public Result<PageResult<Review>> getByMovieId(
            @PathVariable Long movieId,
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "页码最小为1") Integer page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) Integer size,
            @RequestParam(defaultValue = "latest") String sortBy,
            HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("currentUserId");
        PageResult<Review> reviews = reviewService.getByMovieIdPaged(movieId, page, size, sortBy, userId);
        return Result.success(reviews);
    }
    
    /**
     * 获取用户的评论（分页）
     */
    @GetMapping("/user/{userId}")
    public Result<PageResult<Review>> getByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) Integer size) {
        PageResult<Review> reviews = reviewService.getByUserIdPaged(userId, page, size);
        return Result.success(reviews);
    }
    
    /**
     * 获取用户对某电影的评论
     */
    @GetMapping("/check")
    public Result<Review> checkUserReview(
            @RequestParam Long userId,
            @RequestParam Long movieId) {
        Review review = reviewService.getByUserAndMovie(userId, movieId);
        return Result.success(review);
    }
    
    /**
     * 添加或更新评论（需要登录）
     */
    @PostMapping
    public Result<Review> save(@Valid @RequestBody ReviewRequest request, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("currentUserId");
        
        logger.info("用户发表评论: userId={}, movieId={}, rating={}", 
                userId, request.getMovieId(), request.getRating());
        
        Review review = reviewService.saveOrUpdate(userId, request.getMovieId(), 
                request.getRating(), request.getContent());
        return Result.success(review);
    }
    
    /**
     * 删除评论（需要登录，只能删除自己的评论）
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        Long currentUserId = (Long) request.getAttribute("currentUserId");
        
        // 获取评论信息，校验权限
        Review review = reviewService.getById(id);
        if (review == null) {
            return Result.error("评论不存在");
        }
        
        // 权限校验：只能删除自己的评论
        if (!review.getUserId().equals(currentUserId)) {
            logger.warn("用户尝试删除他人评论: userId={}, reviewId={}, reviewOwnerId={}", 
                    currentUserId, id, review.getUserId());
            throw new ForbiddenException("无权删除他人评论");
        }
        
        logger.info("用户删除评论: userId={}, reviewId={}", currentUserId, id);
        reviewService.delete(id);
        return Result.success();
    }
}
