package com.taopiaopiao.service;

import com.taopiaopiao.common.PageResult;
import com.taopiaopiao.entity.Movie;
import com.taopiaopiao.mapper.MovieMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 电影服务类
 */
@Service
public class MovieService {
    
    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);
    
    @Autowired
    private MovieMapper movieMapper;
    
    /**
     * 获取电影列表
     */
    public List<Movie> getList(String genre, String region, String keyword) {
        return movieMapper.findList(genre, region, keyword);
    }
    
    /**
     * 分页获取电影列表
     */
    public PageResult<Movie> getListPaged(String genre, String region, String keyword, Integer page, Integer size) {
        int offset = (page - 1) * size;
        List<Movie> list = movieMapper.findListPaged(genre, region, keyword, offset, size);
        Long total = movieMapper.countList(genre, region, keyword);
        logger.debug("查询电影列表: genre={}, region={}, keyword={}, page={}, total={}", 
                genre, region, keyword, page, total);
        return PageResult.of(list, total, page, size);
    }
    
    /**
     * 根据ID获取电影详情
     */
    public Movie getById(Long id) {
        return movieMapper.findById(id);
    }
    
    /**
     * 获取所有电影类型
     */
    public List<String> getAllGenres() {
        return movieMapper.findAllGenres();
    }
    
    /**
     * 获取所有地区
     */
    public List<String> getAllRegions() {
        return movieMapper.findAllRegions();
    }
    
    /**
     * 更新电影评分
     */
    public void updateRating(Long movieId, Double avgRating, Integer ratingCount) {
        movieMapper.updateRating(movieId, avgRating, ratingCount);
        logger.info("更新电影评分: movieId={}, avgRating={}, ratingCount={}", movieId, avgRating, ratingCount);
    }
}
