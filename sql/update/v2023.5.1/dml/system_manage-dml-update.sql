USE `system_manage`;
SET NAMES utf8mb4;

-- ----------------------------
-- add of menu
-- ----------------------------
INSERT INTO menu (menu_id, parent_menu_id, menu_type, menu_name, product_package_id, sort, `path`, component, query, external_link_flag, cache_flag, visible_flag, permission_code, icon, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(490, 50, 3, '枚举值字段编辑', NULL, 1, NULL, NULL, NULL, 0, 0, 1, 'system:manage:dictionaryType:edit', NULL, 1, 0, 1, sysdate(), 1, sysdate());

-- ----------------------------
-- update of menu
-- ----------------------------
UPDATE menu SET menu_name='枚举值选项新增', sort=2, update_time=sysdate() WHERE menu_id=219;
UPDATE menu SET menu_name='枚举值选项编辑', sort=3, update_time=sysdate() WHERE menu_id=220;
UPDATE menu SET menu_name='枚举值选项删除', sort=4, update_time=sysdate() WHERE menu_id=221;
