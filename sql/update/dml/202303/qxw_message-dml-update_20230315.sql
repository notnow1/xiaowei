USE `qxw_message`;
SET NAMES utf8mb4;

-- ----------------------------
-- update of message_content_config
-- ----------------------------
UPDATE message_content_config SET message_template='您好，您的增略管理平台登录密码重置成功，新密码为{password}，为确保账号安全，登录后可在【用户设置】中修改密码。' WHERE message_content_config_id=8;



