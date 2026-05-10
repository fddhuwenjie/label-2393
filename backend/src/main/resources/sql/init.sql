-- 淘票票影评社区数据库初始化脚本
-- 设置字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS taopiaopiao 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

USE taopiaopiao;

-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `avatar` VARCHAR(255) COMMENT '头像URL',
    `email` VARCHAR(100) COMMENT '邮箱',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 电影表
DROP TABLE IF EXISTS `movie`;
CREATE TABLE `movie` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '电影ID',
    `title` VARCHAR(200) NOT NULL COMMENT '电影名称',
    `poster` VARCHAR(500) COMMENT '海报URL',
    `director` VARCHAR(100) COMMENT '导演',
    `actors` VARCHAR(500) COMMENT '主演',
    `genre` VARCHAR(100) COMMENT '类型',
    `region` VARCHAR(50) COMMENT '地区',
    `release_date` DATE COMMENT '上映日期',
    `duration` INT COMMENT '时长(分钟)',
    `description` TEXT COMMENT '简介',
    `avg_rating` DECIMAL(2,1) DEFAULT 0 COMMENT '平均评分',
    `rating_count` INT DEFAULT 0 COMMENT '评分人数',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='电影表';

-- 评论表
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `movie_id` BIGINT NOT NULL COMMENT '电影ID',
    `rating` INT NOT NULL COMMENT '评分(1-5)',
    `content` TEXT COMMENT '评论内容',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_user_movie` (`user_id`, `movie_id`),
    KEY `idx_movie_id` (`movie_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- 插入测试用户数据 (密码均为123456，使用BCrypt加密)
INSERT INTO `user` (`username`, `password`, `nickname`, `avatar`, `email`) VALUES
('admin', '$2a$10$GFPZ3oF3H0b3IgJ1ndXfS.jE1ZFh/Xylcp7AWp83kK7skNzYjbBpO', '管理员', '/images/default-avatar.svg', 'admin@taopiaopiao.com'),
('user1', '$2a$10$GFPZ3oF3H0b3IgJ1ndXfS.jE1ZFh/Xylcp7AWp83kK7skNzYjbBpO', '影迷小王', '/images/default-avatar.svg', 'user1@taopiaopiao.com'),
('user2', '$2a$10$GFPZ3oF3H0b3IgJ1ndXfS.jE1ZFh/Xylcp7AWp83kK7skNzYjbBpO', '电影达人', '/images/default-avatar.svg', 'user2@taopiaopiao.com');

-- 插入测试电影数据
INSERT INTO `movie` (`title`, `poster`, `director`, `actors`, `genre`, `region`, `release_date`, `duration`, `description`, `avg_rating`, `rating_count`) VALUES
('流浪地球2', '/images/liulangdiqiu2.jpg', '郭帆', '吴京,刘德华,李雪健,沙溢,宁理', '科幻', '中国', '2023-01-22', 173, '太阳即将毁灭，人类在地球表面建造出巨大的推进器，寻找新的家园。然而宇宙之路危机四伏，为了拯救地球，流浪地球时代的年轻人再次挺身而出，展开争分夺秒的生死之战。', 4.5, 128),
('满江红', '/images/manjianghong.jpg', '张艺谋', '沈腾,易烊千玺,张译,雷佳音,岳云鹏', '喜剧', '中国', '2023-01-22', 159, '南宋绍兴年间，岳飞死后四年，秦桧率兵与金国会谈。会谈前夜，金国使者死在宰相驻地，所携密信也不翼而飞。', 4.2, 96),
('阿凡达：水之道', '/images/afanda2.jpg', '詹姆斯·卡梅隆', '萨姆·沃辛顿,佐伊·索尔达娜,西格妮·韦弗', '科幻', '美国', '2022-12-16', 192, '杰克·萨利和奈蒂莉组建了家庭，他们的孩子也逐渐成长。然而危机未曾消散，萨利一家拼尽全力彼此守护。', 4.0, 85),
('铃芽之旅', '/images/lingya.jpg', '新海诚', '原菜乃华,松村北斗,深津绘里', '动画', '日本', '2023-03-24', 122, '少女铃芽与神秘青年草太相遇，为了关闭灾难之门，两人踏上了穿越日本的旅程。', 4.3, 112),
('奥本海默', '/images/aobenhaimo.jpg', '克里斯托弗·诺兰', '基里安·墨菲,艾米莉·布朗特,马特·达蒙,小罗伯特·唐尼', '传记', '美国', '2023-08-30', 180, '影片讲述了美国原子弹之父罗伯特·奥本海默的故事。', 4.6, 156),
('长安三万里', '/images/changansanwanli.jpg', '谢君伟,邹靖', '杨天翔,凌振赫,吴俊全', '动画', '中国', '2023-07-08', 168, '安史之乱后，整个长安因战争而陷入混乱。身陷囹圄的高适回忆起自己与李白的往事。', 4.4, 134),
('消失的她', '/images/xiaoshideta.jpg', '崔睿,刘翔', '朱一龙,倪妮,文咏珊,杜江', '悬疑', '中国', '2023-06-22', 122, '何非的妻子李木子在结婚周年旅行中离奇消失，在何非苦苦寻找之际，一个陌生女人突然出现。', 4.1, 98),
('你好，李焕英', '/images/nihaolhy.jpg', '贾玲', '贾玲,张小斐,沈腾,陈赫', '喜剧', '中国', '2021-02-12', 128, '2001年的某一天，刚刚考上大学的贾晓玲经历了人生中的一次大起大落。', 4.5, 189),
('寄生虫', '/images/jishengchong.jpg', '奉俊昊', '宋康昊,李善均,赵汝贞,崔宇植', '剧情', '韩国', '2019-05-30', 132, '基宇一家四口全是无业游民，住在狭窄的半地下室里。一天，基宇的同学上门拜访，给他介绍了一份去富人朴社长家做家教的工作。', 4.7, 210),
('盗梦空间', '/images/daomengkongjian.jpg', '克里斯托弗·诺兰', '莱昂纳多·迪卡普里奥,约瑟夫·高登-莱维特,艾伦·佩吉', '科幻', '美国', '2010-09-01', 148, '道姆·柯布是一位经验老道的窃贼，他在这一行中算得上是最厉害的，因为他能够潜入人们精神最为脆弱的梦境中，窃取潜意识中有价值的秘密。', 4.8, 256);

