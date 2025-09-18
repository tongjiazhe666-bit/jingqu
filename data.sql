-- 景区应用数据库初始化脚本
-- 数据库名：jingqu
-- 创建用户表
CREATE TABLE IF NOT EXISTS `user` (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `openid` varchar(100) NOT NULL COMMENT '微信openid',
    `nick_name` varchar(100) DEFAULT NULL COMMENT '昵称',
    `avatar_url` varchar(500) DEFAULT NULL COMMENT '头像URL',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_openid` (`openid`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '用户表';

-- 创建轮播图表
CREATE TABLE IF NOT EXISTS `banner` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `image` varchar(500) NOT NULL COMMENT '图片URL',
    `title` varchar(200) NOT NULL COMMENT '标题',
    `sort` int(11) DEFAULT '0' COMMENT '排序',
    `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '轮播图表';

-- 创建景区表
CREATE TABLE IF NOT EXISTS `scenic_spot` (
                                             `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `title` varchar(100) NOT NULL COMMENT '景区名称',
    `image` varchar(500) NOT NULL COMMENT '图片URL',
    `tags` varchar(200) DEFAULT NULL COMMENT '标签，逗号分隔',
    `is_recommended` tinyint(1) DEFAULT '0' COMMENT '是否推荐：0-否，1-是',
    `introduction` text COMMENT '景区介绍',
    `open_time` varchar(200) DEFAULT NULL COMMENT '开放时间',
    `address` varchar(500) DEFAULT NULL COMMENT '地址',
    `latitude` decimal(10, 6) DEFAULT NULL COMMENT '纬度',
    `longitude` decimal(10, 6) DEFAULT NULL COMMENT '经度',
    `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '景区表';

-- 创建游玩项目表
CREATE TABLE IF NOT EXISTS `project` (
                                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `scenic_spot_id` bigint(20) NOT NULL COMMENT '景区ID',
    `title` varchar(100) NOT NULL COMMENT '项目名称',
    `image` varchar(500) NOT NULL COMMENT '图片URL',
    `tag` varchar(50) DEFAULT NULL COMMENT '标签',
    `description` varchar(500) DEFAULT NULL COMMENT '描述',
    `longitude` DECIMAL(9,6) DEFAULT NULL COMMENT '经度',
    `latitude` DECIMAL(9,6) DEFAULT NULL COMMENT '纬度',
    `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_scenic_spot_id` (`scenic_spot_id`),
    KEY `idx_location` (`longitude`, `latitude`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '游玩项目表';


-- 创建收藏关系表 (favorites)
CREATE TABLE `favorites` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `scenic_spot_id` bigint(20) NOT NULL COMMENT '景区ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_scenic_spot` (`user_id`, `scenic_spot_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_scenic_spot_id` (`scenic_spot_id`),
  CONSTRAINT `fk_favorites_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_favorites_scenic_spot` FOREIGN KEY (`scenic_spot_id`) REFERENCES `scenic_spot` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏关系表';

INSERT INTO `favorites` (`user_id`, `scenic_spot_id`, `create_time`) VALUES
(3, 4, '2024-01-15 10:30:00'),
(3, 5, '2024-01-15 10:30:00'),
(3, 6, '2024-01-15 10:30:00'),
(3, 7, '2024-01-15 10:30:00'),
(3, 8, '2024-01-15 10:30:00'),
(3, 9, '2024-01-15 10:30:00'),
(3, 10, '2024-01-15 10:30:00'),
(3, 11, '2024-01-15 10:30:00'),
(3, 12, '2024-01-15 10:30:00'),
(3, 13, '2024-01-15 10:30:00'),
(3, 14, '2024-01-15 10:30:00');