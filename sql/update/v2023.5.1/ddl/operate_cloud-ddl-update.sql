USE `operate_cloud`;
SET NAMES utf8mb4;
-- ----------------------------
-- ddl update salary_item
-- ----------------------------
ALTER TABLE `salary_item`
ADD COLUMN status TINYINT UNSIGNED NOT NULL   COMMENT '状态:0失效;1生效' AFTER `sort`;

ALTER TABLE `salary_item` MODIFY COLUMN `scope` tinyint unsigned NULL COMMENT '作用范围：1部门;2公司';
