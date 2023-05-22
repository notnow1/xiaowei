USE `system_manage`;
SET NAMES utf8mb4;

-- ----------------------------
-- add of menu
-- ----------------------------
INSERT INTO menu (menu_id, parent_menu_id, menu_type, menu_name, product_package_id, sort, `path`, component, query, external_link_flag, cache_flag, visible_flag, permission_code, icon, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(490, 50, 3, '枚举值字段编辑', NULL, 1, NULL, NULL, NULL, 0, 0, 1, 'system:manage:dictionaryType:edit', NULL, 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO menu (menu_id, parent_menu_id, menu_type, menu_name, product_package_id, sort, `path`, component, query, external_link_flag, cache_flag, visible_flag, permission_code, icon, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(491, 8, 3, '重置账号', NULL, 8, NULL, NULL, NULL, 0, 0, 1, 'system:manage:user:resetUserAccount', NULL, 1, 0, 1, sysdate(), 1, sysdate());

-- ----------------------------
-- update of menu
-- ----------------------------
UPDATE menu SET menu_name='枚举值选项新增', sort=2, update_time=sysdate() WHERE menu_id=219;
UPDATE menu SET menu_name='枚举值选项编辑', sort=3, update_time=sysdate() WHERE menu_id=220;
UPDATE menu SET menu_name='枚举值选项删除', sort=4, update_time=sysdate() WHERE menu_id=221;
UPDATE menu SET delete_flag=1, update_time=sysdate() WHERE menu_id IN (10,28,29,199,202,347,348,349);
UPDATE menu SET menu_name='组织与人员', permission_code='system:manage:department:employee:pageList', sort=1, update_time=sysdate() WHERE menu_id=12;
UPDATE menu SET menu_name='启用', update_time=sysdate() WHERE menu_id=27;
UPDATE menu SET menu_name='组织与人员新增', permission_code='system:manage:department:employee:add', update_time=sysdate() WHERE menu_id=36;
UPDATE menu SET menu_name='组织与人员编辑', permission_code='system:manage:department:employee:edit', update_time=sysdate() WHERE menu_id=40;
UPDATE menu SET menu_name='组织与人员详情', permission_code='system:manage:department:employee:info', update_time=sysdate() WHERE menu_id=47;
UPDATE menu SET menu_name='停用', update_time=sysdate() WHERE menu_id=183;
UPDATE menu SET permission_code='system:manage:department:employee:remove', update_time=sysdate() WHERE menu_id=206;
UPDATE menu SET permission_code='system:manage:department:employee:import', update_time=sysdate() WHERE menu_id=207;
UPDATE menu SET permission_code='system:manage:department:employee:export', update_time=sysdate() WHERE menu_id=208;
UPDATE menu SET menu_type=2,cache_flag=1,visible_flag=0,`path`='ArticleWageEdit',component='manage/result/emolumentSetting/articleWage/components/edit', update_time=sysdate() WHERE menu_id=346;
UPDATE menu SET menu_name='薪酬项目编辑',update_time=sysdate() WHERE menu_id=346;

