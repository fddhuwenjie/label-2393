package com.taopiaopiao.mapper;

import com.taopiaopiao.entity.Movie;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 电影Mapper接口
 */
public interface MovieMapper {
    
    /**
     * 查询电影列表（支持筛选）
     */
    List<Movie> findList(@Param("genre") String genre, 
                         @Param("region") String region,
                         @Param("keyword") String keyword);
    
    /**
     * 分页查询电影列表
     */
    List<Movie> findListPaged(@Param("genre") String genre, 
                              @Param("region") String region,
                              @Param("keyword") String keyword,
                              @Param("offset") Integer offset,
                              @Param("size") Integer size);
    
    /**
     * 统计电影总数
     */
    Long countList(@Param("genre") String genre, 
                   @Param("region") String region,
                   @Param("keyword") String keyword);
    
    /**
     * 根据ID查询电影
     */
    Movie findById(@Param("id") Long id);
    
    /**
     * 获取所有电影类型
     */
    List<String> findAllGenres();
    
    /**
     * 获取所有地区
     */
    List<String> findAllRegions();
    
    /**
     * 更新电影评分
     */
    int updateRating(@Param("id") Long id, 
                     @Param("avgRating") Double avgRating, 
                     @Param("ratingCount") Integer ratingCount);
}
