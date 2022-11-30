USE `system_manage`;
SET NAMES utf8mb4;

-- ----------------------------
-- initialization data
-- ----------------------------

-- ----------------------------
-- init of user
-- ----------------------------
INSERT INTO `user` (user_id, employee_id, user_account, password, user_name, mobile_phone, email, avatar, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, NULL, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '超级管理员', NULL, NULL, NULL, NULL, 1, 0, 0, sysdate(), 0, sysdate());


-- ----------------------------
-- init of role
-- ----------------------------
INSERT INTO `role` (role_id, role_code, role_name, data_scope, product_package, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, 'admin', '超级管理员', 1, NULL, 1, '超级管理员', 1, 0, 0, sysdate(), 0, sysdate());


-- ----------------------------
-- init of user_role
-- ----------------------------
INSERT INTO user_role (user_role_id, user_id, role_id, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, 1, 1, 0, 0, sysdate(), 0, sysdate());


-- ----------------------------
-- init of config
-- ----------------------------
INSERT INTO config (config_id, parent_config_id, path_code, config_code, config_value, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, 0, '', 'basic', NULL, '基础配置', 1, 0, 1, sysdate(), 1, sysdate());
INSERT INTO config (config_id, parent_config_id, path_code, config_code, config_value, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(2, 1, 'basic', 'industry_enable', '0', '行业启用：1系统；2自定义', 1, 0, 1, sysdate(), 1, sysdate());


-- ----------------------------
-- init of indicator
-- ----------------------------
INSERT INTO `indicator` VALUES (1, 0, '', 1, 'CW001', '订单（不含税）', 0, 1, 1, 1, 1, 1, '从销售订单目标制定模块引入数据，不可改', '', 1, '', 0, 0, sysdate(), 0, sysdate());
INSERT INTO `indicator` VALUES (2, 0, '', 1, 'CW002', '销售收入', 0, 1, 1, 1, 1, 1, '从销售收入目标制定模块引入数据，不可改', '', 1, '', 0, 0, sysdate(), 0, sysdate());
INSERT INTO `indicator` VALUES (22, 0, '', 1, 'CW022', '回款金额（含税）', 0, 1, 1, 1, 1, 1, '从销售回款目标制定模块引入数据，不可改', '', 1, '', 0, 0, sysdate(), 0, sysdate());
INSERT INTO `indicator` VALUES (13, 0, '', 1, 'CW010', '销售毛利', 0, 1, 1, 1, 1, 1, '默认值系统自动计算，等于销售收入-销售成本，可修改','' , 1, '', 0, 0, sysdate(), 0, sysdate());
INSERT INTO `indicator` VALUES (14, 0, '', 1, 'CW020', '净利润', 0, 1, 1, 1, 1, 1, '默认值系统自动计算，等于税前利润-企业所得税，可修改', '', 1, '', 0, 0, sysdate(), 0, sysdate());


-- ----------------------------
-- init of dictionary_type
-- ----------------------------
INSERT INTO dictionary_type (dictionary_type_id, dictionary_type, dictionary_name, menu_zeroth_name, menu_first_name, menu_second_name, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, 'PRODUCT_CATEGORY', '产品类别', '设置管理', '经营云配置', '产品配置', '', 0, 0, 0, sysdate(), 1, sysdate());


-- ----------------------------
-- init of dictionary_data
-- ----------------------------
INSERT INTO dictionary_data (dictionary_data_id, dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(1, 1, '通用件', '1', 0, 1, '', 0, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_data_id, dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(2, 1, '标准件', '2', 0, 2, '', 0, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_data_id, dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(3, 1, '自制件', '3', 0, 3, '', 0, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_data_id, dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(4, 1, '外购件', '4', 0, 4, '', 0, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_data_id, dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(5, 1, '外协件', '5', 0, 5, '', 0, 0, 1, sysdate(), 1, sysdate());
INSERT INTO dictionary_data (dictionary_data_id, dictionary_type_id, dictionary_label, dictionary_value, default_flag, sort, remark, status, delete_flag, create_by, create_time, update_by, update_time) VALUES(6, 1, '原材料', '6', 0, 6, '', 0, 0, 1, sysdate(), 1, sysdate());


-- ----------------------------
-- init of industry_default
-- ----------------------------
INSERT INTO `industry_default` VALUES (36, 0, NULL, '110000', '农林牧渔', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (47, 0, NULL, '210000', '采掘', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (51, 0, NULL, '220000', '化工', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (75, 0, NULL, '230000', '黑色金属', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (77, 0, NULL, '240000', '有色金属', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (85, 0, NULL, '250000', '建筑建材', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (91, 0, NULL, '260000', '机械设备', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (111, 0, NULL, '270000', '电子元器件', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (117, 0, NULL, '310000', '交运设备', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (128, 0, NULL, '320000', '信息设备', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (133, 0, NULL, '330000', '家用电器', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (140, 0, NULL, '340000', '食品饮料', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (151, 0, NULL, '350000', '纺织服装', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (156, 0, NULL, '360000', '轻工制造', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (161, 0, NULL, '370000', '医药生物', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (168, 0, NULL, '410000', '公用事业', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (175, 0, NULL, '420000', '交通运输', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (183, 0, NULL, '430000', '房地产', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (185, 0, NULL, '440000', '金融服务', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (189, 0, NULL, '450000', '商业贸易', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (193, 0, NULL, '460000', '餐饮旅游', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (198, 0, NULL, '470000', '信息服务', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (207, 0, NULL, '510000', '综合', 1, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (208, 36, '36', '110100', '种植业', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (211, 36, '36', '110200', '渔业', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (213, 36, '36', '110300', '林业', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (214, 36, '36', '110400', '饲料', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (215, 36, '36', '110500', '农产品加工', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (218, 36, '36', '110600', '农业综合', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (219, 47, '47', '210100', '石油开采', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (220, 47, '47', '210200', '煤炭开采', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (222, 47, '47', '210300', '其他采掘', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (223, 51, '51', '220100', '石油化工', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (225, 51, '51', '220200', '化学原料', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (229, 51, '51', '220300', '化学制品', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (238, 51, '51', '220400', '化学纤维', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (243, 51, '51', '220500', '塑料', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (244, 51, '51', '220600', '橡胶', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (246, 51, '51', '220700', '化工新材料', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (247, 75, '75', '230100', '钢铁', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (249, 77, '77', '240100', '有色金属冶炼', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (254, 77, '77', '240200', '金属新材料', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (257, 85, '85', '250100', '建筑材料', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (261, 85, '85', '250200', '建筑装饰', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (263, 91, '91', '260100', '普通机械', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (269, 91, '91', '260200', '专用设备', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (276, 91, '91', '260300', '仪器仪表', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (277, 91, '91', '260400', '电气设备', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (282, 91, '91', '260500', '金属制品', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (283, 111, '111', '270100', '半导体', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (286, 111, '111', '270200', '元件', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (287, 111, '111', '270300', '显示器件', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (288, 111, '111', '270400', '其他电子器件', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (289, 117, '117', '310100', '汽车整车', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (293, 117, '117', '310200', '汽车零部件', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (294, 117, '117', '310300', '非汽车交运设备', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (299, 117, '117', '310400', '汽车服务', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (300, 128, '128', '320100', '通信设备', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (304, 128, '128', '320200', '计算机设备', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (305, 133, '133', '330100', '白色家电', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (310, 133, '133', '330200', '视听器材', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (312, 140, '140', '340100', '食品加工', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (315, 140, '140', '340200', '食品制造', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (317, 140, '140', '340300', '饮料制造', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (323, 151, '151', '350100', '纺织', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (327, 151, '151', '350200', '服装', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (328, 156, '156', '360100', '造纸', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (329, 156, '156', '360200', '包装印刷', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (330, 156, '156', '360300', '其他轻工制造', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (333, 161, '161', '370100', '化学制药', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (335, 161, '161', '370200', '中药', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (336, 161, '161', '370300', '生物制品', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (337, 161, '161', '370400', '医药商业', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (338, 161, '161', '370500', '医疗器械', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (339, 161, '161', '370600', '医疗服务', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (340, 168, '168', '410100', '电力', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (344, 168, '168', '410200', '水务', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (345, 168, '168', '410300', '燃气', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (346, 168, '168', '410400', '环保', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (347, 175, '175', '420100', '港口', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (348, 175, '175', '420200', '高速公路', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (349, 175, '175', '420300', '公交', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (350, 175, '175', '420400', '航空运输', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (351, 175, '175', '420500', '机场', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (352, 175, '175', '420600', '航运', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (353, 175, '175', '420700', '铁路运输', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (354, 175, '175', '420800', '物流', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (355, 183, '183', '430100', '房地产开发', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (356, 183, '183', '430200', '园区开发', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (357, 185, '185', '440100', '银行', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (358, 185, '185', '440200', '信托', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (359, 185, '185', '440300', '证券', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (360, 185, '185', '440400', '保险', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (361, 189, '189', '450100', '零售', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (364, 189, '189', '450200', '贸易', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (365, 193, '193', '460100', '景点', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (367, 193, '193', '460200', '酒店', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (368, 193, '193', '460300', '旅游综合', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (369, 193, '193', '460400', '餐饮', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (370, 198, '198', '470100', '通信运营', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (371, 198, '198', '470200', '网络服务', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (375, 198, '198', '470300', '计算机应用', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (377, 198, '198', '470400', '传媒', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (379, 207, '207', '510100', '综合', 2, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (380, 208, '36,208', '110101', '种子生产', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (381, 208, '36,208', '110102', '果蔬生产', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (382, 208, '36,208', '110103', '其他种植业', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (383, 211, '36,211', '110201', '海洋捕捞', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (384, 211, '36,211', '110202', '水产养殖', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (385, 213, '36,213', '110301', '林业', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (386, 214, '36,214', '110401', '饲料', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (387, 215, '36,215', '110501', '果蔬加工', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (388, 215, '36,215', '110502', '粮油加工', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (389, 215, '36,215', '110503', '畜禽加工', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (390, 218, '36,218', '110601', '业综合', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (391, 219, '47,219', '210101', '石油开采', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (392, 220, '47,220', '210201', '煤炭开采', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (393, 220, '47,220', '210202', '焦炭加工', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (394, 222, '47,222', '210301', '其他采掘', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (395, 223, '51,223', '220101', '石油加工', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (396, 223, '51,223', '220103', '石油贸易', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (397, 225, '51,225', '220201', '纯碱', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (398, 225, '51,225', '220202', '氯碱', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (399, 225, '51,225', '220203', '无机盐', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (400, 225, '51,225', '220204', '其他化学原料', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (401, 229, '51,229', '220301', '氮肥', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (402, 229, '51,229', '220302', '磷肥', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (403, 229, '51,229', '220303', '农药', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (404, 229, '51,229', '220304', '日用化学产品', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (405, 229, '51,229', '220305', '涂料油漆制造', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (406, 229, '51,229', '220306', '钾肥', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (407, 229, '51,229', '220307', '民爆用品', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (408, 229, '51,229', '220308', '纺织化学用品', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (409, 229, '51,229', '220309', '其他化学制品', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (410, 238, '51,238', '220401', '涤纶', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (411, 238, '51,238', '220402', '维纶', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (412, 238, '51,238', '220403', '粘胶', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (413, 238, '51,238', '220404', '其他纤维', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (414, 238, '51,238', '220405', '氨纶', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (415, 243, '51,243', '220501', '塑料', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (416, 244, '51,244', '220601', '轮胎', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (417, 244, '51,244', '220602', '其他橡胶制品', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (418, 246, '51,246', '220701', '化工新材料', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (419, 247, '75,247', '230101', '普钢', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (420, 247, '75,247', '230102', '特钢', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (421, 249, '77,249', '240102', '铝', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (422, 249, '77,249', '240103', '铜', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (423, 249, '77,249', '240104', '铅锌', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (424, 249, '77,249', '240105', '黄金', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (425, 249, '77,249', '240106', '小金属', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (426, 254, '77,254', '240201', '金属新材料', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (427, 254, '77,254', '240202', '磁性材料', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (428, 254, '77,254', '240203', '其他金属新材料', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (429, 257, '85,257', '250101', '玻璃制造', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (430, 257, '85,257', '250102', '水泥制造', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (431, 257, '85,257', '250103', '陶瓷制造', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (432, 257, '85,257', '250104', '其他建材', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (433, 261, '85,261', '250201', '建筑施工', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (434, 261, '85,261', '250202', '装修装饰', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (435, 263, '91,263', '260101', '机床工具', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (436, 263, '91,263', '260102', '机械基础件', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (437, 263, '91,263', '260103', '磨具磨料', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (438, 263, '91,263', '260104', '内燃机', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (439, 263, '91,263', '260105', '制冷空调设备', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (440, 263, '91,263', '260106', '其它普通机械', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (441, 269, '91,269', '260201', '纺织服装设备', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (442, 269, '91,269', '260202', '建筑机械', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (443, 269, '91,269', '260203', '农用机械', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (444, 269, '91,269', '260204', '重型机械', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (445, 269, '91,269', '260205', '冶金矿采设备', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (446, 269, '91,269', '260206', '印刷包装机械', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (447, 269, '91,269', '260207', '其它专用机械', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (448, 276, '91,276', '260301', '仪器仪表', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (449, 277, '91,277', '260401', '电机', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (450, 277, '91,277', '260402', '电气自控设备', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (451, 277, '91,277', '260403', '电源设备', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (452, 277, '91,277', '260404', '输变电设备', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (453, 277, '91,277', '260405', '其他电力设备', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (454, 282, '91,282', '260501', '金属制品', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (455, 283, '111,283', '270101', '集成电路', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (456, 283, '111,283', '270102', '分立器件', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (457, 283, '111,283', '270103', '半导体材料', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (458, 286, '111,286', '270201', '元件', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (459, 287, '111,287', '270301', '显示器件', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (460, 288, '111,288', '270401', '其他电子器件', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (461, 289, '117,289', '310101', '乘用车', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (462, 289, '117,289', '310102', '商用载货车', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (463, 289, '117,289', '310103', '商用载客车', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (464, 289, '117,289', '310104', '专用汽车', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (465, 293, '117,293', '310201', '汽车零部件', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (466, 294, '117,294', '310301', '摩托车', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (467, 294, '117,294', '310302', '农机设备', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (468, 294, '117,294', '310304', '航空航天设备', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (469, 294, '117,294', '310305', '船舶制造', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (470, 294, '117,294', '310306', '铁路设备', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (471, 299, '117,299', '310401', '汽车销售', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (472, 300, '128,300', '320101', '交换设备', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (473, 300, '128,300', '320102', '终端设备', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (474, 300, '128,300', '320103', '通信传输设备', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (475, 300, '128,300', '320104', '通信配套服务', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (476, 304, '128,304', '320201', '计算机设备', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (477, 305, '133,305', '330101', '冰箱', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (478, 305, '133,305', '330102', '空调', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (479, 305, '133,305', '330103', '洗衣机', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (480, 305, '133,305', '330104', '小家电', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (481, 305, '133,305', '330105', '其他白色家电', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (482, 310, '133,310', '330201', '彩电', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (483, 310, '133,310', '330202', '其它视听器材', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (484, 312, '140,312', '340101', '肉制品', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (485, 312, '140,312', '340102', '制糖', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (486, 312, '140,312', '340103', '食品综合', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (487, 315, '140,315', '340201', '调味发酵品', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (488, 315, '140,315', '340202', '乳品', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (489, 317, '140,317', '340301', '白酒', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (490, 317, '140,317', '340302', '啤酒', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (491, 317, '140,317', '340303', '其他酒类', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (492, 317, '140,317', '340304', '软饮料', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (493, 317, '140,317', '340305', '葡萄酒', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (494, 317, '140,317', '340306', '黄酒', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (495, 323, '151,323', '350101', '毛纺', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (496, 323, '151,323', '350102', '棉纺', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (497, 323, '151,323', '350103', '丝绸', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (498, 323, '151,323', '350104', '印染', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (499, 327, '151,327', '350201', '服装', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (500, 328, '156,328', '360101', '造纸', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (501, 329, '156,329', '360201', '包装印刷', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (502, 330, '156,330', '360301', '其他轻工制造', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (503, 330, '156,330', '360302', '家具', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (504, 330, '156,330', '360303', '家用轻工', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (505, 333, '161,333', '370101', '化学原料药', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (506, 333, '161,333', '370102', '化学制剂', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (507, 335, '161,335', '370201', '中药', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (508, 336, '161,336', '370301', '生物制品', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (509, 337, '161,337', '370401', '医药商业', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (510, 338, '161,338', '370501', '医疗器械', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (511, 339, '161,339', '370601', '医疗服务', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (512, 340, '168,340', '410101', '火电', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (513, 340, '168,340', '410102', '水电', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (514, 340, '168,340', '410103', '燃机发电', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (515, 340, '168,340', '410104', '热电', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (516, 344, '168,344', '410201', '水务', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (517, 345, '168,345', '410301', '燃气', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (518, 346, '168,346', '410401', '环保', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (519, 347, '175,347', '420101', '港口', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (520, 348, '175,348', '420201', '高速公路', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (521, 349, '175,349', '420301', '公交', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (522, 350, '175,350', '420401', '航空运输', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (523, 351, '175,351', '420501', '机场', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (524, 352, '175,352', '420601', '航运', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (525, 353, '175,353', '420701', '铁路运输', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (526, 354, '175,354', '420801', '物流', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (527, 355, '183,355', '430101', '房地产开发', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (528, 356, '183,356', '430201', '园区开发', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (529, 357, '185,357', '440101', '银行', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (530, 358, '185,358', '440201', '信托', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (531, 359, '185,359', '440301', '证券', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (532, 360, '185,360', '440401', '保险', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (533, 361, '189,361', '450101', '百货零售', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (534, 361, '189,361', '450102', '专业连锁', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (535, 361, '189,361', '450103', '商业物业经营', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (536, 364, '189,364', '450201', '贸易', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (537, 365, '193,365', '460101', '人工景点', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (538, 365, '193,365', '460102', '自然景点', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (539, 367, '193,367', '460201', '酒店', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (540, 368, '193,368', '460301', '旅游综合', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (541, 369, '193,369', '460401', '餐饮', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (542, 370, '198,370', '470101', '通信运营', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (543, 371, '198,371', '470201', '互联网信息服务', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (544, 371, '198,371', '470202', '宽带网络服务', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (545, 371, '198,371', '470203', '其它信息服务', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (546, 371, '198,371', '470204', '有线电视网络', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (547, 375, '198,375', '470302', '软件开发', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (548, 375, '198,375', '470303', '其它计算机应用', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (549, 377, '198,377', '470401', '出版', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (550, 377, '198,377', '470402', '广播电影电视', 3, 1, 0, 0, sysdate(), 0, sysdate());
INSERT INTO `industry_default` VALUES (551, 379, '207,379', '510101', '综合', 3, 1, 0, 0, sysdate(), 0, sysdate());

commit;