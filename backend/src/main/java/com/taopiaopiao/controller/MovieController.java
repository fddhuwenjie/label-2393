package com.taopiaopiao.controller;

import com.taopiaopiao.common.PageResult;
import com.taopiaopiao.common.Result;
import com.taopiaopiao.entity.Movie;
import com.taopiaopiao.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电影控制器
 */
@RestController
@RequestMapping("/api/movie")
@Validated
public class MovieController {
    
    @Autowired
    private MovieService movieService;
    
    /**
     * 获取电影列表（分页）
     */
    @GetMapping("/list")
    public Result<PageResult<Movie>> list(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "页码最小为1") Integer page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) Integer size) {
        PageResult<Movie> movies = movieService.getListPaged(genre, region, keyword, page, size);
        return Result.success(movies);
    }
    
    /**
     * 获取电影详情
     */
    @GetMapping("/{id}")
    public Result<Movie> getById(@PathVariable Long id) {
        Movie movie = movieService.getById(id);
        if (movie == null) {
            return Result.error("电影不存在");
        }
        return Result.success(movie);
    }
    
    /**
     * 获取筛选选项（类型和地区）
     */
    @GetMapping("/filters")
    public Result<Map<String, List<String>>> getFilters() {
        Map<String, List<String>> filters = new HashMap<>();
        filters.put("genres", movieService.getAllGenres());
        filters.put("regions", movieService.getAllRegions());
        return Result.success(filters);
    }
}
