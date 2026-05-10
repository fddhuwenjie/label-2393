package com.taopiaopiao.controller;

import com.taopiaopiao.common.Result;
import com.taopiaopiao.service.ReviewLikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 评论点赞控制器
 */
@RestController
@RequestMapping("/api/review/like")
public class ReviewLikeController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewLikeController.class);

    @Autowired
    private ReviewLikeService reviewLikeService;

    /**
     * 点赞/取消点赞（toggle操作）
     * 幂等性由数据库唯一索引uk_user_review保证，并发场景下同一用户对同一评论不会产生多条点赞记录
     */
    @PostMapping("/{reviewId}")
    public Result<Map<String, Object>> toggleLike(@PathVariable Long reviewId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("currentUserId");

        logger.info("点赞操作: userId={}, reviewId={}", userId, reviewId);

        boolean liked = reviewLikeService.toggleLike(userId, reviewId);
        Integer likeCount = reviewLikeService.getLikeCount(reviewId);

        Map<String, Object> data = new HashMap<>();
        data.put("liked", liked);
        data.put("likeCount", likeCount);

        return Result.success(data);
    }

    /**
     * 查询当前用户对某评论的点赞状态
     */
    @GetMapping("/status/{reviewId}")
    public Result<Map<String, Object>> getLikeStatus(@PathVariable Long reviewId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("currentUserId");

        boolean liked = reviewLikeService.hasLiked(userId, reviewId);
        Integer likeCount = reviewLikeService.getLikeCount(reviewId);

        Map<String, Object> data = new HashMap<>();
        data.put("liked", liked);
        data.put("likeCount", likeCount);

        return Result.success(data);
    }
}
