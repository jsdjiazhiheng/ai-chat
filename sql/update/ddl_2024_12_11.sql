DROP TABLE IF EXISTS gpt_open_key;
CREATE TABLE gpt_open_key
(
    id          BIGINT NOT NULL COMMENT '主键',
    type        INT COMMENT '模型类型（1-文本，2-图像，3-视觉，4-语音）',
    value       VARCHAR(90) COMMENT '模型值',
    name        VARCHAR(90) COMMENT '模型名称',
    app_id      VARCHAR(255) COMMENT 'AppID',
    app_key     VARCHAR(255) COMMENT 'AppKey',
    app_secret  VARCHAR(255) COMMENT 'App密钥',
    sort        INT COMMENT '排序',
    tenant_id   VARCHAR(20) COMMENT '租户号',
    dept_id     BIGINT COMMENT '部门ID',
    user_id     BIGINT COMMENT '用户Id',
    version     INT DEFAULT 0 COMMENT '乐观锁',
    create_dept BIGINT COMMENT '创建部门',
    create_by   BIGINT COMMENT '创建人',
    create_time DATETIME COMMENT '创建时间',
    update_by   BIGINT COMMENT '更新人',
    update_time DATETIME COMMENT '更新时间',
    del_flag    INT DEFAULT 0 COMMENT '删除标识',
    PRIMARY KEY (id)
) COMMENT = '模型配置';



