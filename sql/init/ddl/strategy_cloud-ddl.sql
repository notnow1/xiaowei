DROP DATABASE IF EXISTS `strategy_cloud`;
CREATE DATABASE `strategy_cloud` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `strategy_cloud`;
SET NAMES utf8mb4;


CREATE TABLE plan_business_unit(
    plan_business_unit_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    business_unit_code VARCHAR(32)    COMMENT '规划业务单元编码' ,
    business_unit_name VARCHAR(64)    COMMENT '规划业务单元名称' ,
    business_unit_decompose VARCHAR(256)    COMMENT '规划业务单元维度(region,department,product,industry,company)' ,
    status TINYINT UNSIGNED NOT NULL   COMMENT '状态:0失效;1生效' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (plan_business_unit_id)
)  COMMENT = '规划业务单元';


CREATE TABLE industry_attraction(
    industry_attraction_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    attraction_element_name VARCHAR(64)    COMMENT '行业吸引力要素名称' ,
    status TINYINT UNSIGNED NOT NULL   COMMENT '状态:0失效;1生效' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (industry_attraction_id)
)  COMMENT = '行业吸引力表';


CREATE TABLE industry_attraction_element(
    industry_attraction_element_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    industry_attraction_id BIGINT UNSIGNED NOT NULL   COMMENT '行业吸引力ID' ,
    assess_standard_name VARCHAR(64)    COMMENT '评估标准名称' ,
    assess_standard_description VARCHAR(256)    COMMENT '评估标准说明' ,
    display_color VARCHAR(32)    COMMENT '显示颜色' ,
    font_color VARCHAR(32)    COMMENT '字体颜色' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    status TINYINT UNSIGNED NOT NULL   COMMENT '状态:0失效;1生效' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (industry_attraction_element_id)
)  COMMENT = '行业吸引力要素表';


CREATE TABLE field_config(
    field_config_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    business_type SMALLINT UNSIGNED NOT NULL   COMMENT '业务类型' ,
    field_name VARCHAR(128) NOT NULL   COMMENT '字段名称' ,
    field_label VARCHAR(256)    COMMENT '字段标签' ,
    field_type SMALLINT UNSIGNED    COMMENT '字段类型' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (field_config_id)
)  COMMENT = '字段配置表';


CREATE TABLE field_list_config(
    field_list_config_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    field_config_id BIGINT UNSIGNED NOT NULL   COMMENT '字段配置ID' ,
    user_id BIGINT UNSIGNED NOT NULL   COMMENT '用户ID' ,
    field_width SMALLINT UNSIGNED    COMMENT '字段宽度' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    show_flag TINYINT UNSIGNED    COMMENT '显示标记:0否;1是' ,
    fixation_flag TINYINT UNSIGNED    COMMENT '固定标记:0否;1是' ,
    show_force TINYINT UNSIGNED    COMMENT '强制显示:0否;1是' ,
    fixation_force TINYINT UNSIGNED    COMMENT '强制固定:0否;1是' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (field_list_config_id)
)  COMMENT = '字段列表配置表';


CREATE TABLE strategy_intent(
    strategy_intent_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    plan_year SMALLINT UNSIGNED NOT NULL   COMMENT '规划年度' ,
    vision VARCHAR(2048)    COMMENT '愿景' ,
    mission VARCHAR(2048)    COMMENT '使命' ,
    strategy_target VARCHAR(2048)    COMMENT '战略定位' ,
    strategy_position VARCHAR(2048)    COMMENT '战略目标' ,
    operate_plan_period TINYINT UNSIGNED    COMMENT '经营规划期' ,
    operate_history_year TINYINT UNSIGNED    COMMENT '经营历史年份' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (strategy_intent_id)
)  COMMENT = '战略意图表';


