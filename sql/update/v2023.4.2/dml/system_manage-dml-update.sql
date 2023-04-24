USE `system_manage`;
SET NAMES utf8mb4;

-- ----------------------------
-- add of menu
-- ----------------------------
INSERT INTO menu (menu_id, parent_menu_id, menu_type, menu_name, product_package_id, sort, `path`, component, query, external_link_flag, cache_flag, visible_flag, permission_code, icon, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(489, 116, 3, '导入', 1, 2, NULL, NULL, NULL, 0, 0, 1, 'operate:cloud:officialRankEmolument:import', NULL, 1, 0, 1, sysdate(), 1, sysdate());

-- ----------------------------
-- update of menu
-- ----------------------------
UPDATE menu SET sort=1 WHERE menu_id=10;
UPDATE menu SET sort=3 WHERE menu_id=13;
UPDATE menu SET sort=2 WHERE menu_id=14;