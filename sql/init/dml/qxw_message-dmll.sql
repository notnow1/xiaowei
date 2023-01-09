USE `qxw_message`;
SET NAMES utf8mb4;

-- ----------------------------
-- initialization data
-- ----------------------------
-- ----------------------------
-- init of message_content_config
-- ----------------------------
INSERT INTO message_content_config (message_type, business_type, business_subtype, receive_role, message_template, receive_user, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, 1, 101, 0, '恭喜，您的二级域名申请已审核通过，系统将于{validity_time_domain}替换为以下登录地址：https://{domain}.qixiaowei.net。', NULL, 0, 1, sysdate(), 1, sysdate());
INSERT INTO message_content_config (message_type, business_type, business_subtype, receive_role, message_template, receive_user, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, 1, 102, 0, '很遗憾，您的二级域名申请审核未通过。', NULL, 0, 1, sysdate(), 1, sysdate());
INSERT INTO message_content_config (message_type, business_type, business_subtype, receive_role, message_template, receive_user, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, 27, 2700, 0, '{employee_name}（{employee_code}）已将{target_year}年度，对于{decomposition_dimension}的{time_dimension}{target_decompose_type}滚动预测移交给您，请知悉。', NULL, 0, 1, sysdate(), 1, sysdate());
INSERT INTO message_content_config (message_type, business_type, business_subtype, receive_role, message_template, receive_user, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, 23, 2300, 0, '{employee_name}（{employee_code}）已邀请您参与{target_year}年度，对于{decomposition_dimension}的{time_dimension}销售订单滚动预测，请知悉。', NULL, 0, 1, sysdate(), 1, sysdate());
INSERT INTO message_content_config (message_type, business_type, business_subtype, receive_role, message_template, receive_user, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, 24, 2400, 0, '{employee_name}（{employee_code}）已邀请您参与{target_year}年度，对于{decomposition_dimension}的{time_dimension}销售收入滚动预测，请知悉。', NULL, 0, 1, sysdate(), 1, sysdate());
INSERT INTO message_content_config (message_type, business_type, business_subtype, receive_role, message_template, receive_user, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, 25, 2500, 0, '{employee_name}（{employee_code}）已邀请您参与{target_year}年度，对于{decomposition_dimension}的{time_dimension}销售回款滚动预测，请知悉。', NULL, 0, 1, sysdate(), 1, sysdate());
INSERT INTO message_content_config (message_type, business_type, business_subtype, receive_role, message_template, receive_user, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, 26, 2600, 0, '{employee_name}（{employee_code}）已邀请您参与{target_year}年度，对于{decomposition_dimension}的{time_dimension}自定义滚动预测，请知悉。', NULL, 0, 1, sysdate(), 1, sysdate());