CREATE TABLE strategy_intent_operate(
    strategy_intent_operate_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    strategy_intent_id BIGINT UNSIGNED NOT NULL   COMMENT '战略意图ID' ,
    indicator_id BIGINT UNSIGNED    COMMENT '指标ID' ,
    operate_year SMALLINT UNSIGNED    COMMENT '经营年度' ,
    operate_value DECIMAL(14,2)    COMMENT '经营值' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (strategy_intent_operate_id)
)  COMMENT = '战略意图经营表';


CREATE TABLE gap_analysis(
    gap_analysis_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    plan_year SMALLINT UNSIGNED NOT NULL   COMMENT '规划年度' ,
    plan_business_unit_id BIGINT UNSIGNED NOT NULL   COMMENT '规划业务单元ID' ,
    business_unit_decompose VARCHAR(256)    COMMENT '规划业务单元维度(region,department,product,industry)' ,
    area_id BIGINT UNSIGNED    COMMENT '区域ID' ,
    department_id BIGINT UNSIGNED    COMMENT '部门ID' ,
    product_id BIGINT UNSIGNED    COMMENT '产品ID' ,
    industry_id BIGINT UNSIGNED    COMMENT '行业ID' ,
    operate_history_year TINYINT UNSIGNED    COMMENT '经营历史年份' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (gap_analysis_id)
)  COMMENT = '差距分析表';


CREATE TABLE gap_analysis_performance(
    gap_analysis_performance_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    gap_analysis_id BIGINT UNSIGNED NOT NULL   COMMENT '差距分析ID' ,
    serial_number SMALLINT UNSIGNED    COMMENT '序列号' ,
    gap_performance_name VARCHAR(128)    COMMENT '业绩差距名称' ,
    gap_description VARCHAR(2048)    COMMENT '差距描述' ,
    root_cause_analysis VARCHAR(2048)    COMMENT '根因分析' ,
    root_cause_category VARCHAR(128)    COMMENT '根因类别' ,
    root_cause_subtype VARCHAR(128)    COMMENT '根因子类' ,
    recommended_practice VARCHAR(2048)    COMMENT '建议措施' ,
    propose_employee_id BIGINT UNSIGNED    COMMENT '提出人员ID' ,
    propose_employee_name VARCHAR(64)    COMMENT '提出人员姓名' ,
    propose_employee_code VARCHAR(32)    COMMENT '提出人员编码' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (gap_analysis_performance_id)
)  COMMENT = '业绩差距表';


CREATE TABLE gap_analysis_opportunity(
    gap_analysis_opportunity_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    gap_analysis_id BIGINT UNSIGNED NOT NULL   COMMENT '差距分析ID' ,
    serial_number SMALLINT UNSIGNED    COMMENT '序列号' ,
    gap_performance_name VARCHAR(128)    COMMENT '业绩差距名称' ,
    gap_description VARCHAR(2048)    COMMENT '差距描述' ,
    root_cause_analysis VARCHAR(2048)    COMMENT '根因分析' ,
    root_cause_category VARCHAR(128)    COMMENT '根因类别' ,
    root_cause_subtype VARCHAR(128)    COMMENT '根因子类' ,
    recommended_practice VARCHAR(2048)    COMMENT '建议措施' ,
    propose_employee_id BIGINT UNSIGNED    COMMENT '提出人员ID' ,
    propose_employee_name VARCHAR(64)    COMMENT '提出人员姓名' ,
    propose_employee_code VARCHAR(32)    COMMENT '提出人员编码' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (gap_analysis_opportunity_id)
)  COMMENT = '机会差距表';


CREATE TABLE gap_analysis_operate(
    gap_analysis_operate_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    gap_analysis_id BIGINT UNSIGNED NOT NULL   COMMENT '差距分析ID' ,
    indicator_id BIGINT UNSIGNED    COMMENT '指标ID' ,
    operate_year SMALLINT UNSIGNED    COMMENT '经营年度' ,
    target_value DECIMAL(14,2)    COMMENT '目标值' ,
    actual_value DECIMAL(14,2)    COMMENT '实际值' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (gap_analysis_operate_id)
)  COMMENT = '差距分析经营情况表';


