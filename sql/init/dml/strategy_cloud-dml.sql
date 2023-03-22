USE strategy_cloud;
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
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color,font_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id1, '高潜行业', '总产值三年复合增长率达到GDP年均增长率的四倍（约为25-30%）', '#DEEBFF','#0052CC', 1, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color,font_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id1, '价值行业', '总产值三年复合增长率达到GDP年均增长率的两倍（约为13-15%）', '#DEEBFF','#0052CC', 2, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color,font_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id1, '稳健行业', '总产值三年复合增长率大于等于GDP年均增长率小于两倍', '#DFE1E6','#182B4E', 3, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color,font_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id1, '谨慎行业', '总产值三年复合增长率小于GDP年均增长率', '#FFEBE6','#BF2600', 4, 1, 0, 1, sysdate(), 1, sysdate(), 0);

INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color,font_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id2, '超高利润行业', '平均毛利率超过80%', '#DEEBFF','#0052CC', 1, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color,font_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id2, '高利润行业', '平均毛利率50%-80%', '#DEEBFF','#0052CC', 2, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color,font_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id2, '平均利润行业', '平均毛利率30%-50%', '#DFE1E6','#182B4E', 3, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color,font_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id2, '低利润行业', '平均毛利率低于30%', '#FFEBE6','#BF2600', 4, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color,font_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id3, '极高寡占型', 'CR8 ≥ 70%', '#FFEBE6','#BF2600', 1, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color,font_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id3, '低集中寡占型', '40% ≤ CR8 < 70%', '#DEEBFF','#0052CC', 2, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color,font_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id3, '低集中竞争型', '20% ≤ CR8 < 40%', '#DEEBFF','#0052CC', 3, 1, 0, 1, sysdate(), 1, sysdate(), 0);
INSERT INTO industry_attraction_element
( industry_attraction_id, assess_standard_name, assess_standard_description, display_color,font_color, sort, status, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES( @industry_attraction_id3, '分散竞争型', 'CR8 < 20%', '#DFE1E6','#182B4E', 4, 1, 0, 1, sysdate(), 1, sysdate(), 0);

INSERT INTO strategy_index_dimension
( parent_index_dimension_id, ancestors, index_dimension_code, index_dimension_name, sort, `level`, status, remark, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES (0, '', 'F', '财务层面', 1, 1, 1, NULL, 0, 0, sysdate(), 1, sysdate(), 1);
INSERT INTO strategy_index_dimension
( parent_index_dimension_id, ancestors, index_dimension_code, index_dimension_name, sort, `level`, status, remark, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES (0, '', 'C', '客户层面', 2, 1, 1, NULL, 0, 0, sysdate(), 1, sysdate(), 1);
INSERT INTO strategy_index_dimension
( parent_index_dimension_id, ancestors, index_dimension_code, index_dimension_name, sort, `level`, status, remark, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES (0, '', 'I', '内部经营层面', 3, 1, 1, NULL, 0, 0, sysdate(), 1, sysdate(), 1);
INSERT INTO strategy_index_dimension
( parent_index_dimension_id, ancestors, index_dimension_code, index_dimension_name, sort, `level`, status, remark, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES (0, '', 'L', '学习与成长层面', 4, 1, 1, NULL, 0, 0, sysdate(), 1, sysdate(), 1);

Set @strategy_index_dimension_id1 = (SELECT strategy_index_dimension_id FROM strategy_index_dimension where index_dimension_name='内部经营层面' and tenant_id = 0);
Set @strategy_index_dimension_id2 = (SELECT strategy_index_dimension_id FROM strategy_index_dimension where index_dimension_name='学习与成长层面' and tenant_id = 0);

INSERT INTO strategy_index_dimension
( parent_index_dimension_id, ancestors, index_dimension_code, index_dimension_name, sort, `level`, status, remark, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES (@strategy_index_dimension_id1, @strategy_index_dimension_id1, 'I', '客户管理', 1, 2, 1, NULL, 0, 0, sysdate(), 1, sysdate(), 1);
INSERT INTO strategy_index_dimension
( parent_index_dimension_id, ancestors, index_dimension_code, index_dimension_name, sort, `level`, status, remark, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES (@strategy_index_dimension_id1, @strategy_index_dimension_id1, 'I', '产品创新', 2, 2, 1, NULL, 0, 0, sysdate(), 1, sysdate(), 1);
INSERT INTO strategy_index_dimension
( parent_index_dimension_id, ancestors, index_dimension_code, index_dimension_name, sort, `level`, status, remark, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES (@strategy_index_dimension_id1, @strategy_index_dimension_id1, 'I', '运营管理', 3, 2, 1, NULL, 0, 0, sysdate(), 1, sysdate(), 1);
INSERT INTO strategy_index_dimension
( parent_index_dimension_id, ancestors, index_dimension_code, index_dimension_name, sort, `level`, status, remark, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES (@strategy_index_dimension_id1, @strategy_index_dimension_id1, 'I', '负责的公民', 4, 2, 1, NULL, 0, 0, sysdate(), 1, sysdate(), 1);
INSERT INTO strategy_index_dimension
( parent_index_dimension_id, ancestors, index_dimension_code, index_dimension_name, sort, `level`, status, remark, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES (@strategy_index_dimension_id2, @strategy_index_dimension_id2, 'L', '人力资本', 1, 2, 1, NULL, 0, 0, sysdate(), 1, sysdate(), 1);
INSERT INTO strategy_index_dimension
( parent_index_dimension_id, ancestors, index_dimension_code, index_dimension_name, sort, `level`, status, remark, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES (@strategy_index_dimension_id2, @strategy_index_dimension_id2, 'L', '信息资本', 2, 2, 1, NULL, 0, 0, sysdate(), 1, sysdate(), 1);
INSERT INTO strategy_index_dimension
( parent_index_dimension_id, ancestors, index_dimension_code, index_dimension_name, sort, `level`, status, remark, delete_flag, create_by, create_time, update_by, update_time, tenant_id)
VALUES (@strategy_index_dimension_id2, @strategy_index_dimension_id2, 'L', '组织资本', 3, 2, 1, NULL, 0, 0, sysdate(), 1, sysdate(), 1);