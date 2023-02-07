USE `operate_cloud`;
SET NAMES utf8mb4;
-- ----------------------------
-- target_decompose_history、decompose_details_snapshot、detail_cycles_snapshot update
-- ----------------------------


UPDATE target_decompose_history SET delete_flag=1 WHERE delete_flag=0;
UPDATE decompose_details_snapshot SET delete_flag=1 WHERE delete_flag=0;
UPDATE detail_cycles_snapshot SET delete_flag=1 WHERE delete_flag=0;
