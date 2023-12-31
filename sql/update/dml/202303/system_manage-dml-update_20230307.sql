USE `system_manage`;
SET NAMES utf8mb4;
-- ----------------------------
-- ----------------------------
INSERT INTO dictionary_type (dictionary_type, dictionary_name, menu_zeroth_name, menu_first_name, menu_second_name, remark, status, delete_flag, create_by, create_time, update_by, update_time,tenant_id) VALUES('MARKET_INSIGHT_INDUSTRY_INDUSTRY_TYPE', '行业类型', '战略云', '市场洞察', '看行业', '', 1, 0, 0, sysdate(), 0, sysdate(),0);

-- ----------------------------
-- init of dictionary_data
-- ----------------------------
Set @dictionary_type_id = (select dictionary_type_id  from dictionary_type where  dictionary_type='MARKET_INSIGHT_INDUSTRY_INDUSTRY_TYPE' and tenant_id =0);
INSERT INTO dictionary_data ( dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id, '改造型', '1', 0, 1, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data ( dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id, '替代型', '2', 0, 2, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id, '创新型', '3', 0, 3, '', 1, 0, 1, sysdate(), 1, sysdate());


-- ----------------------------
-- update of menu
-- ----------------------------
UPDATE menu SET sort=11 WHERE menu_id=18;
UPDATE menu SET parent_menu_id=7, sort=10, component='setting/basics/regional/index', product_package_id=NULL WHERE menu_id=22;
UPDATE menu SET parent_menu_id=7, sort=9, component='setting/basics/product/index', product_package_id=NULL WHERE menu_id=23;
UPDATE menu SET parent_menu_id=7, sort=8, component='setting/basics/productUnit/index', product_package_id=NULL WHERE menu_id=24;
UPDATE menu SET component='setting/basics/product/components/add', product_package_id=NULL WHERE menu_id=57;
UPDATE menu SET component='setting/basics/product/components/edit', product_package_id=NULL WHERE menu_id=58;
UPDATE menu SET component='setting/basics/product/components/detail', product_package_id=NULL WHERE menu_id=59;