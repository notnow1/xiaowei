USE `system_manage`;
SET NAMES utf8mb4;
-- ----------------------------
-- menu update
-- ----------------------------
UPDATE menu SET product_package_id=1 WHERE menu_id IN (21,22,23,24,25,26,57,58,59,60,61,62,63,64,222,223,224,225,226,227,231,232,233,234,235,236,240,244);

UPDATE menu SET menu_type=2, menu_name='组织信息配置新增', `path`='organizationAdd', component='setting/basics/organization/components/add', cache_flag=1, visible_flag=0, icon='transparent' WHERE menu_id=199;

UPDATE menu SET menu_type=2, menu_name='绩效等级配置新增', product_package_id=1, `path`='performanceRankAdd', component='setting/manage/performanceRank/components/add', cache_flag=1, visible_flag=0, icon='transparent' WHERE menu_id = 237;

INSERT INTO menu (menu_id, parent_menu_id, menu_type, menu_name, product_package_id, sort, `path`, component, query, external_link_flag, cache_flag, visible_flag, permission_code, icon, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(365, 13, 3, '导入', 1, 5, NULL, NULL, NULL, 0, 0, 1, 'system:manage:post:import', NULL, 1, 0, 1, sysdate(), 1, sysdate());