CREATE TABLE market_insight_macro(
    market_insight_macro_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    plan_year SMALLINT UNSIGNED NOT NULL   COMMENT '规划年度' ,
    plan_business_unit_id BIGINT UNSIGNED NOT NULL   COMMENT '规划业务单元ID' ,
    business_unit_decompose VARCHAR(256)    COMMENT '规划业务单元维度(region,department,product,industry)' ,
    area_id BIGINT UNSIGNED    COMMENT '区域ID' ,
    department_id BIGINT UNSIGNED    COMMENT '部门ID' ,
    product_id BIGINT UNSIGNED    COMMENT '产品ID' ,
    industry_id BIGINT UNSIGNED    COMMENT '行业ID' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (market_insight_macro_id)
)  COMMENT = '市场洞察宏观表';


CREATE TABLE mi_macro_detail(
    mi_macro_detail_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    market_insight_macro_id BIGINT UNSIGNED NOT NULL   COMMENT '市场洞察宏观ID' ,
    visual_angle BIGINT UNSIGNED NOT NULL   COMMENT '视角' ,
    company_related_factor VARCHAR(1024)    COMMENT '企业相关因素' ,
    change_trend VARCHAR(1024)    COMMENT '变化及趋势' ,
    influence_description VARCHAR(1024)    COMMENT '影响描述' ,
    recommended_practice VARCHAR(1024)    COMMENT '建议措施' ,
    plan_period TINYINT UNSIGNED    COMMENT '规划期' ,
    estimate_opportunity_amount DECIMAL(14,2)    COMMENT '预估机会点金额' ,
    propose_employee_id BIGINT UNSIGNED    COMMENT '提出人员ID' ,
    propose_employee_name VARCHAR(64)    COMMENT '提出人员姓名' ,
    propose_employee_code VARCHAR(32)    COMMENT '提出人员编码' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (mi_macro_detail_id)
)  COMMENT = '市场洞察宏观详情表';


CREATE TABLE mi_macro_estimate(
    mi_macro_estimate_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    market_insight_macro_id BIGINT UNSIGNED NOT NULL   COMMENT '市场洞察宏观ID' ,
    mi_macro_detail_id BIGINT UNSIGNED NOT NULL   COMMENT '市场洞察宏观详情ID' ,
    plan_year SMALLINT UNSIGNED    COMMENT '规划年度' ,
    estimate_opportunity_amount DECIMAL(14,2)    COMMENT '预估机会点金额' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (mi_macro_estimate_id)
)  COMMENT = '市场洞察宏观预估表';


CREATE TABLE business_design(
    business_design_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    plan_year SMALLINT UNSIGNED NOT NULL   COMMENT '规划年度' ,
    plan_business_unit_id BIGINT UNSIGNED NOT NULL   COMMENT '规划业务单元ID' ,
    business_unit_decompose VARCHAR(256)    COMMENT '规划业务单元维度(region,department,product,industry)' ,
    area_id BIGINT UNSIGNED    COMMENT '区域ID' ,
    department_id BIGINT UNSIGNED    COMMENT '部门ID' ,
    product_id BIGINT UNSIGNED    COMMENT '产品ID' ,
    industry_id BIGINT UNSIGNED    COMMENT '行业ID' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (business_design_id)
)  COMMENT = '业务设计表';


