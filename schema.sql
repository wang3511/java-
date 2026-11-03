-- 游戏陪玩订单与评价系统 数据库脚本
-- 在 MySQL 中执行: mysql -u root -p < schema.sql
-- 或者打开 MySQL Workbench / Navicat 执行本文件

DROP DATABASE IF EXISTS peiwan;
CREATE DATABASE peiwan DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE peiwan;

-- 用户表:玩家和陪玩共用,用 role 区分
CREATE TABLE user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    nickname VARCHAR(50),
    role VARCHAR(20) NOT NULL,          -- PLAYER(玩家) 或 PEIWAN(陪玩)
    game VARCHAR(50),                   -- 陪玩擅长的游戏(玩家可空)
    price DECIMAL(10,2) DEFAULT 0,      -- 陪玩每小时单价(玩家为0)
    balance DECIMAL(10,2) DEFAULT 0     -- 账户余额
);

-- 订单表
CREATE TABLE game_order (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(32) NOT NULL UNIQUE,  -- 订单号
    player_id INT NOT NULL,                -- 下单的玩家
    peiwan_id INT NOT NULL,                -- 接单的陪玩
    game VARCHAR(50) NOT NULL,             -- 游戏名称
    hours INT NOT NULL,                    -- 陪玩时长(小时)
    amount DECIMAL(10,2) NOT NULL,         -- 订单金额
    status VARCHAR(20) NOT NULL,           -- PENDING待接单 / TAKEN已接单 / FINISHED已完成 / EVALUATED已评价 / SETTLED已结算
    create_time DATETIME,
    take_time DATETIME,
    finish_time DATETIME,
    FOREIGN KEY (player_id) REFERENCES user(id),
    FOREIGN KEY (peiwan_id) REFERENCES user(id)
);

-- 评价表
CREATE TABLE evaluation (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL UNIQUE,    -- 一个订单只能评价一次
    player_id INT NOT NULL,
    peiwan_id INT NOT NULL,
    score INT NOT NULL,              -- 1~5 星
    content VARCHAR(500),
    create_time DATETIME,
    FOREIGN KEY (order_id) REFERENCES game_order(id)
);

-- 初始测试数据
-- 玩家(账号密码都是 player / 123)
INSERT INTO user(username, password, nickname, role, balance) VALUES
('player', '123', '小明', 'PLAYER', 1000.00);

-- 陪玩(账号密码: pw01/pw02/pw03,密码都是 123)
INSERT INTO user(username, password, nickname, role, game, price, balance) VALUES
('pw01', '123', '王者荣耀小飞', 'PEIWAN', '王者荣耀', 30.00, 0.00),
('pw02', '123', 'LOL大神阿狸', 'PEIWAN', '英雄联盟', 50.00, 0.00),
('pw03', '123', '和平精英雪儿', 'PEIWAN', '和平精英', 25.00, 0.00);
