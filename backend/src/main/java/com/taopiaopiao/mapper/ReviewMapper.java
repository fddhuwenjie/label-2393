package com.taopiaopiao.mapper;

import com.taopiaopiao.entity.Review;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 评论Mapper接口
 */
public interface ReviewMapper {
    
    /**
     * 根据ID查询评论
     */
    Review findById(@Param("id") Long id);
    
    /**
     * 根据电影ID查询评论列表
     */
    List<Review> findByMovieId(@Param("movieId") Long movieId);
    
    /**
     * 根据电影ID分页查询评论（支持排序）
     */
    List<Review> findByMovieIdPaged(@Param("movieId") Long movieId, 
                                     @Param("offset") Integer offset, 
                                     @Param("size") Integer size,
                                     @Param("sortBy") String sortBy);
    
    /**
     * 统计电影评论总数
     */
    Long countByMovieId(@Param("movieId") Long movieId);
    
    /**
     * 根据用户ID查询评论列表
     */
    List<Review> findByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID分页查询评论
     */
    List<Review> findByUserIdPaged(@Param("userId") Long userId, 
                                    @Param("offset") Integer offset, 
                                    @Param("size") Integer size);
    
    /**
     * 统计用户评论总数
     */
    Long countByUserId(@Param("userId") Long userId);
    
    /**
     * 查询用户对某电影的评论
     */
    Review findByUserAndMovie(@Param("userId") Long userId, @Param("movieId") Long movieId);
    
    /**
     * 插入评论
     */
    int insert(Review review);
    
    /**
     * 更新评论
     */
    int update(Review review);
    
    /**
     * 删除评论
     */
    int delete(@Param("id") Long id);
    
    /**
     * 统计电影平均评分
     */
    Double getAvgRating(@Param("movieId") Long movieId);
    
    /**
     * 统计电影评论数
     */
    Integer getCount(@Param("movieId") Long movieId);
}