CREATE TABLE business_design_param(
    business_design_param_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    business_design_id BIGINT UNSIGNED NOT NULL   COMMENT '业务设计ID' ,
    param_dimension TINYINT UNSIGNED NOT NULL   COMMENT '参数维度:1产品;2客户;3区域' ,
    param_relation_id BIGINT UNSIGNED    COMMENT '参数关联ID' ,
    param_name VARCHAR(128)    COMMENT '参数名称' ,
    history_average_rate DECIMAL(14,2)    COMMENT '历史平均毛利率' ,
    history_weight DECIMAL(5,2)    COMMENT '历史权重' ,
    forecast_rate DECIMAL(14,2)    COMMENT '预测毛利率' ,
    forecast_weight DECIMAL(5,2)    COMMENT '预测权重' ,
    history_order_amount DECIMAL(14,2)    COMMENT '历史订单额' ,
    history_order_weight DECIMAL(5,2)    COMMENT '历史订单权重' ,
    forecast_order_amount DECIMAL(14,2)    COMMENT '预测订单额' ,
    forecast_order_weight DECIMAL(5,2)    COMMENT '预测订单权重' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (business_design_param_id)
)  COMMENT = '业务设计参数表';


CREATE TABLE business_design_axis_config(
    business_design_axis_config_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    business_design_id BIGINT UNSIGNED NOT NULL   COMMENT '业务设计ID' ,
    param_dimension TINYINT UNSIGNED NOT NULL   COMMENT '参数维度:1产品;2客户;3区域' ,
    coordinate_axis TINYINT UNSIGNED NOT NULL   COMMENT '坐标轴:1 x轴;2 y轴' ,
    upper_value DECIMAL(14,2)    COMMENT '高区值' ,
    lower_value DECIMAL(14,2)    COMMENT '低区值' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (business_design_axis_config_id)
)  COMMENT = '业务设计轴配置表';


CREATE TABLE market_insight_industry(
    market_insight_industry_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    plan_year SMALLINT UNSIGNED NOT NULL   COMMENT '规划年度' ,
    plan_business_unit_id BIGINT UNSIGNED NOT NULL   COMMENT '规划业务单元ID' ,
    business_unit_decompose VARCHAR(256)    COMMENT '规划业务单元维度(region,department,product,industry)' ,
    area_id BIGINT UNSIGNED    COMMENT '区域ID' ,
    department_id BIGINT UNSIGNED    COMMENT '部门ID' ,
    product_id BIGINT UNSIGNED    COMMENT '产品ID' ,
    industry_id BIGINT UNSIGNED    COMMENT '行业ID' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (market_insight_industry_id)
)  COMMENT = '市场洞察行业表';


CREATE TABLE mi_industry_detail(
    mi_industry_detail_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    market_insight_industry_id BIGINT UNSIGNED NOT NULL   COMMENT '市场洞察行业ID' ,
    industry_id BIGINT UNSIGNED NOT NULL   COMMENT '行业ID' ,
    industry_type BIGINT UNSIGNED    COMMENT '行业类型' ,
    plan_period TINYINT UNSIGNED    COMMENT '规划期' ,
    overall_space DECIMAL(14,2)    COMMENT '整体空间' ,
    participate_space DECIMAL(14,2)    COMMENT '可参与空间' ,
    target_market_space DECIMAL(14,2)    COMMENT '目标市场空间' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (mi_industry_detail_id)
)  COMMENT = '市场洞察行业详情表';


CREATE TABLE mi_industry_estimate(
    mi_macro_estimate_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    market_insight_industry_id BIGINT UNSIGNED NOT NULL   COMMENT '市场洞察行业ID' ,
    mi_industry_detail_id BIGINT UNSIGNED NOT NULL   COMMENT '市场洞察行业详情ID' ,
    plan_year SMALLINT UNSIGNED    COMMENT '规划年度' ,
    overall_space_amount DECIMAL(14,2)    COMMENT '整体空间金额' ,
    participate_space_amount DECIMAL(14,2)    COMMENT '可参与空间金额' ,
    target_market_space_amount DECIMAL(14,2)    COMMENT '目标市场空间金额' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (mi_macro_estimate_id)
)  COMMENT = '市场洞察行业预估表';


