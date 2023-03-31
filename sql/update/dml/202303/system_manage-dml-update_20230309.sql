USE `system_manage`;
SET NAMES utf8mb4;

-- ----------------------------
-- ----------------------------
INSERT INTO dictionary_type (dictionary_type, dictionary_name, menu_zeroth_name, menu_first_name, menu_second_name, remark, status, delete_flag, create_by, create_time, update_by, update_time,tenant_id) VALUES('MARKET_INSIGHT_SELF_CAPACITY_FACTOR', '对比项目', '战略云', '市场洞察', '看对手;看自身', '', 1, 0, 0, sysdate(), 0, sysdate(),0);

-- ----------------------------
-- init of dictionary_data
-- ----------------------------
Set @dictionary_type_id6 = (select dictionary_type_id  from dictionary_type where  dictionary_type='MARKET_INSIGHT_SELF_CAPACITY_FACTOR' and  tenant_id =0);
INSERT INTO dictionary_data ( dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id6, '品牌', '1', 0, 1, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data ( dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id6, '客户关系', '2', 0, 2, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id6, '技术', '3', 0, 3, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id6, '成本', '4', 0, 4, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id6, '质量', '5', 0, 5, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id6, '服务', '6', 0, 6, '', 1, 0, 1, sysdate(), 1, sysdate());



-- ----------------------------
-- ----------------------------
INSERT INTO dictionary_type (dictionary_type, dictionary_name, menu_zeroth_name, menu_first_name, menu_second_name, remark, status, delete_flag, create_by, create_time, update_by, update_time,tenant_id) VALUES('MARKET_INSIGHT_OPPONENT_COMPETITOR_CATEGORY', '竞争对手类别', '战略云', '市场洞察', '看对手', '', 1, 0, 0, sysdate(), 0, sysdate(),0);

-- ----------------------------
-- init of dictionary_data
-- ----------------------------
Set @dictionary_type_id7 = (select dictionary_type_id  from dictionary_type where  dictionary_type='MARKET_INSIGHT_OPPONENT_COMPETITOR_CATEGORY' and tenant_id =0);
INSERT INTO dictionary_data ( dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id7, '标杆企业', '1', 0, 1, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data ( dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id7, '战略合作', '2', 0, 2, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id7, '战略竞争', '3', 0, 3, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id7, '一般竞争', '4', 0, 4, '', 1, 0, 1, sysdate(), 1, sysdate());


-- ----------------------------
INSERT INTO dictionary_type (dictionary_type, dictionary_name, menu_zeroth_name, menu_first_name, menu_second_name, remark, status, delete_flag, create_by, create_time, update_by, update_time,tenant_id) VALUES('MARKET_INSIGHT_OPPONENT_COMPETITION_STRATEGY_TYPE', '竞争策略类型', '战略云', '市场洞察', '看对手', '', 1, 0, 0, sysdate(), 0, sysdate(),0);

-- ----------------------------
-- init of dictionary_data
-- ----------------------------
Set @dictionary_type_id8 = (select dictionary_type_id  from dictionary_type where  dictionary_type='MARKET_INSIGHT_OPPONENT_COMPETITION_STRATEGY_TYPE' and tenant_id =0);
INSERT INTO dictionary_data ( dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id8, '先发制人策略', '1', 0, 1, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data ( dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id8, '硬碰硬策略', '2', 0, 2, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id8, '迂回策略', '3', 0, 3, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id8, '各个击破策略', '4', 0, 4, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id8, '拖延策略', '5', 0, 5, '', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES( @dictionary_type_id8, '产品价格恶性竞争', '6', 0, 6, '', 1, 0, 1, sysdate(), 1, sysdate());


