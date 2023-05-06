USE `operate_cloud`;
SET NAMES utf8mb4;
-- ----------------------------
-- ddl update salary_item
-- ----------------------------
ALTER TABLE `salary_item`
ADD COLUMN status TINYINT UNSIGNED NOT NULL   COMMENT '状态:0失效;1生效' AFTER `sort`;