CREATE TABLE mi_industry_attraction(
    mi_industry_attraction_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    market_insight_industry_id BIGINT UNSIGNED NOT NULL   COMMENT '市场洞察行业ID' ,
    industry_attraction_id BIGINT UNSIGNED NOT NULL   COMMENT '行业吸引力ID' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (mi_industry_attraction_id)
)  COMMENT = '市场洞察行业吸引力表';


CREATE TABLE mi_industry_attraction_data(
    mi_industry_attraction_data_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    market_insight_industry_id BIGINT UNSIGNED NOT NULL   COMMENT '市场洞察行业ID' ,
    mi_industry_detail_id BIGINT UNSIGNED NOT NULL   COMMENT '市场洞察行业详情ID' ,
    mi_industry_attraction_id BIGINT UNSIGNED NOT NULL   COMMENT '市场洞察行业吸引力ID' ,
    industry_attraction_element_id BIGINT UNSIGNED    COMMENT '行业吸引力要素ID' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (mi_industry_attraction_data_id)
)  COMMENT = '市场洞察行业吸引力数据表';


CREATE TABLE market_insight_customer(
    market_insight_customer_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    plan_year SMALLINT UNSIGNED NOT NULL   COMMENT '规划年度' ,
    plan_business_unit_id BIGINT UNSIGNED NOT NULL   COMMENT '规划业务单元ID' ,
    business_unit_decompose VARCHAR(256)    COMMENT '规划业务单元维度(region,department,product,industry)' ,
    area_id BIGINT UNSIGNED    COMMENT '区域ID' ,
    department_id BIGINT UNSIGNED    COMMENT '部门ID' ,
    product_id BIGINT UNSIGNED    COMMENT '产品ID' ,
    industry_id BIGINT UNSIGNED    COMMENT '行业ID' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (market_insight_customer_id)
)  COMMENT = '市场洞察客户表';


CREATE TABLE mi_customer_choice(
    mi_customer_choice_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    market_insight_customer_id BIGINT UNSIGNED NOT NULL   COMMENT '市场洞察客户ID' ,
    industry_id BIGINT UNSIGNED    COMMENT '行业ID' ,
    customer_name VARCHAR(128)    COMMENT '客户名称' ,
    admission_flag TINYINT UNSIGNED    COMMENT '准入标记:0否;1是' ,
    customer_category BIGINT UNSIGNED    COMMENT '客户类别' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (mi_customer_choice_id)
)  COMMENT = '市场洞察客户选择表';



CREATE TABLE mi_customer_invest_plan(
    mi_customer_invest_plan_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    market_insight_customer_id BIGINT UNSIGNED NOT NULL   COMMENT '市场洞察客户ID' ,
    industry_id BIGINT UNSIGNED    COMMENT '行业ID' ,
    customer_name VARCHAR(128)    COMMENT '客户名称' ,
    customer_category BIGINT UNSIGNED    COMMENT '客户类别' ,
    exist_market_share DECIMAL(5,2)    COMMENT '现有市场占有率' ,
    previous_year_sales DECIMAL(14,2)    COMMENT '上年销售额' ,
    plan_period TINYINT UNSIGNED    COMMENT '规划期' ,
    future_part_market_space DECIMAL(14,2)    COMMENT '未来可参与市场空间' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (mi_customer_invest_plan_id)
)  COMMENT = '市场洞察客户投资计划表';


CREATE TABLE mi_customer_invest_detail(
    mi_customer_invest_detail_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    market_insight_customer_id BIGINT UNSIGNED NOT NULL   COMMENT '市场洞察客户ID' ,
    mi_customer_invest_plan_id BIGINT UNSIGNED NOT NULL   COMMENT '市场洞察客户投资计划ID' ,
    plan_year SMALLINT UNSIGNED NOT NULL   COMMENT '规划年度' ,
    product_id BIGINT UNSIGNED    COMMENT '产品ID' ,
    total_annual_demand VARCHAR(32)    COMMENT '年需求总量' ,
    customer_invest_plan_amount DECIMAL(14,2)    COMMENT '客户投资计划金额' ,
    estimate_market_share DECIMAL(5,2)    COMMENT '预计市场占有率' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (mi_customer_invest_detail_id)
)  COMMENT = '市场洞察客户投资详情表';