-- 插入测试评论数据
INSERT INTO `review` (`user_id`, `movie_id`, `rating`, `content`) VALUES
(1, 1, 5, '太震撼了！特效一流，剧情感人，国产科幻的巅峰之作！'),
(2, 1, 4, '比第一部更加宏大，刘德华的表演很出彩。'),
(3, 1, 5, '看哭了，中国科幻电影的骄傲！'),
(1, 2, 4, '张艺谋的群戏调度太厉害了，沈腾演技在线。'),
(2, 2, 4, '悬疑感十足，结局很感人。'),
(1, 3, 4, '视觉效果无敌，水下世界太美了。'),
(3, 3, 4, '卡梅隆还是那个卡梅隆，技术狂人。'),
(2, 4, 5, '新海诚的画面永远那么美，故事也很治愈。'),
(1, 5, 5, '诺兰神作，基里安·墨菲的表演太棒了！'),
(2, 5, 5, '三个小时完全不觉得长，历史与人性的深度探讨。'),
(3, 5, 4, '很有深度的传记片，值得二刷。'),
(1, 6, 5, '诗词与动画的完美结合，看完想背诗了。'),
(2, 6, 4, '画面精美，李白和高适的友情很感人。'),
(1, 7, 4, '反转很精彩，朱一龙演技炸裂。'),
(3, 7, 4, '悬疑片的佳作，节奏把控得很好。'),
(2, 8, 5, '笑着笑着就哭了，母爱太伟大了。'),
(3, 8, 5, '贾玲的诚意之作，张小斐演得太好了。'),
(1, 9, 5, '奉俊昊的神作，社会讽刺太到位了。'),
(2, 9, 5, '每一个镜头都有深意，值得反复观看。'),
(1, 10, 5, '诺兰的想象力太惊人了，梦境套梦境的设计绝了。'),
(2, 10, 5, '经典中的经典，每次看都有新发现。'),
(3, 10, 5, '烧脑神作，结局的陀螺到底倒没倒？');

-- 评论点赞表
DROP TABLE IF EXISTS `review_like`;
CREATE TABLE `review_like` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '点赞ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `review_id` BIGINT NOT NULL COMMENT '评论ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_user_review` (`user_id`, `review_id`),
    KEY `idx_review_id` (`review_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论点赞表';

-- 插入测试点赞数据
INSERT INTO `review_like` (`user_id`, `review_id`) VALUES
(2, 1), (3, 1), (1, 2), (3, 2),
(1, 3), (2, 3), (2, 4), (3, 5),
(1, 9), (2, 9), (3, 9), (1, 10),
(2, 10), (3, 10), (1, 11), (2, 11),
(1, 12), (2, 12), (3, 12), (1, 17),
(2, 17), (3, 17), (1, 18), (2, 18),
(3, 18), (1, 19), (2, 19), (3, 19),
(1, 20), (2, 20), (3, 20), (1, 21),
(2, 21), (3, 21), (1, 22), (2, 22);

-- 更新电影评分统计
UPDATE movie m SET 
    avg_rating = (SELECT ROUND(AVG(rating), 1) FROM review WHERE movie_id = m.id),
    rating_count = (SELECT COUNT(*) FROM review WHERE movie_id = m.id)
WHERE EXISTS (SELECT 1 FROM review WHERE movie_id = m.id);
