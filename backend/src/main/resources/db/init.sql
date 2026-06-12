-- =============================================
-- 东软环保公众监督系统 - 数据库初始化脚本
-- 数据库名: environment | 16张表
-- =============================================

CREATE DATABASE IF NOT EXISTS environment DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE environment;

-- =============================================
-- 1. 用户表
-- =============================================
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号（监督员唯一标识）',
    `login_code` VARCHAR(50) DEFAULT NULL COMMENT '登录编码（网格员/管理员/决策者用）',
    `password` VARCHAR(255) NOT NULL COMMENT '加密密码',
    `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `age` INT DEFAULT NULL COMMENT '年龄',
    `gender` TINYINT DEFAULT 0 COMMENT '性别 0女 1男',
    `role` TINYINT NOT NULL COMMENT '角色 0公众监督员 1网格员 2管理员 3决策者',
    `grid_province` VARCHAR(50) DEFAULT NULL COMMENT '所属省（网格员用）',
    `grid_city` VARCHAR(50) DEFAULT NULL COMMENT '所属市（网格员用）',
    `work_status` TINYINT DEFAULT 0 COMMENT '工作状态 0空闲 1忙碌（网格员用）',
    `account_status` TINYINT DEFAULT 1 COMMENT '账号状态 0禁用 1正常',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最近登录时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_login_code` (`login_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- =============================================
-- 2. 网格区域表
-- =============================================
DROP TABLE IF EXISTS `grid_region`;
CREATE TABLE `grid_region` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `province` VARCHAR(50) NOT NULL COMMENT '省',
    `city` VARCHAR(50) NOT NULL COMMENT '市',
    `city_type` VARCHAR(50) DEFAULT NULL COMMENT '城市分类',
    `is_capital` TINYINT DEFAULT 0 COMMENT '是否省会 0否 1是',
    PRIMARY KEY (`id`),
    KEY `idx_province` (`province`),
    KEY `idx_city` (`city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网格区域表';

-- =============================================
-- 3. 公众监督反馈表
-- =============================================
DROP TABLE IF EXISTS `supervision_feedback`;
CREATE TABLE `supervision_feedback` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `supervisor_id` BIGINT NOT NULL COMMENT '监督员ID',
    `province` VARCHAR(50) NOT NULL COMMENT '省',
    `city` VARCHAR(50) NOT NULL COMMENT '市',
    `address_detail` VARCHAR(255) DEFAULT NULL COMMENT '具体地址',
    `estimated_aqi_level` INT NOT NULL COMMENT '预估AQI等级(1-6)',
    `description` TEXT COMMENT '空气质量描述信息',
    `status` TINYINT DEFAULT 0 COMMENT '状态 0待指派 1已指派 2已完成',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '反馈时间',
    PRIMARY KEY (`id`),
    KEY `idx_supervisor_id` (`supervisor_id`),
    KEY `idx_status` (`status`),
    KEY `idx_province_city` (`province`, `city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公众监督反馈表';

-- =============================================
-- 4. 任务指派表
-- =============================================
DROP TABLE IF EXISTS `task_assignment`;
CREATE TABLE `task_assignment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `feedback_id` BIGINT NOT NULL COMMENT '反馈ID',
    `grid_man_id` BIGINT NOT NULL COMMENT '网格员ID',
    `assign_type` TINYINT DEFAULT 0 COMMENT '指派类型 0本地指派 1异地指派',
    `assign_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '指派时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_feedback_id` (`feedback_id`),
    KEY `idx_grid_man_id` (`grid_man_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务指派表';

-- =============================================
-- 5. AQI实测数据表
-- =============================================
DROP TABLE IF EXISTS `aqi_data`;
CREATE TABLE `aqi_data` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `feedback_id` BIGINT NOT NULL COMMENT '反馈ID',
    `grid_man_id` BIGINT NOT NULL COMMENT '网格员ID',
    `so2_aqi` DECIMAL(5,2) NOT NULL COMMENT 'SO2 AQI浓度值',
    `co_aqi` DECIMAL(5,2) NOT NULL COMMENT 'CO AQI浓度值',
    `pm25_aqi` DECIMAL(5,2) NOT NULL COMMENT 'PM2.5 AQI浓度值',
    `final_aqi` DECIMAL(5,2) NOT NULL COMMENT '最终AQI = MAX(SO2, CO, PM2.5)',
    `is_exceeded` TINYINT DEFAULT 0 COMMENT '是否超标 0否 1是',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    PRIMARY KEY (`id`),
    KEY `idx_feedback_id` (`feedback_id`),
    KEY `idx_grid_man_id` (`grid_man_id`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_is_exceeded` (`is_exceeded`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AQI实测数据表';

-- =============================================
-- 6. AQI标准参考表
-- =============================================
DROP TABLE IF EXISTS `aqi_standard`;
CREATE TABLE `aqi_standard` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `aqi_level` INT NOT NULL COMMENT 'AQI等级(1-6)',
    `level_name` VARCHAR(50) NOT NULL COMMENT '等级名称',
    `aqi_min` INT NOT NULL COMMENT 'AQI范围下限',
    `aqi_max` INT NOT NULL COMMENT 'AQI范围上限',
    `pm25_limit` DECIMAL(10,2) DEFAULT NULL COMMENT 'PM2.5浓度限值(μg/m³)',
    `so2_limit` DECIMAL(10,2) DEFAULT NULL COMMENT 'SO2浓度限值(μg/m³)',
    `co_limit` DECIMAL(10,2) DEFAULT NULL COMMENT 'CO浓度限值(mg/m³)',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AQI标准参考表';

-- =============================================
-- 7. 网格覆盖率统计表
-- =============================================
DROP TABLE IF EXISTS `grid_coverage`;
CREATE TABLE `grid_coverage` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `stat_date` DATE NOT NULL COMMENT '统计日期',
    `total_provinces` INT DEFAULT 34 COMMENT '全国省份总数',
    `covered_provinces` INT DEFAULT 0 COMMENT '已覆盖省份数',
    `total_cities` INT DEFAULT 106 COMMENT '106个大城市总数',
    `covered_cities` INT DEFAULT 0 COMMENT '已覆盖大城市数',
    `province_rate` DECIMAL(5,2) DEFAULT 0 COMMENT '省覆盖率%',
    `city_rate` DECIMAL(5,2) DEFAULT 0 COMMENT '大城市覆盖率%',
    PRIMARY KEY (`id`),
    KEY `idx_stat_date` (`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网格覆盖率统计表';

-- =============================================
-- 8. AQI统计表
-- =============================================
DROP TABLE IF EXISTS `aqi_statistics`;
CREATE TABLE `aqi_statistics` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `stat_month` VARCHAR(7) NOT NULL COMMENT '统计月份 YYYY-MM',
    `province` VARCHAR(50) DEFAULT NULL COMMENT '省',
    `aqi_level` INT DEFAULT NULL COMMENT 'AQI等级',
    `so2_exceeded_count` INT DEFAULT 0 COMMENT 'SO2超标累计数',
    `co_exceeded_count` INT DEFAULT 0 COMMENT 'CO超标累计数',
    `pm25_exceeded_count` INT DEFAULT 0 COMMENT 'PM2.5超标累计数',
    `aqi_exceeded_count` INT DEFAULT 0 COMMENT 'AQI超标累计数',
    `total_detect_count` INT DEFAULT 0 COMMENT '检测总数',
    `good_detect_count` INT DEFAULT 0 COMMENT '检测良好数',
    `bad_detect_count` INT DEFAULT 0 COMMENT '检测超标数',
    PRIMARY KEY (`id`),
    KEY `idx_stat_month` (`stat_month`),
    KEY `idx_province` (`province`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AQI统计表';

-- =============================================
-- 9. 操作日志表
-- =============================================
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '操作用户ID',
    `user_name` VARCHAR(50) DEFAULT NULL COMMENT '操作用户名',
    `role` TINYINT DEFAULT NULL COMMENT '角色',
    `module` VARCHAR(50) DEFAULT NULL COMMENT '操作模块',
    `operation_type` VARCHAR(50) NOT NULL COMMENT '操作类型 LOGIN/INSERT/UPDATE/DELETE/ASSIGN等',
    `operation_desc` VARCHAR(500) DEFAULT NULL COMMENT '操作描述',
    `target_table` VARCHAR(50) DEFAULT NULL COMMENT '目标数据表',
    `target_id` BIGINT DEFAULT NULL COMMENT '目标数据ID',
    `ip_address` VARCHAR(50) DEFAULT NULL COMMENT '操作IP地址',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_operation_type` (`operation_type`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- =============================================
-- 10. 系统公告表
-- =============================================
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `title` VARCHAR(200) NOT NULL COMMENT '公告标题',
    `content` TEXT NOT NULL COMMENT '公告内容',
    `publisher_id` BIGINT NOT NULL COMMENT '发布人ID',
    `publisher_name` VARCHAR(50) DEFAULT NULL COMMENT '发布人姓名',
    `is_top` TINYINT DEFAULT 0 COMMENT '是否置顶 0否 1是',
    `status` TINYINT DEFAULT 1 COMMENT '状态 0草稿 1已发布 2已撤回',
    `publish_time` DATETIME DEFAULT NULL COMMENT '发布时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_status` (`status`),
    KEY `idx_is_top` (`is_top`),
    KEY `idx_publish_time` (`publish_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统公告表';

-- =============================================
-- 11. 反馈图片表
-- =============================================
DROP TABLE IF EXISTS `feedback_image`;
CREATE TABLE `feedback_image` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `feedback_id` BIGINT NOT NULL COMMENT '反馈ID',
    `image_url` VARCHAR(500) NOT NULL COMMENT '图片URL',
    `thumbnail_url` VARCHAR(500) DEFAULT NULL COMMENT '缩略图URL',
    `sort_order` INT DEFAULT 0 COMMENT '排序号',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    PRIMARY KEY (`id`),
    KEY `idx_feedback_id` (`feedback_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='反馈图片表';

-- =============================================
-- 12. 消息通知表
-- =============================================
DROP TABLE IF EXISTS `message_notification`;
CREATE TABLE `message_notification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '接收用户ID',
    `title` VARCHAR(200) NOT NULL COMMENT '通知标题',
    `content` TEXT DEFAULT NULL COMMENT '通知内容',
    `type` TINYINT DEFAULT 0 COMMENT '通知类型 0系统通知 1任务指派 2任务完成 3公告通知',
    `is_read` TINYINT DEFAULT 0 COMMENT '是否已读 0未读 1已读',
    `related_id` BIGINT DEFAULT NULL COMMENT '关联业务ID',
    `read_time` DATETIME DEFAULT NULL COMMENT '阅读时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id_read` (`user_id`, `is_read`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息通知表';

-- =============================================
-- 13. 系统字典表
-- =============================================
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `dict_code` VARCHAR(50) NOT NULL COMMENT '字典编码',
    `dict_name` VARCHAR(100) DEFAULT NULL COMMENT '字典名称',
    `dict_value` VARCHAR(50) NOT NULL COMMENT '字典值',
    `dict_label` VARCHAR(100) NOT NULL COMMENT '字典标签',
    `sort_order` INT DEFAULT 0 COMMENT '排序号',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_dict_code` (`dict_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统字典表';

-- =============================================
-- 14. 公众投诉表
-- =============================================
DROP TABLE IF EXISTS `complaint`;
CREATE TABLE `complaint` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '投诉人ID',
    `title` VARCHAR(200) NOT NULL COMMENT '投诉标题',
    `complaint_type` TINYINT DEFAULT 0 COMMENT '投诉类型 0水体污染 1大气污染 2噪音污染 3固废污染 4其他',
    `province` VARCHAR(50) NOT NULL COMMENT '省',
    `city` VARCHAR(50) NOT NULL COMMENT '市',
    `address_detail` VARCHAR(255) DEFAULT NULL COMMENT '详细地址',
    `description` TEXT NOT NULL COMMENT '投诉描述',
    `status` TINYINT DEFAULT 0 COMMENT '状态 0待处理 1处理中 2已处理 3已驳回',
    `handler_id` BIGINT DEFAULT NULL COMMENT '处理人ID',
    `handle_result` TEXT DEFAULT NULL COMMENT '处理结果',
    `handle_time` DATETIME DEFAULT NULL COMMENT '处理时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '投诉时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_province_city` (`province`, `city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公众投诉表';

-- =============================================
-- 15. 环保政策表
-- =============================================
DROP TABLE IF EXISTS `environmental_policy`;
CREATE TABLE `environmental_policy` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `title` VARCHAR(200) NOT NULL COMMENT '政策标题',
    `content` TEXT NOT NULL COMMENT '政策内容',
    `policy_type` TINYINT DEFAULT 0 COMMENT '政策类型 0国家法规 1地方条例 2行业标准 3环保科普',
    `publish_date` DATE DEFAULT NULL COMMENT '发布日期',
    `source` VARCHAR(200) DEFAULT NULL COMMENT '来源',
    `publisher_id` BIGINT DEFAULT NULL COMMENT '发布人ID',
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `status` TINYINT DEFAULT 1 COMMENT '状态 0草稿 1已发布',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_policy_type` (`policy_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='环保政策表';

-- =============================================
-- 16. 网格巡检记录表
-- =============================================
DROP TABLE IF EXISTS `grid_inspection_record`;
CREATE TABLE `grid_inspection_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `grid_man_id` BIGINT NOT NULL COMMENT '网格员ID',
    `province` VARCHAR(50) NOT NULL COMMENT '省',
    `city` VARCHAR(50) NOT NULL COMMENT '市',
    `address_detail` VARCHAR(255) DEFAULT NULL COMMENT '具体巡检地址',
    `inspection_date` DATE NOT NULL COMMENT '巡检日期',
    `weather` VARCHAR(50) DEFAULT NULL COMMENT '天气情况',
    `so2_value` DECIMAL(5,2) DEFAULT NULL COMMENT 'SO2实测值',
    `co_value` DECIMAL(5,2) DEFAULT NULL COMMENT 'CO实测值',
    `pm25_value` DECIMAL(5,2) DEFAULT NULL COMMENT 'PM2.5实测值',
    `aqi_level` INT DEFAULT NULL COMMENT 'AQI等级',
    `description` TEXT DEFAULT NULL COMMENT '巡检描述',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_grid_man_id` (`grid_man_id`),
    KEY `idx_inspection_date` (`inspection_date`),
    KEY `idx_province_city` (`province`, `city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网格巡检记录表';

-- =============================================
-- 插入 AQI 标准参考数据
-- =============================================
INSERT INTO `aqi_standard` (`aqi_level`, `level_name`, `aqi_min`, `aqi_max`, `pm25_limit`, `so2_limit`, `co_limit`) VALUES
(1, '优',   0,  50,  35.00,  50.00,  2.00),
(2, '良',  51, 100,  75.00, 150.00,  4.00),
(3, '轻度污染', 101, 150, 115.00, 475.00, 14.00),
(4, '中度污染', 151, 200, 150.00, 800.00, 24.00),
(5, '重度污染', 201, 300, 250.00, 1600.00, 36.00),
(6, '严重污染', 301, 500, 500.00, 2625.00, 60.00);

-- =============================================
-- 插入网格区域数据（省会城市 + 大城市）
-- =============================================
INSERT INTO `grid_region` (`province`, `city`, `city_type`, `is_capital`) VALUES
('北京', '北京', '超大城市', 1),
('天津', '天津', '超大城市', 1),
('上海', '上海', '超大城市', 1),
('重庆', '重庆', '超大城市', 1),
('河北', '石家庄', 'I型大城市', 1),
('河北', '唐山', 'II型大城市', 0),
('河北', '邯郸', 'II型大城市', 0),
('河北', '保定', 'II型大城市', 0),
('山西', '太原', 'I型大城市', 1),
('内蒙古', '呼和浩特', 'II型大城市', 1),
('辽宁', '沈阳', '特大城市', 1),
('辽宁', '大连', '特大城市', 0),
('吉林', '长春', '特大城市', 1),
('黑龙江', '哈尔滨', '特大城市', 1),
('江苏', '南京', '特大城市', 1),
('江苏', '苏州', '特大城市', 0),
('江苏', '无锡', 'II型大城市', 0),
('江苏', '常州', 'II型大城市', 0),
('浙江', '杭州', '特大城市', 1),
('浙江', '宁波', 'I型大城市', 0),
('浙江', '温州', 'II型大城市', 0),
('安徽', '合肥', 'I型大城市', 1),
('福建', '福州', 'I型大城市', 1),
('福建', '厦门', 'II型大城市', 0),
('福建', '泉州', 'II型大城市', 0),
('江西', '南昌', 'I型大城市', 1),
('山东', '济南', '特大城市', 1),
('山东', '青岛', '特大城市', 0),
('山东', '淄博', 'II型大城市', 0),
('山东', '烟台', 'II型大城市', 0),
('河南', '郑州', '特大城市', 1),
('河南', '洛阳', 'II型大城市', 0),
('湖北', '武汉', '超大城市', 1),
('湖南', '长沙', '特大城市', 1),
('广东', '广州', '超大城市', 1),
('广东', '深圳', '超大城市', 1),
('广东', '东莞', '特大城市', 0),
('广东', '佛山', '特大城市', 0),
('广西', '南宁', 'I型大城市', 1),
('海南', '海口', 'II型大城市', 1),
('四川', '成都', '超大城市', 1),
('贵州', '贵阳', 'II型大城市', 1),
('云南', '昆明', '特大城市', 1),
('西藏', '拉萨', 'II型大城市', 1),
('陕西', '西安', '特大城市', 1),
('甘肃', '兰州', 'II型大城市', 1),
('青海', '西宁', 'II型大城市', 1),
('宁夏', '银川', 'II型大城市', 1),
('新疆', '乌鲁木齐', 'II型大城市', 1);

-- =============================================
-- 插入系统字典数据
-- =============================================
INSERT INTO `sys_dict` (`dict_code`, `dict_name`, `dict_value`, `dict_label`, `sort_order`, `remark`) VALUES
('complaint_type', '投诉类型', '0', '水体污染', 1, NULL),
('complaint_type', '投诉类型', '1', '大气污染', 2, NULL),
('complaint_type', '投诉类型', '2', '噪音污染', 3, NULL),
('complaint_type', '投诉类型', '3', '固废污染', 4, NULL),
('complaint_type', '投诉类型', '4', '其他', 5, NULL),
('policy_type', '政策类型', '0', '国家法规', 1, NULL),
('policy_type', '政策类型', '1', '地方条例', 2, NULL),
('policy_type', '政策类型', '2', '行业标准', 3, NULL),
('policy_type', '政策类型', '3', '环保科普', 4, NULL),
('notification_type', '通知类型', '0', '系统通知', 1, NULL),
('notification_type', '通知类型', '1', '任务指派', 2, NULL),
('notification_type', '通知类型', '2', '任务完成', 3, NULL),
('notification_type', '通知类型', '3', '公告通知', 4, NULL),
('complaint_status', '投诉状态', '0', '待处理', 1, NULL),
('complaint_status', '投诉状态', '1', '处理中', 2, NULL),
('complaint_status', '投诉状态', '2', '已处理', 3, NULL),
('complaint_status', '投诉状态', '3', '已驳回', 4, NULL);

-- =============================================
-- 预置测试账号（密码均为 123456 的 BCrypt 加密值）
-- =============================================
INSERT INTO `sys_user` (`phone`, `login_code`, `password`, `real_name`, `age`, `gender`, `role`, `grid_province`, `grid_city`, `work_status`, `account_status`) VALUES
-- 公众监督员 (role=0)
('13800000001', NULL, '$2b$10$ilL8XXYR2A.V7sXyZ/osweS9uD3JlDrzrQ0rN11mhUaAZ/v8XogyO', '张三', 28, 1, 0, NULL, NULL, NULL, 1),
('13800000002', NULL, '$2b$10$ilL8XXYR2A.V7sXyZ/osweS9uD3JlDrzrQ0rN11mhUaAZ/v8XogyO', '李四', 35, 0, 0, NULL, NULL, NULL, 1),
-- 网格员 (role=1)
(NULL, 'GRID001', '$2b$10$ilL8XXYR2A.V7sXyZ/osweS9uD3JlDrzrQ0rN11mhUaAZ/v8XogyO', '王五', 30, 1, 1, '辽宁', '沈阳', 0, 1),
(NULL, 'GRID002', '$2b$10$ilL8XXYR2A.V7sXyZ/osweS9uD3JlDrzrQ0rN11mhUaAZ/v8XogyO', '赵六', 27, 0, 1, '辽宁', '大连', 0, 1),
(NULL, 'GRID003', '$2b$10$ilL8XXYR2A.V7sXyZ/osweS9uD3JlDrzrQ0rN11mhUaAZ/v8XogyO', '孙七', 32, 1, 1, '北京', '北京', 0, 1),
-- 系统管理员 (role=2)
(NULL, 'ADMIN001', '$2b$10$ilL8XXYR2A.V7sXyZ/osweS9uD3JlDrzrQ0rN11mhUaAZ/v8XogyO', '管理员', 0, 0, 2, NULL, NULL, NULL, 1),
-- 决策者 (role=3)
(NULL, 'DECIS001', '$2b$10$ilL8XXYR2A.V7sXyZ/osweS9uD3JlDrzrQ0rN11mhUaAZ/v8XogyO', '决策者', 0, 0, 3, NULL, NULL, NULL, 1);

-- =============================================
-- 预置公告数据
-- =============================================
INSERT INTO `announcement` (`title`, `content`, `publisher_id`, `publisher_name`, `is_top`, `status`, `publish_time`) VALUES
('关于加强空气质量监督工作的通知', '各位公众监督员、网格员：为进一步加强环境保护监督工作，现要求各网格区域加大巡查力度，及时反馈空气质量异常情况。', 6, '管理员', 1, 1, NOW()),
('关于2025年第二季度空气质量报告发布', '2025年第二季度全国空气质量报告已发布，请相关人员关注并跟进本区域数据变化。', 6, '管理员', 0, 1, NOW()),
('系统升级维护通知', '本系统计划于本周六进行版本升级，届时将暂停服务，请提前做好工作安排。', 6, '管理员', 0, 1, NOW());

-- =============================================
-- 预置环保政策数据
-- =============================================
INSERT INTO `environmental_policy` (`title`, `content`, `policy_type`, `publish_date`, `source`, `publisher_id`, `view_count`, `status`) VALUES
('中华人民共和国大气污染防治法', '为保护和改善环境，防治大气污染，保障公众健康，推进生态文明建设，促进经济社会可持续发展，制定本法。', 0, '2018-10-26', '全国人大常委会', 6, 128, 1),
('环境空气质量标准（GB 3095-2012）', '本标准规定了环境空气功能区分类、标准分级、污染物项目、平均时间及浓度限值、监测方法、数据统计的有效性规定及实施与监督等内容。', 2, '2012-02-29', '环境保护部', 6, 96, 1),
('PM2.5污染防治知识科普', 'PM2.5是指环境空气中空气动力学当量直径小于等于2.5微米的颗粒物。它能较长时间悬浮于空气中，对空气质量和能见度有重要影响。', 3, '2024-03-15', '环保科普中心', 6, 86, 1);

-- =============================================
-- 预置消息通知数据
-- =============================================
INSERT INTO `message_notification` (`user_id`, `title`, `content`, `type`, `is_read`) VALUES
(3, '新任务指派通知', '您收到了一个新的空气质量检测任务，请及时查看并处理。', 1, 0),
(4, '新任务指派通知', '您收到了一个新的空气质量检测任务，请及时查看并处理。', 1, 1),
(1, '系统欢迎通知', '欢迎加入东软环保公众监督系统！请您积极参与空气质量监督工作。', 0, 1);
