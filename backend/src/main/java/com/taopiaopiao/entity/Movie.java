package com.taopiaopiao.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 电影实体类
 */
@Data
public class Movie {
    private Long id;
    private String title;
    private String poster;
    private String director;
    private String actors;
    private String genre;
    private String region;
    private LocalDate releaseDate;
    private Integer duration;
    private String description;
    private BigDecimal avgRating;
    private Integer ratingCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