CREATE TABLE market_insight_opponent(
    market_insight_opponent_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    plan_year SMALLINT UNSIGNED NOT NULL   COMMENT '规划年度' ,
    plan_business_unit_id BIGINT UNSIGNED NOT NULL   COMMENT '规划业务单元ID' ,
    business_unit_decompose VARCHAR(256)    COMMENT '规划业务单元维度(region,department,product,industry)' ,
    area_id BIGINT UNSIGNED    COMMENT '区域ID' ,
    department_id BIGINT UNSIGNED    COMMENT '部门ID' ,
    product_id BIGINT UNSIGNED    COMMENT '产品ID' ,
    industry_id BIGINT UNSIGNED    COMMENT '行业ID' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (market_insight_opponent_id)
)  COMMENT = '市场洞察对手表';


CREATE TABLE mi_opponent_choice(
    mi_opponent_choice_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    market_insight_opponent_id BIGINT UNSIGNED NOT NULL   COMMENT '市场洞察对手ID' ,
    industry_id BIGINT UNSIGNED    COMMENT '行业ID' ,
    opponent_name VARCHAR(128)    COMMENT '对手名称' ,
    comparison_item BIGINT UNSIGNED    COMMENT '对比项目' ,
    ability_assess_score DECIMAL(5,2)    COMMENT '能力评估分数' ,
    analysis_opponent_core_ability VARCHAR(1024)    COMMENT '对手核心能力分析' ,
    own_advantage VARCHAR(1024)    COMMENT '自身优势' ,
    own_disadvantage VARCHAR(1024)    COMMENT '自身劣势' ,
    competitor_category BIGINT UNSIGNED    COMMENT '竞争对手类别' ,
    competition_strategy_type BIGINT UNSIGNED    COMMENT '竞争战略类型' ,
    operate_history_period TINYINT UNSIGNED    COMMENT '经营历史期' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (mi_opponent_choice_id)
)  COMMENT = '市场洞察对手选择表';


CREATE TABLE mi_opponent_finance(
    mi_opponent_finance_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    market_insight_opponent_id BIGINT UNSIGNED NOT NULL   COMMENT '市场洞察对手ID' ,
    mi_opponent_choice_id BIGINT UNSIGNED NOT NULL   COMMENT '市场洞察对手选择ID' ,
    indicator_id BIGINT UNSIGNED NOT NULL   COMMENT '指标ID' ,
    operate_year SMALLINT UNSIGNED    COMMENT '经营年度' ,
    operate_value DECIMAL(14,2)    COMMENT '经营值' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (mi_opponent_finance_id)
)  COMMENT = '市场洞察对手财务表';


CREATE TABLE market_insight_self(
    market_insight_self_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    plan_year SMALLINT UNSIGNED NOT NULL   COMMENT '规划年度' ,
    plan_business_unit_id BIGINT UNSIGNED NOT NULL   COMMENT '规划业务单元ID' ,
    business_unit_decompose VARCHAR(256)    COMMENT '规划业务单元维度(region,department,product,industry)' ,
    area_id BIGINT UNSIGNED    COMMENT '区域ID' ,
    department_id BIGINT UNSIGNED    COMMENT '部门ID' ,
    product_id BIGINT UNSIGNED    COMMENT '产品ID' ,
    industry_id BIGINT UNSIGNED    COMMENT '行业ID' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (market_insight_self_id)
)  COMMENT = '市场洞察自身表';