INSERT INTO `gpt_open_key` (`id`, `type`, `value`, `name`, `app_id`, `app_key`, `app_secret`, `sort`, `tenant_id`, `dept_id`, `user_id`, `version`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1867415972197642241, 1, 'KIMI', 'Kimi', NULL, NULL, 'token', 1, '000000', NULL, NULL, 0, 103, 1, '2024-12-13 11:47:17', 1, '2024-12-13 11:47:17', 0);
INSERT INTO `gpt_open_key` (`id`, `type`, `value`, `name`, `app_id`, `app_key`, `app_secret`, `sort`, `tenant_id`, `dept_id`, `user_id`, `version`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1867416166553300993, 1, 'BAIDU', '百度', NULL, NULL, 'token', 2, '000000', NULL, NULL, 0, 103, 1, '2024-12-13 11:48:04', 1, '2024-12-13 11:48:04', 0);
INSERT INTO `gpt_open_key` (`id`, `type`, `value`, `name`, `app_id`, `app_key`, `app_secret`, `sort`, `tenant_id`, `dept_id`, `user_id`, `version`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1867416331536248833, 1, 'ZHIPU', '智谱清言', NULL, NULL, 'token', 3, '000000', NULL, NULL, 0, 103, 1, '2024-12-13 11:48:43', 1, '2024-12-13 11:48:43', 0);
INSERT INTO `gpt_open_key` (`id`, `type`, `value`, `name`, `app_id`, `app_key`, `app_secret`, `sort`, `tenant_id`, `dept_id`, `user_id`, `version`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1867416451266850818, 1, 'DEEPSEEK', 'deepseek', NULL, NULL, 'token', 4, '000000', NULL, NULL, 0, 103, 1, '2024-12-13 11:49:12', 1, '2024-12-13 11:49:12', 0);
INSERT INTO `gpt_open_key` (`id`, `type`, `value`, `name`, `app_id`, `app_key`, `app_secret`, `sort`, `tenant_id`, `dept_id`, `user_id`, `version`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1867416522863620098, 1, 'SPARK', '讯飞星火', NULL, NULL, 'token', 5, '000000', NULL, NULL, 0, 103, 1, '2024-12-13 11:49:29', 1, '2024-12-13 11:49:29', 0);
INSERT INTO `gpt_open_key` (`id`, `type`, `value`, `name`, `app_id`, `app_key`, `app_secret`, `sort`, `tenant_id`, `dept_id`, `user_id`, `version`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1867416609320808450, 1, 'ALIYUN', '阿里云', NULL, NULL, 'token', 6, '000000', NULL, NULL, 0, 103, 1, '2024-12-13 11:49:49', 1, '2024-12-13 11:49:49', 0);
INSERT INTO `gpt_open_key` (`id`, `type`, `value`, `name`, `app_id`, `app_key`, `app_secret`, `sort`, `tenant_id`, `dept_id`, `user_id`, `version`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1867416749632860162, 1, 'VOLCENGINE', '火山', NULL, NULL, 'token', 7, '000000', NULL, NULL, 0, 103, 1, '2024-12-13 11:50:23', 1, '2024-12-13 11:50:23', 0);
INSERT INTO `gpt_open_key` (`id`, `type`, `value`, `name`, `app_id`, `app_key`, `app_secret`, `sort`, `tenant_id`, `dept_id`, `user_id`, `version`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1867417070203514882, 2, 'BAIDU', '百度', NULL, NULL, 'token', 1, '000000', NULL, NULL, 0, 103, 1, '2024-12-13 11:51:39', 1, '2024-12-13 11:51:39', 0);
INSERT INTO `gpt_open_key` (`id`, `type`, `value`, `name`, `app_id`, `app_key`, `app_secret`, `sort`, `tenant_id`, `dept_id`, `user_id`, `version`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1867417181126078466, 2, 'ZHIPU', '智谱清言', NULL, NULL, 'token', 2, '000000', NULL, NULL, 0, 103, 1, '2024-12-13 11:52:06', 1, '2024-12-13 11:52:06', 0);
INSERT INTO `gpt_open_key` (`id`, `type`, `value`, `name`, `app_id`, `app_key`, `app_secret`, `sort`, `tenant_id`, `dept_id`, `user_id`, `version`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1867417279461535746, 2, 'CZHAN_AI', '触站', NULL, NULL, 'token', 3, '000000', NULL, NULL, 0, 103, 1, '2024-12-13 11:52:29', 1, '2024-12-13 11:52:29', 0);
INSERT INTO `gpt_open_key` (`id`, `type`, `value`, `name`, `app_id`, `app_key`, `app_secret`, `sort`, `tenant_id`, `dept_id`, `user_id`, `version`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1867417355110002689, 2, 'SPARK', '讯飞星火', NULL, NULL, 'token', 4, '000000', NULL, NULL, 0, 103, 1, '2024-12-13 11:52:47', 1, '2024-12-13 11:52:47', 0);
INSERT INTO `gpt_open_key` (`id`, `type`, `value`, `name`, `app_id`, `app_key`, `app_secret`, `sort`, `tenant_id`, `dept_id`, `user_id`, `version`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1867417437586796546, 2, 'ALIYUN', '阿里云', NULL, NULL, 'token', 5, '000000', NULL, NULL, 0, 103, 1, '2024-12-13 11:53:07', 1, '2024-12-13 11:53:07', 0);
INSERT INTO `gpt_open_key` (`id`, `type`, `value`, `name`, `app_id`, `app_key`, `app_secret`, `sort`, `tenant_id`, `dept_id`, `user_id`, `version`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1867417600619393026, 2, 'NOLIPIX', '画宇宙', NULL, NULL, 'token', 6, '000000', NULL, NULL, 0, 103, 1, '2024-12-13 11:53:46', 1, '2024-12-13 11:53:46', 0);
INSERT INTO `gpt_open_key` (`id`, `type`, `value`, `name`, `app_id`, `app_key`, `app_secret`, `sort`, `tenant_id`, `dept_id`, `user_id`, `version`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1867417730101751810, 2, 'VOLCENGINE', '火山', NULL, NULL, 'token', 7, '000000', NULL, NULL, 0, 103, 1, '2024-12-13 11:54:16', 1, '2024-12-13 11:54:16', 0);
INSERT INTO `gpt_open_key` (`id`, `type`, `value`, `name`, `app_id`, `app_key`, `app_secret`, `sort`, `tenant_id`, `dept_id`, `user_id`, `version`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1867417907046854658, 3, 'ZHIPU', '智谱清言', NULL, NULL, 'token', 1, '000000', NULL, NULL, 0, 103, 1, '2024-12-13 11:54:59', 1, '2024-12-13 11:54:59', 0);
INSERT INTO `gpt_open_key` (`id`, `type`, `value`, `name`, `app_id`, `app_key`, `app_secret`, `sort`, `tenant_id`, `dept_id`, `user_id`, `version`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1867417985560031233, 3, 'SPARK', '讯飞星火', NULL, NULL, 'token', 2, '000000', NULL, NULL, 0, 103, 1, '2024-12-13 11:55:17', 1, '2024-12-13 11:55:17', 0);
INSERT INTO `gpt_open_key` (`id`, `type`, `value`, `name`, `app_id`, `app_key`, `app_secret`, `sort`, `tenant_id`, `dept_id`, `user_id`, `version`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1867418064702353409, 3, 'ALIYUN', '阿里云', NULL, NULL, 'token', 3, '000000', NULL, NULL, 0, 103, 1, '2024-12-13 11:55:36', 1, '2024-12-13 11:55:36', 0);
INSERT INTO `gpt_open_key` (`id`, `type`, `value`, `name`, `app_id`, `app_key`, `app_secret`, `sort`, `tenant_id`, `dept_id`, `user_id`, `version`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1867423088606130178, 3, 'VOLCENGINE', '火山', NULL, NULL, 'token', 4, '000000', NULL, NULL, 0, 103, 1, '2024-12-13 12:15:34', 1, '2024-12-13 12:15:34', 0);
