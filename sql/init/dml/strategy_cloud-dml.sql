USE `operate_cloud`;
SET NAMES utf8mb4;

-- ----------------------------
-- initialization data
-- ----------------------------

-- ----------------------------
-- init of industry_attraction
-- ----------------------------
INSERT INTO industry_attraction
(industry_attraction_id, attraction_element_name, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES(1, '成长性', 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction
(industry_attraction_id, attraction_element_name, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES(2, '盈利性', 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction
(industry_attraction_id, attraction_element_name, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES(3, '集中度', 1, 0, 1, sysdate(), 1, sysdate(), 0);
-- init of industry_attraction_element
-- ----------------------------
INSERT INTO industry_attraction_element
(industry_attraction_element_id, industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES(1, 1, '高潜行业', '总产值三年复合增长率达到GDP年均增长率的四倍（约为25-30%）', '蓝色', 1, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
(industry_attraction_element_id, industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES(2, 1, '价值行业', '总产值三年复合增长率达到GDP年均增长率的两倍（约为13-15%）', '蓝色', 2, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
(industry_attraction_element_id, industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES(3, 1, '稳健行业', '总产值三年复合增长率大于等于GDP年均增长率小于两倍', '灰色', 3, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
(industry_attraction_element_id, industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES(4, 1, '谨慎行业', '总产值三年复合增长率小于GDP年均增长率', '红色', 4, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
(industry_attraction_element_id, industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES(5, 2, '超高利润行业', '平均毛利率超过80%', '蓝色', 1, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
(industry_attraction_element_id, industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES(6, 2, '高利润行业', '平均毛利率50%-80%', '蓝色', 2, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
(industry_attraction_element_id, industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES(7, 2, '平均利润行业', '平均毛利率30%-50%', '灰色', 3, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
(industry_attraction_element_id, industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES(8, 2, '低利润行业', '平均毛利率低于30%', '红色', 4, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
(industry_attraction_element_id, industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES(9, 3, '极高寡占型', 'CR8 ≥ 70%', '红色', 1, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
(industry_attraction_element_id, industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES(10, 3, '低集中寡占型', '40% ≤ CR8<70%', '蓝色', 2, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
(industry_attraction_element_id, industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES(11, 3, '低集中竞争型', '20% ≤ CR8<40%', '蓝色', 3, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
(industry_attraction_element_id, industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES(12, 3, '分散竞争型', 'CR8<20%', '灰色', 4, 1, 0, 1, sysdate(), 1, sysdate(), 0);