CREATE TABLE mi_self_ability_access(
    mi_self_ability_access_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    market_insight_self_id BIGINT UNSIGNED NOT NULL   COMMENT '市场洞察自身ID' ,
    capacity_factor BIGINT UNSIGNED    COMMENT '能力要素' ,
    description_actuality VARCHAR(2048)    COMMENT '现状描述' ,
    ability_assess_score DECIMAL(5,2)    COMMENT '能力评估分数' ,
    strategy_control_point_flag TINYINT UNSIGNED    COMMENT '战略控制点标记:0否;1是' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (mi_self_ability_access_id)
)  COMMENT = '市场洞察自身能力评估表';


CREATE TABLE strategy_index_dimension(
    strategy_index_dimension_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    parent_index_dimension_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '父级战略指标维度ID' ,
    ancestors VARCHAR(256)   DEFAULT '' COMMENT '祖级列表ID，按层级用英文逗号隔开' ,
    index_dimension_code VARCHAR(32)    COMMENT '战略指标维度编码' ,
    index_dimension_name VARCHAR(64)    COMMENT '战略指标维度名称' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    level TINYINT UNSIGNED    COMMENT '层级' ,
    status TINYINT UNSIGNED NOT NULL   COMMENT '状态:0失效;1生效' ,
    remark VARCHAR(256)    COMMENT '备注' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (strategy_index_dimension_id)
)  COMMENT = '战略指标维度表';


CREATE TABLE strategy_measure(
    strategy_measure_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    plan_year SMALLINT UNSIGNED NOT NULL   COMMENT '规划年度' ,
    plan_business_unit_id BIGINT UNSIGNED NOT NULL   COMMENT '规划业务单元ID' ,
    business_unit_decompose VARCHAR(256)    COMMENT '规划业务单元维度(region,department,product,industry)' ,
    area_id BIGINT UNSIGNED    COMMENT '区域ID' ,
    department_id BIGINT UNSIGNED    COMMENT '部门ID' ,
    product_id BIGINT UNSIGNED    COMMENT '产品ID' ,
    industry_id BIGINT UNSIGNED    COMMENT '行业ID' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (strategy_measure_id)
)  COMMENT = '战略举措清单表';


CREATE TABLE strategy_measure_detail(
    strategy_measure_detail_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    strategy_measure_id BIGINT UNSIGNED NOT NULL   COMMENT '战略举措清单ID' ,
    strategy_index_dimension_id BIGINT UNSIGNED NOT NULL   COMMENT '战略指标维度ID' ,
    serial_number SMALLINT UNSIGNED NOT NULL   COMMENT '序列号' ,
    strategy_measure_name VARCHAR(128)    COMMENT '战略举措名称' ,
    strategy_measure_source BIGINT UNSIGNED    COMMENT '战略举措来源' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (strategy_measure_detail_id)
)  COMMENT = '战略举措清单详情表';


CREATE TABLE strategy_measure_task(
    strategy_measure_task_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    strategy_measure_id BIGINT UNSIGNED NOT NULL   COMMENT '战略举措清单ID' ,
    strategy_measure_detail_id BIGINT UNSIGNED NOT NULL   COMMENT '战略举措清单详情ID' ,
    key_task VARCHAR(1024)    COMMENT '关键任务' ,
    close_standard VARCHAR(1024)    COMMENT '闭环标准' ,
    duty_department_id BIGINT UNSIGNED    COMMENT '责任部门' ,
    duty_employee_id BIGINT UNSIGNED    COMMENT '责任人员ID' ,
    duty_employee_name VARCHAR(64)    COMMENT '责任人员姓名' ,
    duty_employee_code VARCHAR(32)    COMMENT '责任人员编码' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (strategy_measure_task_id)
)  COMMENT = '战略举措清单任务表';


