USE `operate_cloud`;
SET NAMES utf8mb4;
-- ----------------------------
-- bonus_budget update
-- ----------------------------
ALTER TABLE `bonus_budget`
ADD COLUMN amount_wage_budget DECIMAL(14,2)    COMMENT '总工资包预算' AFTER `amount_bonus_budget`;