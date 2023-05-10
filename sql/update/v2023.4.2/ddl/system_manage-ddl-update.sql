USE `system_manage`;
SET NAMES utf8mb4;


-- ----------------------------
-- update of menu
-- ----------------------------
ALTER TABLE `user` MODIFY COLUMN status tinyint unsigned NOT NULL COMMENT '状态:0失效;1生效;2未激活';

-- ----------------------------
-- update of tenant
-- ----------------------------
ALTER TABLE `tenant`
ADD COLUMN admin_name VARCHAR(32)    COMMENT '管理员姓名' AFTER `domain`;
