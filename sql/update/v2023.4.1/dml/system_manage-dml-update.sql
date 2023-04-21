USE `system_manage`;
SET NAMES utf8mb4;

-- ----------------------------
-- update of menu
-- ----------------------------
UPDATE menu SET sort=1 WHERE menu_id=10;
UPDATE menu SET sort=3 WHERE menu_id=13;
UPDATE menu SET sort=2 WHERE menu_id=14;
