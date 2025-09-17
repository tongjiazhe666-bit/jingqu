# 景区应用后端API接口文档

## 技术栈
- Spring Boot 3.5.5
- MyBatis Plus
- MySQL 8.0.43
- 数据库名：jingqu
- 端口：8080
- SDK：21.0.8
## 数据库表设计

### 1. 用户表 (user)
```sql
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `openid` varchar(100) NOT NULL COMMENT '微信openid',
  `nick_name` varchar(100) DEFAULT NULL COMMENT '昵称',
  `avatar_url` varchar(500) DEFAULT NULL COMMENT '头像URL',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_openid` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

### 2. 轮播图表 (banner)
```sql
CREATE TABLE `banner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `image` varchar(500) NOT NULL COMMENT '图片URL',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮播图表';
```

### 3. 景区表 (scenic_spot)
```sql
CREATE TABLE `scenic_spot` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(100) NOT NULL COMMENT '景区名称',
  `image` varchar(500) NOT NULL COMMENT '图片URL',
  `tags` varchar(200) DEFAULT NULL COMMENT '标签，逗号分隔',
  `is_recommended` tinyint(1) DEFAULT '0' COMMENT '是否推荐：0-否，1-是',
  `introduction` text COMMENT '景区介绍',
  `open_time` varchar(200) DEFAULT NULL COMMENT '开放时间',
  `address` varchar(500) DEFAULT NULL COMMENT '地址',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '纬度',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '经度',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景区表';
```

### 4. 游玩项目表 (project)
```sql
CREATE TABLE `project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `scenic_spot_id` bigint(20) NOT NULL COMMENT '景区ID',
  `title` varchar(100) NOT NULL COMMENT '项目名称',
  `image` varchar(500) NOT NULL COMMENT '图片URL',
  `tag` varchar(50) DEFAULT NULL COMMENT '标签',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_scenic_spot_id` (`scenic_spot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游玩项目表';
```

### 5. 用户收藏表 (user_like)
```sql
CREATE TABLE `user_like` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `scenic_spot_id` bigint(20) NOT NULL COMMENT '景区ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_scenic` (`user_id`, `scenic_spot_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_scenic_spot_id` (`scenic_spot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表';
```

## API接口列表

### 1. 登录接口
**接口路径**: `/api/login`  
**请求方式**: POST  
**请求参数**:
```json
{
  "code": "微信登录code"
}
```

**响应数据**:
```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "token": "jwt_token_string",
    "userInfo": {
      "id": 1,
      "nickName": "用户昵称",
      "avatarUrl": "头像URL"
    }
  }
}
```

### 2. 获取用户信息
**接口路径**: `/api/getUserInfo`  
**请求方式**: GET  
**请求头**: `token: jwt_token_string`

**响应数据**:
```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "id": 1,
    "nickName": "用户昵称",
    "avatarUrl": "头像URL"
  }
}
```

### 3. 获取轮播图
**接口路径**: `/api/user/getBanner`  
**请求方式**: GET  

**响应数据**:
```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "bannerList": [
      {
        "image": "https://example.com/banner1.jpg",
        "title": "轮播图标题1"
      },
      {
        "image": "https://example.com/banner2.jpg",
        "title": "轮播图标题2"
      }
    ]
  }
}
```

### 4. 获取首页景区列表
**接口路径**: `/api/user/getHomeList`  
**请求方式**: GET  

**响应数据**:
```json
{
  "code": 1,
  "msg": "success",
  "data": [
    {
      "id": 1,
      "title": "天坛公园",
      "img": "/static/tt.jpg",
      "tag": ["著名", "名胜古迹"],
      "isDot": "推荐",
      "dot": true,
      "introduce": "天坛公园介绍...",
      "times": "每周一到周五9:00 -- 18:00开放",
      "isPlay": false,
      "address": ["116.410886", "39.881949"]
    }
  ]
}
```

### 5. 获取景区详情游玩项目
**接口路径**: `/api/detail/project`  
**请求方式**: GET  

**响应数据**:
```json
{
  "code": 1,
  "msg": "success",
  "data": [
    {
      "id": 1,
      "belong":1,
      "title": "游玩项目1",
      "url": "项目图片URL",
      "tag": "推荐",
      "desc": "项目描述"
    }
  ]
}
```

### 6. 获取项目详情信息
**接口路径**: `/api/project/info`  
**请求方式**: POST  
**请求参数**:
```json
{
  "id": 1
}
```

**响应数据**:
```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "id": 1,
    "title": "项目详情标题",
    "content": "项目详细内容...",
    "similiar":[]
  }
}
```

### 7. 获取用户收藏列表
**接口路径**: `/api/like/list`  
**请求方式**: GET  
**请求头**: `token: jwt_token_string`

**响应数据**:
```json
{
  "code": 1,
  "msg": "success",
  "data": [
    {
      "id": 1,
      "title": "天坛公园",
      "img": "/static/tt.jpg",
      "introduce": "天坛公园介绍...",
      "isDot": "推荐"
    }
  ]
}
```

### 8. 添加/取消收藏
**接口路径**: `/api/like/toggle`  
**请求方式**: POST  
**请求头**: `token: jwt_token_string`  
**请求参数**:
```json
{
  "scenic_spot_id": 1
}
```

**响应数据**:
```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "liked": true
  }
}
```

### 9. 检查收藏状态
**接口路径**: `/api/like/status`  
**请求方式**: POST  
**请求头**: `token: jwt_token_string`  
**请求参数**:
```json
{
  "scenic_spot_id": 1
}
```

**响应数据**:
```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "liked": true
  }
}
```

## 统一响应格式
所有接口都遵循以下响应格式：
```json
{
  "code": 1,
  "msg": "success",
  "data": {}
}
```

## 错误码说明
- 1: 成功
- 0: 失败
- 401: 未授权/Token无效
- 403: 无权限访问
- 404: 资源不存在
- 500: 服务器内部错误

## 收藏相关业务逻辑
1. 用户需要先登录才能使用收藏功能
2. 每个用户对同一个景区只能收藏一次（通过唯一约束保证）
3. 收藏列表接口返回用户收藏的所有景区信息
4. 收藏状态接口用于检查用户是否已收藏某个景区
5. 收藏切换接口用于添加或取消收藏

## 部署说明
1. 确保MySQL 5.7已安装并创建数据库`jingqu`
2. 应用配置文件`application.yml`中配置数据库连接
3. 启动端口设置为8080
4. 前端请求地址配置为`http://localhost:8080/api`

```

这个文档包含了您前端应用所需的所有API接口，包括新增的收藏功能接口，数据库表结构设计，以及详细的接口说明。您可以根据这个文档来开发完整的Spring Boot后端服务。
