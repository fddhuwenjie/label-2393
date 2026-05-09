package com.taopiaopiao.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String email;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
