USE `system_manage`;
SET NAMES utf8mb4;

-- ----------------------------
-- initialization data
-- ----------------------------

-- ----------------------------
-- init of user
-- ----------------------------
INSERT INTO `user` (user_id, employee_id, user_account, password, user_name, mobile_phone, email, avatar, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, NULL, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '超级管理员', NULL, NULL, NULL, NULL, 1, 0, 0, sysdate(), 0, sysdate());


-- ----------------------------
-- init of role
-- ----------------------------
INSERT INTO `role` (role_id, role_code, role_name, data_scope, product_package, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, 'admin', '超级管理员', 1, NULL, 1, '超级管理员', 1, 0, 0, sysdate(), 0, sysdate());


-- ----------------------------
-- init of user_role
-- ----------------------------
INSERT INTO user_role (user_role_id, user_id, role_id, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, 1, 1, 0, 0, sysdate(), 0, sysdate());


-- ----------------------------
-- init of config
-- ----------------------------
INSERT INTO config (config_id, parent_config_id, path_code, config_code, config_value, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, 0, '', 'basic', NULL, '基础配置', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO config (config_id, parent_config_id, path_code, config_code, config_value, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(2, 1, 'basic', 'industry_enable', '0', '行业启用：1系统；2自定义', 1, 0, 1, sysdate(), 1, sysdate());


-- ----------------------------
-- init of indicator
-- ----------------------------
INSERT INTO `indicator` VALUES (1, 0, '', 1, 'CW001', '订单（不含税）', 0, 1, 1, 1, 1, 1, '从销售订单目标制定模块引入数据，不可改', '', 1, '', 0, 0, sysdate(), 0, sysdate());
INSERT INTO `indicator` VALUES (2, 0, '', 1, 'CW002', '销售收入', 0, 1, 1, 1, 1, 1, '从销售收入目标制定模块引入数据，不可改', '', 1, '', 0, 0, sysdate(), 0, sysdate());
INSERT INTO `indicator` VALUES (22, 0, '', 1, 'CW022', '回款金额（含税）', 0, 1, 1, 1, 1, 1, '从销售回款目标制定模块引入数据，不可改', '', 1, '', 0, 0, sysdate(), 0, sysdate());
INSERT INTO `indicator` VALUES (13, 0, '', 1, 'CW010', '销售毛利', 0, 1, 1, 1, 1, 1, '默认值系统自动计算，等于销售收入-销售成本，可修改','' , 1, '', 0, 0, sysdate(), 0, sysdate());
INSERT INTO `indicator` VALUES (14, 0, '', 1, 'CW020', '净利润', 0, 1, 1, 1, 1, 1, '默认值系统自动计算，等于税前利润-企业所得税，可修改', '', 1, '', 0, 0, sysdate(), 0, sysdate());


-- ----------------------------
-- init of dictionary_type
-- ----------------------------
INSERT INTO dictionary_type (dictionary_type_id, dictionary_type, dictionary_name, menu_zeroth_name, menu_first_name, menu_second_name, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, 'PRODUCT_CATEGORY', '产品类别', '设置管理', '经营云配置', '产品配置', '', 0, 0, 0, sysdate(), 1, sysdate());


-- ----------------------------
-- init of dictionary_data
-- ----------------------------
INSERT INTO dictionary_data (dictionary_data_id, dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, 1, '通用件', '1', 0, 1, '', 0, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_data_id, dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(2, 1, '标准件', '2', 0, 2, '', 0, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_data_id, dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(3, 1, '自制件', '3', 0, 3, '', 0, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_data_id, dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(4, 1, '外购件', '4', 0, 4, '', 0, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_data_id, dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(5, 1, '外协件', '5', 0, 5, '', 0, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_data_id, dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(6, 1, '原材料', '6', 0, 6, '', 0, 0, 1, sysdate(), 1, sysdate());


-- ----------------------------
-- init of xxx
-- ----------------------------



commit;