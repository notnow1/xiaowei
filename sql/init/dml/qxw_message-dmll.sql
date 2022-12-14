USE `qxw_message`;
SET NAMES utf8mb4;

-- ----------------------------
-- initialization data
-- ----------------------------
-- ----------------------------
-- init of message_content_config
-- ----------------------------
INSERT INTO message_content_config (message_type, business_type, business_subtype, receive_role, message_template, receive_user, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, 1, 1002, 0, '恭喜，您的二级域名申请已审核通过，系统将于{validity_time_domain}替换为以下登录地址：https://{domain}.qixiaowei.net。', NULL, 0, 1, sysdate(), 1, sysdate());
INSERT INTO message_content_config (message_type, business_type, business_subtype, receive_role, message_template, receive_user, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, 1, 1003, 0, '很遗憾，您的二级域名申请审核未通过。', NULL, 0, 1, sysdate(), 1, sysdate());





