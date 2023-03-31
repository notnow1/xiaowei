USE `system_manage`;
SET NAMES utf8mb4;
-- ----------------------------
-- ----------------------------
-- init of dictionary_type
-- ----------------------------
INSERT INTO dictionary_type (dictionary_type, dictionary_name, menu_zeroth_name, menu_first_name, menu_second_name, remark, status, delete_flag, create_by, create_time, update_by, update_time,tenant_id) VALUES('MARKET_INSIGHT_MACRO_VISUAL_ANGLE', '视角', '战略云', '市场洞察', '看宏观', '', 1, 0, 0, sysdate(), 0, sysdate(),0);

-- ----------------------------
-- init of dictionary_data
-- ----------------------------
Set @dictionary_type_id = (select dictionary_type_id  from dictionary_type where  dictionary_type='MARKET_INSIGHT_MACRO_VISUAL_ANGLE' and tenant_id =0);
INSERT INTO dictionary_data ( dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id, '政策', '1', 0, 1, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data ( dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id, '经济', '2', 0, 2, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id, '社会', '3', 0, 3, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id, '技术', '4', 0, 4, '', 1, 0, 1, sysdate(), 1, sysdate());
