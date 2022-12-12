USE `operate_cloud`;
SET NAMES utf8mb4;

-- ----------------------------
-- initialization data
-- ----------------------------

-- ----------------------------
-- init of salary_item
-- ----------------------------
INSERT INTO `salary_item`(`salary_item_id`, `first_level_item`, `second_level_item`, `third_level_item`, `scope`, `sort`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1, 1, 1, '基本工资', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `salary_item`(`salary_item_id`, `first_level_item`, `second_level_item`, `third_level_item`, `scope`, `sort`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (2, 2, 4, '战略奖', 2, 2, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `salary_item`(`salary_item_id`, `first_level_item`, `second_level_item`, `third_level_item`, `scope`, `sort`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (3, 2, 4, '项目奖', 1, 3, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `salary_item`(`salary_item_id`, `first_level_item`, `second_level_item`, `third_level_item`, `scope`, `sort`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (4, 2, 4, '绩效奖金', 1, 4, 0, 0, sysdate(), 0, sysdate());
