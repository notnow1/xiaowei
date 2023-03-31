USE `system_manage`;
SET NAMES utf8mb4;
-- ----------------------------
-- ----------------------------

-- init of dictionary_type
-- ----------------------------
INSERT INTO dictionary_type (dictionary_type, dictionary_name, menu_zeroth_name, menu_first_name, menu_second_name, remark, status, delete_flag, create_by, create_time, update_by, update_time,tenant_id) VALUES('STRATEGY_MEASURE_SOURCE', '来源', '战略云', '战略解码', '战略举措清单', '', 1, 0, 0, sysdate(), 0, sysdate(),0);


-- ----------------------------
-- init of dictionary_data
-- ----------------------------
Set @dictionary_type_id = (select dictionary_type_id  from dictionary_type where  dictionary_type='STRATEGY_MEASURE_SOURCE' and tenant_id =0);
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES ( @dictionary_type_id, '差距分析', '1', 0, 1, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES ( @dictionary_type_id, '看宏观', '2', 0, 2, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES ( @dictionary_type_id, '看行业', '3', 0, 3, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES ( @dictionary_type_id, '看客户', '4', 0, 4, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES ( @dictionary_type_id, '看对手', '5', 0, 5, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES ( @dictionary_type_id, '看自身', '6', 0, 6, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES ( @dictionary_type_id, '战略意图', '7', 0, 7, '', 1, 0, 1, sysdate(), 1, sysdate());


INSERT INTO dictionary_type (dictionary_type, dictionary_name, menu_zeroth_name, menu_first_name, menu_second_name, remark, status, delete_flag, create_by, create_time, update_by, update_time,tenant_id) VALUES('MARKET_INSIGHT_CUSTOMER_CUSTOMER_CATEGORY', '客户类别', '战略云', '市场洞察', '看客户', '', 1, 0, 0, sysdate(), 0, sysdate(),0);

-- ----------------------------
-- init of dictionary_data
-- ----------------------------
Set @dictionary_type_id2 = (select dictionary_type_id  from dictionary_type where  dictionary_type='MARKET_INSIGHT_CUSTOMER_CUSTOMER_CATEGORY' and tenant_id =0);
INSERT INTO dictionary_data ( dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id2, '战略大客户', '1', 0, 1, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data ( dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id2, '战略拓展客户', '2', 0, 2, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id2, '战略潜在客户', '3', 0, 3, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id2, '检验客户（普通客户）', '4', 0, 4, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id2, '空白客户（零星客户）', '5', 0, 5, '', 1, 0, 1, sysdate(), 1, sysdate());

