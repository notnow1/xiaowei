USE `operate_cloud`;
SET NAMES utf8mb4;
-- ----------------------------
-- 修改产品数据
-- ----------------------------
UPDATE product SET listing_flag =1 WHERE listing_flag is null ;
