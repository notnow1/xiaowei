USE `operate_cloud`;
SET NAMES utf8mb4;
-- ----------------------------
-- dml update
-- ----------------------------


UPDATE salary_item SET `status`  = 1 WHERE delete_flag = 0;