CREATE TABLE strategy_metrics(
    strategy_metrics_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    strategy_measure_id BIGINT UNSIGNED NOT NULL   COMMENT '战略举措清单ID' ,
    plan_year SMALLINT UNSIGNED NOT NULL   COMMENT '规划年度' ,
    plan_business_unit_id BIGINT UNSIGNED NOT NULL   COMMENT '规划业务单元ID' ,
    business_unit_decompose VARCHAR(256)    COMMENT '规划业务单元维度(region,department,product,industry)' ,
    area_id BIGINT UNSIGNED    COMMENT '区域ID' ,
    department_id BIGINT UNSIGNED    COMMENT '部门ID' ,
    product_id BIGINT UNSIGNED    COMMENT '产品ID' ,
    industry_id BIGINT UNSIGNED    COMMENT '行业ID' ,
    plan_period TINYINT UNSIGNED    COMMENT '规划期' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (strategy_metrics_id)
)  COMMENT = '战略衡量指标表';


CREATE TABLE strategy_metrics_detail(
    strategy_metrics_detail_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    strategy_metrics_id BIGINT UNSIGNED NOT NULL   COMMENT '战略衡量指标ID' ,
    strategy_index_dimension_id BIGINT UNSIGNED NOT NULL   COMMENT '战略指标维度ID' ,
    serial_number SMALLINT UNSIGNED NOT NULL   COMMENT '序列号' ,
    strategy_measure_name VARCHAR(128)    COMMENT '战略举措名称' ,
    indicator_id BIGINT UNSIGNED    COMMENT '指标ID' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (strategy_metrics_detail_id)
)  COMMENT = '战略衡量指标详情表';


CREATE TABLE strategy_metrics_plan(
    strategy_metrics_plan_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    strategy_metrics_id BIGINT UNSIGNED NOT NULL   COMMENT '战略衡量指标ID' ,
    strategy_metrics_detail_id BIGINT UNSIGNED NOT NULL   COMMENT '战略衡量指标详情ID' ,
    plan_year SMALLINT UNSIGNED NOT NULL   COMMENT '规划年度' ,
    plan_value DECIMAL(14,2)    COMMENT '规划值' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (strategy_metrics_plan_id)
)  COMMENT = '战略衡量指标规划表';


CREATE TABLE annual_key_work(
    annual_key_work_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    plan_year SMALLINT UNSIGNED NOT NULL   COMMENT '规划年度' ,
    plan_business_unit_id BIGINT UNSIGNED NOT NULL   COMMENT '规划业务单元ID' ,
    business_unit_decompose VARCHAR(256)    COMMENT '规划业务单元维度(region,department,product,industry)' ,
    area_id BIGINT UNSIGNED    COMMENT '区域ID' ,
    department_id BIGINT UNSIGNED    COMMENT '部门ID' ,
    product_id BIGINT UNSIGNED    COMMENT '产品ID' ,
    industry_id BIGINT UNSIGNED    COMMENT '行业ID' ,
    plan_rank TINYINT UNSIGNED NOT NULL   COMMENT '规划级别:1部门;2公司' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (annual_key_work_id)
)  COMMENT = '年度重点工作表';


CREATE TABLE annual_key_work_detail(
    annual_key_work_detail_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    annual_key_work_id BIGINT UNSIGNED NOT NULL   COMMENT '年度重点工作ID' ,
    task_number VARCHAR(128)    COMMENT '任务编号' ,
    task_name VARCHAR(128)    COMMENT '任务名称' ,
    department_id BIGINT UNSIGNED    COMMENT '所属部门' ,
    department_employee_id BIGINT UNSIGNED    COMMENT '部门主管' ,
    task_description VARCHAR(1024)    COMMENT '任务描述' ,
    close_standard VARCHAR(1024)    COMMENT '闭环标准' ,
    task_start_time DATE    COMMENT '任务开始时间' ,
    task_end_time DATE    COMMENT '任务结束时间' ,
    duty_employee_id BIGINT UNSIGNED    COMMENT '责任人' ,
    duty_employee_name VARCHAR(64)    COMMENT '责任人员姓名' ,
    duty_employee_code VARCHAR(32)    COMMENT '责任人员编码' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (annual_key_work_detail_id)
)  COMMENT = '年度重点工作详情表';