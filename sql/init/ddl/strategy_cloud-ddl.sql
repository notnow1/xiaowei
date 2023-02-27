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
    gap_performance_name VARCHAR(64)    COMMENT '业绩差距名称' ,
    gap_description VARCHAR(512)    COMMENT '差距描述' ,
    root_cause_analysis VARCHAR(512)    COMMENT '根因分析' ,
    root_cause_category VARCHAR(64)    COMMENT '根因类别' ,
    root_cause_subtype VARCHAR(64)    COMMENT '根因子类' ,
    recommended_practice VARCHAR(512)    COMMENT '建议措施' ,
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
    gap_performance_name VARCHAR(64)    COMMENT '业绩差距名称' ,
    gap_description VARCHAR(512)    COMMENT '差距描述' ,
    root_cause_analysis VARCHAR(512)    COMMENT '根因分析' ,
    root_cause_category VARCHAR(64)    COMMENT '根因类别' ,
    root_cause_subtype VARCHAR(64)    COMMENT '根因子类' ,
    recommended_practice VARCHAR(512)    COMMENT '建议措施' ,
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
    indicator_id BIGINT UNSIGNED NOT NULL   COMMENT '指标ID' ,
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
    company_related_factor VARCHAR(256)    COMMENT '企业相关因素' ,
    change_trend VARCHAR(256)    COMMENT '变化及趋势' ,
    influence_description VARCHAR(512)    COMMENT '影响描述' ,
    recommended_practice VARCHAR(512)    COMMENT '建议措施' ,
    plan_period TINYINT UNSIGNED    COMMENT '规划期' ,
    estimate_opportunity_amount DECIMAL(14,2)    COMMENT '预估机会点金额' ,
    propose_employee_id BIGINT UNSIGNED    COMMENT '提出人员ID' ,
    propose_employee_name VARCHAR(64)    COMMENT '提出人员姓名' ,
    propose_employee_code VARCHAR(32)    COMMENT '提出人员编码' ,
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
