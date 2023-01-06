USE `operate_cloud`;
SET NAMES utf8mb4;
-- ----------------------------
-- employee_budget、employee_budget_adjusts、employee_budget_details update
-- ----------------------------
ALTER TABLE employee_budget MODIFY COLUMN amount_adjust INT NULL COMMENT '本年新增人数';
ALTER TABLE employee_budget_adjusts MODIFY COLUMN number_adjust INT NULL COMMENT '调整人数';
ALTER TABLE employee_budget_details MODIFY COLUMN count_adjust INT NULL COMMENT '本年新增人数';