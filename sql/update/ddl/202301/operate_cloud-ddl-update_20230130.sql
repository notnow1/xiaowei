USE `operate_cloud`;
SET NAMES utf8mb4;
-- ----------------------------
-- emp_annual_bonus_objects update
-- ----------------------------
ALTER TABLE `emp_annual_bonus_objects`
ADD COLUMN responsible_employee_id BIGINT UNSIGNED    COMMENT '主管负责人ID' AFTER `choice_flag`,
ADD COLUMN responsible_employee_name VARCHAR(64)    COMMENT '主管负责人' AFTER `responsible_employee_id`;

