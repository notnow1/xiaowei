USE `system_manage`;
SET NAMES utf8mb4;

-- ----------------------------
-- role update
-- ----------------------------
ALTER TABLE `role`
ADD COLUMN role_type TINYINT UNSIGNED DEFAULT 1 NOT NULL COMMENT '角色类型:0内置角色;1自定义角色' AFTER `role_id`;

-- ----------------------------
-- user update
-- ----------------------------
ALTER TABLE `user`
ADD COLUMN user_type TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '用户类型:0其他;1系统管理员' AFTER `user_id`;