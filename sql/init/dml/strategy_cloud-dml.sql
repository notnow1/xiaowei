USE `strategy_cloud`;
SET NAMES utf8mb4;

-- init of industry_attraction
-- ----------------------------
INSERT INTO industry_attraction
( attraction_element_name, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( '成长性', 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction
( attraction_element_name, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( '盈利性', 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction
( attraction_element_name, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES('集中度', 1, 0, 1, sysdate(), 1, sysdate(), 0);



Set @industry_attraction_id1 = (SELECT industry_attraction_id FROM industry_attraction where attraction_element_name='成长性' and tenant_id = 0);
Set @industry_attraction_id2 = (SELECT industry_attraction_id FROM industry_attraction where attraction_element_name='盈利性' and tenant_id = 0);
Set @industry_attraction_id3 = (SELECT industry_attraction_id FROM industry_attraction where attraction_element_name='集中度' and tenant_id = 0);
-- init of industry_attraction_element
-- ----------------------------
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id1, '高潜行业', '总产值三年复合增长率达到GDP年均增长率的四倍（约为25-30%）', '#1890FF', 1, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id1, '价值行业', '总产值三年复合增长率达到GDP年均增长率的两倍（约为13-15%）', '#1890FF', 2, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id1, '稳健行业', '总产值三年复合增长率大于等于GDP年均增长率小于两倍', '#C0C4CC', 3, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id1, '谨慎行业', '总产值三年复合增长率小于GDP年均增长率', '#F94E4D', 4, 1, 0, 1, sysdate(), 1, sysdate(), 0);

INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id2, '超高利润行业', '平均毛利率超过80%', '#1890FF', 1, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id2, '高利润行业', '平均毛利率50%-80%', '#1890FF', 2, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id2, '平均利润行业', '平均毛利率30%-50%', '#C0C4CC', 3, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id2, '低利润行业', '平均毛利率低于30%', '#F94E4D', 4, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id3, '极高寡占型', 'CR8 ≥ 70%', '#F94E4D', 1, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id3, '低集中寡占型', '40% ≤ CR8 < 70%', '#1890FF', 2, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id3, '低集中竞争型', '20% ≤ CR8 < 40%', '#1890FF', 3, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id3, '分散竞争型', 'CR8 < 20%', '#C0C4CC', 4, 1, 0, 1, sysdate(), 1, sysdate(), 0);
