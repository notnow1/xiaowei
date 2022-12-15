DROP DATABASE IF EXISTS `operate_cloud`;
CREATE DATABASE `operate_cloud` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `operate_cloud`;
SET NAMES utf8mb4;

CREATE TABLE area(
    area_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    area_code VARCHAR(32) NOT NULL   COMMENT '区域编码' ,
    area_name VARCHAR(64)    COMMENT '区域名称' ,
    region_ids VARCHAR(2048)    COMMENT '地区ID集合,用英文逗号隔开' ,
    region_names VARCHAR(2048)    COMMENT '地区名称集合,用英文逗号隔开' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (area_id)
)  COMMENT = '区域表';


CREATE TABLE target_decompose_dimension(
    target_decompose_dimension_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    decomposition_dimension VARCHAR(256) NOT NULL   COMMENT '分解维度(region,salesman,department,product,province,industry)' ,
    sort TINYINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (target_decompose_dimension_id)
)  COMMENT = '目标分解维度表';


CREATE TABLE performance_rank(
    performance_rank_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    performance_rank_category TINYINT UNSIGNED NOT NULL   COMMENT '绩效等级类别:1组织;2个人' ,
    performance_rank_name VARCHAR(64)    COMMENT '绩效等级名称' ,
    performance_rank_description VARCHAR(256)    COMMENT '绩效等级描述' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (performance_rank_id)
)  COMMENT = '绩效等级表';


CREATE TABLE performance_rank_factor(
    performance_rank_factor_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    performance_rank_id BIGINT UNSIGNED NOT NULL   COMMENT '绩效等级ID' ,
    performance_rank_name VARCHAR(32)    COMMENT '绩效等级名称' ,
    bonus_factor DECIMAL(4,2)    COMMENT '奖金系数' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (performance_rank_factor_id)
)  COMMENT = '绩效等级系数表';


CREATE TABLE performance_percentage(
    performance_percentage_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    performance_percentage_name VARCHAR(64)    COMMENT '绩效比例名称' ,
    org_performance_rank_id BIGINT UNSIGNED NOT NULL   COMMENT '组织绩效等级ID' ,
    person_performance_rank_id BIGINT UNSIGNED NOT NULL   COMMENT '个人绩效等级ID' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (performance_percentage_id)
)  COMMENT = '绩效比例表';


CREATE TABLE performance_percentage_data(
    performance_percentage_data_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    performance_percentage_id BIGINT UNSIGNED NOT NULL   COMMENT '绩效比例ID' ,
    org_rank_factor_id BIGINT UNSIGNED NOT NULL   COMMENT '组织绩效等级系数ID' ,
    person_rank_factor_id BIGINT UNSIGNED NOT NULL   COMMENT '个人绩效等级系数ID' ,
    value DECIMAL(5,2)    COMMENT '数值,单位:百分号%' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (performance_percentage_data_id)
)  COMMENT = '绩效比例数据表';


CREATE TABLE product_unit(
    product_unit_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    product_unit_code VARCHAR(32) NOT NULL   COMMENT '产品单位编码' ,
    product_unit_name VARCHAR(64)    COMMENT '产品单位名称' ,
    reserve_digit TINYINT UNSIGNED    COMMENT '保留的小数位(0代表整数;1代表1位小数...)' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (product_unit_id)
)  COMMENT = '产品单位表';


CREATE TABLE product(
    product_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    parent_product_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '父级产品ID' ,
    ancestors VARCHAR(256)   DEFAULT '' COMMENT '祖级列表ID，按层级用英文逗号隔开' ,
    product_code VARCHAR(32) NOT NULL   COMMENT '产品编码' ,
    product_name VARCHAR(64)    COMMENT '产品名称' ,
    level TINYINT UNSIGNED    COMMENT '层级' ,
    product_unit_id BIGINT UNSIGNED    COMMENT '产品单位ID' ,
    product_category VARCHAR(128)    COMMENT '产品类别' ,
    product_description VARCHAR(512)    COMMENT '产品描述' ,
    list_price DECIMAL(18,2)    COMMENT '目录价' ,
    listing_flag TINYINT UNSIGNED    COMMENT '上架标记：0下架;1上架' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (product_id)
)  COMMENT = '产品表';


CREATE TABLE product_file(
    product_file_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    product_id BIGINT UNSIGNED NOT NULL   COMMENT '产品ID' ,
    product_file_name VARCHAR(128)    COMMENT '产品文件名称' ,
    product_file_format VARCHAR(16)    COMMENT '产品文件格式' ,
    product_file_size BIGINT UNSIGNED    COMMENT '产品文件大小' ,
    product_file_path VARCHAR(256)    COMMENT '产品文件路径' ,
    sort TINYINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (product_file_id)
)  COMMENT = '产品文件表';


CREATE TABLE product_specification(
    product_specification_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    product_id BIGINT UNSIGNED NOT NULL   COMMENT '产品ID' ,
    specification_name VARCHAR(64)    COMMENT '规格名称' ,
    list_price DECIMAL(12,2)    COMMENT '目录价,单位元' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (product_specification_id)
)  COMMENT = '产品规格表';


CREATE TABLE product_specification_param(
    product_specification_param_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    product_id BIGINT UNSIGNED NOT NULL   COMMENT '产品ID' ,
    specification_param_name VARCHAR(64)    COMMENT '规格参数名称' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (product_specification_param_id)
)  COMMENT = '产品规格参数表';


CREATE TABLE product_specification_data(
    product_specification_data_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    product_id BIGINT UNSIGNED NOT NULL   COMMENT '产品ID' ,
    product_specification_id BIGINT UNSIGNED NOT NULL   COMMENT '产品规格ID' ,
    product_specification_param_id BIGINT UNSIGNED NOT NULL   COMMENT '产品规格参数ID' ,
    value VARCHAR(128)    COMMENT '产品规格参数数值' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (product_specification_data_id)
)  COMMENT = '产品规格数据表';


CREATE TABLE salary_item(
    salary_item_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    first_level_item TINYINT UNSIGNED NOT NULL   COMMENT '一级项目:1总工资包;2总奖金包;3总扣减项' ,
    second_level_item TINYINT UNSIGNED NOT NULL   COMMENT '二级项目:1工资;2津贴;3福利;4奖金;5代扣代缴;6其他扣款' ,
    third_level_item VARCHAR(64)    COMMENT '三级项目' ,
    scope TINYINT UNSIGNED NOT NULL   COMMENT '作用范围：1部门;2公司' ,
    sort SMALLINT UNSIGNED   DEFAULT 0 COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (salary_item_id)
)  COMMENT = '工资项';


CREATE TABLE target_setting(
    target_setting_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    target_setting_type TINYINT UNSIGNED NOT NULL   COMMENT '目标制定类型:0自定义;1销售订单;2销售收入;3销售回款' ,
    indicator_id BIGINT UNSIGNED NOT NULL   COMMENT '指标ID' ,
    target_year SMALLINT UNSIGNED NOT NULL   COMMENT '目标年度' ,
    percentage DECIMAL(6,2)    COMMENT '百分比(%)' ,
    challenge_value DECIMAL(14,2)    COMMENT '挑战值' ,
    target_value DECIMAL(14,2)    COMMENT '目标值' ,
    guaranteed_value DECIMAL(14,2)    COMMENT '保底值' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (target_setting_id)
)  COMMENT = '目标制定';


CREATE TABLE target_setting_income(
    target_setting_income_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    target_setting_id BIGINT UNSIGNED NOT NULL   COMMENT '目标制定ID' ,
    money_before_one DECIMAL(14,2)    COMMENT '一年前订单金额' ,
    money_before_two DECIMAL(14,2)    COMMENT '两年前订单金额' ,
    money_before_three DECIMAL(14,2)    COMMENT '三年前订单金额' ,
    conversion_before_one DECIMAL(6,2)    COMMENT '一年前订单转化率' ,
    conversion_before_two DECIMAL(6,2)    COMMENT '两年前订单转化率' ,
    conversion_before_three DECIMAL(6,2)    COMMENT '三年前订单转化率' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (target_setting_income_id)
)  COMMENT = '目标制定收入表';


CREATE TABLE target_setting_recoveries(
    target_setting_recoveries_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    target_setting_id BIGINT UNSIGNED NOT NULL   COMMENT '目标制定ID' ,
    type TINYINT UNSIGNED NOT NULL   COMMENT '类型:1:应回尽回;2:逾期清理;3:提前还款;4:销售收入目标;5:期末应收账款余额' ,
    actual_last_year DECIMAL(14,2)    COMMENT '上年实际值' ,
    challenge_value DECIMAL(14,2)    COMMENT '挑战值' ,
    target_value DECIMAL(14,2)    COMMENT '目标值' ,
    guaranteed_value DECIMAL(14,2)    COMMENT '保底值' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (target_setting_recoveries_id)
)  COMMENT = '目标制定回款集合表';


CREATE TABLE target_setting_recovery(
    target_setting_recoveries_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    target_setting_id BIGINT UNSIGNED NOT NULL   COMMENT '目标制定ID' ,
    balance_receivables DECIMAL(14,2)    COMMENT '上年年末应收账款余额' ,
    baseline_value SMALLINT UNSIGNED    COMMENT 'DSO(应收账款周转天数)基线' ,
    improve_days SMALLINT UNSIGNED    COMMENT 'DSO(应收账款周转天数)改进天数' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (target_setting_recoveries_id)
)  COMMENT = '目标制定回款表';


CREATE TABLE target_setting_order(
    target_setting_order_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    target_setting_id BIGINT UNSIGNED NOT NULL   COMMENT '目标制定ID' ,
    history_year SMALLINT UNSIGNED NOT NULL   COMMENT '历史年度' ,
    history_actual DECIMAL(14,2)    COMMENT '历史年度实际值' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (target_setting_order_id)
)  COMMENT = '目标制定订单表';


CREATE TABLE target_decompose(
    target_decompose_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    target_decompose_type TINYINT UNSIGNED NOT NULL   COMMENT '目标分解类型:0自定义;1销售订单;2销售收入;3销售回款' ,
    indicator_id BIGINT UNSIGNED NOT NULL   COMMENT '指标ID' ,
    target_year SMALLINT UNSIGNED NOT NULL   COMMENT '目标年度' ,
    target_decompose_dimension_id BIGINT UNSIGNED NOT NULL   COMMENT '目标分解维度ID' ,
    decomposition_dimension VARCHAR(256)    COMMENT '分解维度' ,
    time_dimension TINYINT UNSIGNED NOT NULL   COMMENT '时间维度:1年度;2半年度;3季度;4月度;5周' ,
    decompose_target DECIMAL(14,2)    COMMENT '分解目标值' ,
    forecast_year DECIMAL(14,2)    COMMENT '年度预测值' ,
    actual_total DECIMAL(14,2)    COMMENT '累计实际值' ,
    status TINYINT UNSIGNED NOT NULL   COMMENT '状态:0待录入;1已录入' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (target_decompose_id)
)  COMMENT = '目标分解表';


CREATE TABLE target_decompose_details(
    target_decompose_details_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    target_decompose_id BIGINT UNSIGNED NOT NULL   COMMENT '目标分解ID' ,
    employee_id BIGINT UNSIGNED    COMMENT '员工ID' ,
    area_id BIGINT UNSIGNED    COMMENT '区域ID' ,
    department_id BIGINT UNSIGNED    COMMENT '部门ID' ,
    product_id BIGINT UNSIGNED    COMMENT '产品ID' ,
    region_id BIGINT UNSIGNED    COMMENT '省份ID' ,
    industry_id BIGINT UNSIGNED    COMMENT '行业ID' ,
    principal_employee_id BIGINT UNSIGNED    COMMENT '负责人ID' ,
    amount_target DECIMAL(14,2)    COMMENT '汇总目标值' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (target_decompose_details_id)
)  COMMENT = '目标分解详情表';


CREATE TABLE decompose_detail_cycles(
    decompose_detail_cycles_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    target_decompose_details_id BIGINT UNSIGNED NOT NULL   COMMENT '目标分解详情ID' ,
    cycle_number TINYINT UNSIGNED NOT NULL   COMMENT '周期数(顺序递增)' ,
    cycle_target DECIMAL(14,2)    COMMENT '周期目标值' ,
    cycle_forecast DECIMAL(14,2)    COMMENT '周期预测值' ,
    cycle_actual DECIMAL(14,2)    COMMENT '周期实际值' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (decompose_detail_cycles_id)
)  COMMENT = '目标分解详情周期表';


CREATE TABLE target_decompose_history(
    target_decompose_history_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    target_decompose_id BIGINT UNSIGNED NOT NULL   COMMENT '目标分解ID' ,
    version VARCHAR(16)    COMMENT '版本号' ,
    forecast_cycle VARCHAR(16)    COMMENT '预测周期' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (target_decompose_history_id)
)  COMMENT = '目标分解历史版本表';


CREATE TABLE decompose_details_snapshot(
    decompose_details_snapshot_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    target_decompose_history_id BIGINT UNSIGNED NOT NULL   COMMENT '目标分解历史版本ID' ,
    employee_id BIGINT UNSIGNED    COMMENT '员工ID' ,
    area_id BIGINT UNSIGNED    COMMENT '区域ID' ,
    department_id BIGINT UNSIGNED    COMMENT '部门ID' ,
    product_id BIGINT UNSIGNED    COMMENT '产品ID' ,
    region_id BIGINT UNSIGNED    COMMENT '省份ID' ,
    industry_id BIGINT UNSIGNED    COMMENT '行业ID' ,
    principal_employee_id BIGINT UNSIGNED    COMMENT '负责人ID' ,
    amount_target DECIMAL(14,2)    COMMENT '汇总目标值' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (decompose_details_snapshot_id)
)  COMMENT = '目标分解详情快照表';


CREATE TABLE detail_cycles_snapshot(
    detail_cycles_snapshot_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    decompose_details_snapshot_id BIGINT UNSIGNED NOT NULL   COMMENT '目标分解详情快照ID' ,
    cycle_number TINYINT UNSIGNED NOT NULL   COMMENT '周期数(顺序递增)' ,
    cycle_target DECIMAL(14,2)    COMMENT '周期目标值' ,
    cycle_forecast DECIMAL(14,2)    COMMENT '周期预测值' ,
    cycle_actual DECIMAL(14,2)    COMMENT '周期实际值' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (detail_cycles_snapshot_id)
)  COMMENT = '目标分解详情周期快照表';


CREATE TABLE target_outcome(
    target_outcome_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    target_year SMALLINT UNSIGNED NOT NULL   COMMENT '目标年度' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (target_outcome_id)
)  COMMENT = '目标结果表';


CREATE TABLE target_outcome_details(
    target_outcome_details_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    target_outcome_id BIGINT UNSIGNED NOT NULL   COMMENT '目标结果ID' ,
    indicator_id BIGINT UNSIGNED NOT NULL   COMMENT '指标ID' ,
    actual_total DECIMAL(14,2)    COMMENT '累计实际值' ,
    actual_january DECIMAL(14,2)    COMMENT '一月实际值' ,
    actual_february DECIMAL(14,2)    COMMENT '二月实际值' ,
    actual_march DECIMAL(14,2)    COMMENT '三月实际值' ,
    actual_april DECIMAL(14,2)    COMMENT '四月实际值' ,
    actual_may DECIMAL(14,2)    COMMENT '五月实际值' ,
    actual_june DECIMAL(14,2)    COMMENT '六月实际值' ,
    actual_july DECIMAL(14,2)    COMMENT '七月实际值' ,
    actual_august DECIMAL(14,2)    COMMENT '八月实际值' ,
    actual_september DECIMAL(14,2)    COMMENT '九月实际值' ,
    actual_october DECIMAL(14,2)    COMMENT '十月实际值' ,
    actual_november DECIMAL(14,2)    COMMENT '十一月实际值' ,
    actual_december DECIMAL(14,2)    COMMENT '十二月实际值' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (target_outcome_details_id)
)  COMMENT = '目标结果详情表';


CREATE TABLE employee_budget(
    employee_budget_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    budget_year SMALLINT UNSIGNED NOT NULL   COMMENT '预算年度' ,
    department_id BIGINT UNSIGNED NOT NULL   COMMENT '预算部门ID' ,
    official_rank_system_id BIGINT UNSIGNED NOT NULL   COMMENT '职级体系ID' ,
    budget_cycle TINYINT UNSIGNED NOT NULL   COMMENT '预算周期:1季度;2月度' ,
    amount_last_year INT UNSIGNED    COMMENT '上年期末人数' ,
    amount_adjust INT UNSIGNED    COMMENT '本年新增人数' ,
    amount_average_adjust DECIMAL(10,3)    COMMENT '平均新增数' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (employee_budget_id)
)  COMMENT = '人力预算表';


CREATE TABLE employee_budget_details(
    employee_budget_details_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    employee_budget_id BIGINT UNSIGNED NOT NULL   COMMENT '人力预算ID' ,
    official_rank INT UNSIGNED    COMMENT '岗位职级' ,
    number_last_year INT UNSIGNED    COMMENT '上年期末人数' ,
    count_adjust INT UNSIGNED    COMMENT '本年新增人数' ,
    average_adjust DECIMAL(10,3)    COMMENT '平均新增数' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (employee_budget_details_id)
)  COMMENT = '人力预算明细表';


CREATE TABLE employee_budget_adjusts(
    employee_budget_adjusts_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    employee_budget_details_id BIGINT UNSIGNED NOT NULL   COMMENT '人力预算明细ID' ,
    cycle_number TINYINT UNSIGNED NOT NULL   COMMENT '周期数(顺序递增)' ,
    number_adjust INT UNSIGNED    COMMENT '调整人数' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (employee_budget_adjusts_id)
)  COMMENT = '人力预算调整表';


CREATE TABLE emolument_plan(
    emolument_plan_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    plan_year SMALLINT UNSIGNED NOT NULL   COMMENT '预算年度' ,
    revenue_before_one DECIMAL(14,2)    COMMENT '预算年前一年销售收入' ,
    revenue DECIMAL(14,2)    COMMENT '预算年销售收入' ,
    revenue_after_one DECIMAL(14,2)    COMMENT '预算年后一年销售收入' ,
    revenue_after_two DECIMAL(14,2)    COMMENT '预算年后二年销售收入' ,
    er_before_one DECIMAL(14,2)    COMMENT '预算年前一年E/R值(%)' ,
    emolument_revenue_improve DECIMAL(14,2)    COMMENT '预算年E/R值改进率(%)' ,
    er_improve_after_one DECIMAL(14,2)    COMMENT '预算年后一年E/R值改进率(%)' ,
    er_improve_after_two DECIMAL(14,2)    COMMENT '预算年后二年E/R值改进率(%)' ,
    emolument_package_before_one DECIMAL(14,2)    COMMENT '预算年前一年总薪酬包' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (emolument_plan_id)
)  COMMENT = '薪酬规划表';



CREATE TABLE salary_pay(
    salary_pay_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    employee_id BIGINT UNSIGNED NOT NULL   COMMENT '员工ID' ,
    pay_year SMALLINT UNSIGNED NOT NULL   COMMENT '发薪年份' ,
    pay_month TINYINT UNSIGNED NOT NULL   COMMENT '发薪月份' ,
    salary_amount DECIMAL(18,2)    COMMENT '工资金额' ,
    allowance_amount DECIMAL(18,2)    COMMENT '津贴金额' ,
    welfare_amount DECIMAL(18,2)    COMMENT '福利金额' ,
    bonus_amount DECIMAL(18,2)    COMMENT '奖金金额' ,
    withhold_remit_tax DECIMAL(18,2)    COMMENT '代扣代缴金额' ,
    other_deductions DECIMAL(18,2)    COMMENT '其他扣款金额' ,
    pay_amount DECIMAL(18,2)    COMMENT '发薪金额' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (salary_pay_id)
)  COMMENT = '工资发薪表';


CREATE TABLE salary_pay_details(
    salary_pay_details_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    salary_pay_id BIGINT UNSIGNED NOT NULL   COMMENT '工资发薪ID' ,
    salary_item_id BIGINT UNSIGNED NOT NULL   COMMENT '工资项ID' ,
    amount DECIMAL(18,2)    COMMENT '金额(单位/元)' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (salary_pay_details_id)
)  COMMENT = '工资发薪明细表';


CREATE TABLE performance_appraisal(
    performance_appraisal_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    performance_rank_id BIGINT UNSIGNED NOT NULL   COMMENT '绩效等级ID' ,
    performance_rank_name VARCHAR(64)    COMMENT '绩效等级名称' ,
    appraisal_year SMALLINT UNSIGNED NOT NULL   COMMENT '考核年度' ,
    appraisal_name VARCHAR(128)    COMMENT '考核名称' ,
    cycle_flag TINYINT UNSIGNED NOT NULL   COMMENT '周期性考核标记:0否;1是' ,
    cycle_type TINYINT UNSIGNED NOT NULL   COMMENT '周期类型:1月度;2季度;3半年度;4年度' ,
    cycle_number TINYINT UNSIGNED NOT NULL   COMMENT '考核周期' ,
    appraisal_start_date DATE    COMMENT '考核开始日期' ,
    appraisal_end_date DATE    COMMENT '考核结束日期' ,
    filing_date DATE    COMMENT '归档日期' ,
    appraisal_flow TINYINT UNSIGNED    COMMENT '考核流程:1系统流程;2仅导入结果' ,
    appraisal_object TINYINT UNSIGNED NOT NULL   COMMENT '考核对象:1组织;2员工' ,
    self_defined_columns_flag TINYINT UNSIGNED NOT NULL   COMMENT '自定义列标记:0否;1是' ,
    appraisal_status TINYINT UNSIGNED NOT NULL   COMMENT '考核状态:1制定目标;2评议;3排名;4归档' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (performance_appraisal_id)
)  COMMENT = '绩效考核表';


CREATE TABLE performance_appraisal_objects(
    perform_appraisal_objects_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    performance_appraisal_id BIGINT UNSIGNED NOT NULL   COMMENT '绩效考核ID' ,
    appraisal_object_id BIGINT UNSIGNED NOT NULL   COMMENT '考核对象ID' ,
    appraisal_principal_id BIGINT UNSIGNED    COMMENT '考核负责人ID' ,
    appraisal_principal_name VARCHAR(64)    COMMENT '考核负责人姓名' ,
    evaluation_score DECIMAL(5,2)    COMMENT '评议分数' ,
    appraisal_result_id BIGINT UNSIGNED    COMMENT '考核结果(绩效等级系数ID)' ,
    appraisal_result VARCHAR(64)    COMMENT '考核结果' ,
    appraisal_object_status TINYINT UNSIGNED NOT NULL   COMMENT '考核对象状态:1待制定目标;2已制定目标-草稿;3待评议;4已评议-草稿;5待排名' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (perform_appraisal_objects_id)
)  COMMENT = '绩效考核对象表';


CREATE TABLE perform_appraisal_object_snap(
    appraisal_object_snap_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    perform_appraisal_objects_id BIGINT UNSIGNED NOT NULL   COMMENT '绩效考核对象ID' ,
    appraisal_object_name VARCHAR(64)    COMMENT '考核对象名称' ,
    appraisal_object_code VARCHAR(32)    COMMENT '考核对象编码' ,
    department_id BIGINT UNSIGNED    COMMENT '部门ID' ,
    department_name VARCHAR(64)    COMMENT '部门名称' ,
    post_id BIGINT UNSIGNED    COMMENT '岗位ID' ,
    post_name VARCHAR(64)    COMMENT '岗位名称' ,
    official_rank_system_id BIGINT UNSIGNED    COMMENT '职级体系ID' ,
    official_rank INT UNSIGNED    COMMENT '职级' ,
    official_rank_name VARCHAR(16)    COMMENT '职级名称' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (appraisal_object_snap_id)
)  COMMENT = '绩效考核对象快照表';


CREATE TABLE performance_appraisal_items(
    perform_appraisal_items_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    perform_appraisal_objects_id BIGINT UNSIGNED NOT NULL   COMMENT '绩效考核对象ID' ,
    indicator_id BIGINT UNSIGNED NOT NULL   COMMENT '指标ID' ,
    indicator_name VARCHAR(64)    COMMENT '指标名称' ,
    indicator_value_type TINYINT UNSIGNED    COMMENT '指标值类型:1金额;2比率' ,
    examine_direction TINYINT UNSIGNED    COMMENT '考核方向:0反向;1正向' ,
    challenge_value DECIMAL(18,2)    COMMENT '挑战值' ,
    target_value DECIMAL(18,2)    COMMENT '目标值' ,
    guaranteed_value DECIMAL(18,2)    COMMENT '保底值' ,
    actual_value DECIMAL(18,2)    COMMENT '实际值' ,
    weight DECIMAL(5,2)    COMMENT '权重百分比(%)' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (perform_appraisal_items_id)
)  COMMENT = '绩效考核项目表';


CREATE TABLE performance_appraisal_columns(
    perform_appraisal_columns_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    performance_appraisal_id BIGINT UNSIGNED NOT NULL   COMMENT '绩效考核ID' ,
    column_name VARCHAR(256)    COMMENT '列名' ,
    column_value VARCHAR(1024)    COMMENT '列值(所有行集合的JSON格式数据)' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (perform_appraisal_columns_id)
)  COMMENT = '绩效考核自定义列表';


CREATE TABLE official_rank_emolument(
    official_rank_emolument_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    official_rank_system_id BIGINT UNSIGNED NOT NULL   COMMENT '职级体系ID' ,
    official_rank INT UNSIGNED NOT NULL   COMMENT '职级' ,
    salary_cap DECIMAL(18,2)    COMMENT '工资上限' ,
    salary_floor DECIMAL(18,2)    COMMENT '工资下限' ,
    salary_median DECIMAL(18,2)    COMMENT '工资中位数' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (official_rank_emolument_id)
)  COMMENT = '职级薪酬表';


CREATE TABLE bonus_budget(
    bonus_budget_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    budget_year SMALLINT UNSIGNED NOT NULL   COMMENT '预算年度' ,
    amount_bonus_budget DECIMAL(14,2)    COMMENT '总奖金包预算' ,
    bonus_before_one DECIMAL(14,2)    COMMENT '预算年度前一年的总奖金包' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (bonus_budget_id)
)  COMMENT = '奖金预算表';


CREATE TABLE bonus_budget_parameters(
    bonus_budget_parameters_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    bonus_budget_id BIGINT UNSIGNED NOT NULL   COMMENT '奖金预算ID' ,
    indicator_id BIGINT UNSIGNED NOT NULL   COMMENT '指标ID' ,
    bonus_weight DECIMAL(14,2)    COMMENT '奖金权重(%)' ,
    bonus_proportion_standard DECIMAL(14,2)    COMMENT '奖金占比基准值(%)' ,
    bonus_proportion_variation DECIMAL(14,2)    COMMENT '奖金占比浮动差值' ,
    challenge_value DECIMAL(14,2)    COMMENT '挑战值' ,
    target_value DECIMAL(14,2)    COMMENT '目标值' ,
    guaranteed_value DECIMAL(14,2)    COMMENT '保底值' ,
    target_completion_rate DECIMAL(14,2)    COMMENT '预计目标达成率(%)' ,
    performance_after_one DECIMAL(14,2)    COMMENT '预算年后一年业绩增长率' ,
    performance_after_two DECIMAL(14,2)    COMMENT '预算年后二年业绩增长率' ,
    bonus_allowance_after_one DECIMAL(14,2)    COMMENT '预算年后一年奖金折让系数' ,
    bonus_allowance_after_two DECIMAL(14,2)    COMMENT '预算年后二年奖金折让系数' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (bonus_budget_parameters_id)
)  COMMENT = '奖金预算参数表';


CREATE TABLE dept_bonus_budget(
    dept_bonus_budget_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    budget_year SMALLINT UNSIGNED NOT NULL   COMMENT '预算年度' ,
    strategy_award_percentage DECIMAL(14,2)    COMMENT '战略奖比例' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (dept_bonus_budget_id)
)  COMMENT = '部门奖金包预算表';


CREATE TABLE dept_bonus_budget_details(
    dept_bonus_budget_details_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    dept_bonus_budget_id BIGINT UNSIGNED NOT NULL   COMMENT '部门奖金包预算ID' ,
    department_id BIGINT UNSIGNED NOT NULL   COMMENT '部门ID' ,
    dept_bonus_percentage DECIMAL(5,2)    COMMENT '部门奖金占比' ,
    department_importance_factor DECIMAL(6,2)    COMMENT '部门重要性系数' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (dept_bonus_budget_details_id)
)  COMMENT = '部门奖金预算明细表';


CREATE TABLE dept_bonus_budget_items(
    dept_bonus_budget_items_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    dept_bonus_budget_id BIGINT UNSIGNED NOT NULL   COMMENT '部门奖金包预算ID' ,
    dept_bonus_budget_details_id BIGINT UNSIGNED NOT NULL   COMMENT '部门奖金预算明细ID' ,
    salary_item_id BIGINT UNSIGNED NOT NULL   COMMENT '工资项ID' ,
    bonus_percentage DECIMAL(5,2)    COMMENT '奖金占比' ,
    sort SMALLINT UNSIGNED    COMMENT '排序' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (dept_bonus_budget_items_id)
)  COMMENT = '部门奖金预算项目表';


CREATE TABLE bonus_pay_application(
    bonus_pay_application_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    salary_item_id BIGINT UNSIGNED NOT NULL   COMMENT '奖项类别,工资条ID' ,
    award_code VARCHAR(64) NOT NULL   COMMENT '奖项编码' ,
    award_name VARCHAR(32)    COMMENT '奖项名称' ,
    award_year SMALLINT UNSIGNED NOT NULL   COMMENT '获奖时间-年' ,
    award_month TINYINT UNSIGNED NOT NULL   COMMENT '获奖时间-月' ,
    apply_department_id BIGINT UNSIGNED    COMMENT '申请部门ID' ,
    award_total_amount DECIMAL(18,2)    COMMENT '奖项总金额' ,
    bonus_pay_object TINYINT UNSIGNED NOT NULL   COMMENT '奖金发放对象:1部门;2员工;3部门+员工' ,
    award_description VARCHAR(512)    COMMENT '奖项事迹描述' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (bonus_pay_application_id)
)  COMMENT = '奖金发放申请表';


CREATE TABLE bonus_pay_budget_dept(
    bonus_pay_budget_dept_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    bonus_pay_application_id BIGINT UNSIGNED NOT NULL   COMMENT '奖金发放申请ID' ,
    department_id BIGINT UNSIGNED NOT NULL   COMMENT '部门ID' ,
    bonus_percentage DECIMAL(5,2)    COMMENT '奖金比例' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (bonus_pay_budget_dept_id)
)  COMMENT = '奖金发放预算部门表';


CREATE TABLE bonus_pay_objects(
    bonus_pay_objects_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    bonus_pay_application_id BIGINT UNSIGNED NOT NULL   COMMENT '奖金发放申请ID' ,
    bonus_pay_object TINYINT UNSIGNED NOT NULL   COMMENT '奖金发放对象:1部门;2员工' ,
    bonus_pay_object_id BIGINT UNSIGNED NOT NULL   COMMENT '奖金发放对象ID' ,
    award_amount DECIMAL(18,2)    COMMENT '奖项金额' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (bonus_pay_objects_id)
)  COMMENT = '奖金发放对象表';


CREATE TABLE dept_annual_bonus(
    dept_annual_bonus_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    annual_bonus_year SMALLINT UNSIGNED NOT NULL   COMMENT '年终奖年度' ,
    company_annual_bonus DECIMAL(14,2)    COMMENT '公司年终奖总包' ,
    department_annual_bonus DECIMAL(14,2)    COMMENT '部门年终奖总包' ,
    status TINYINT UNSIGNED NOT NULL   COMMENT '状态' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (dept_annual_bonus_id)
)  COMMENT = '部门年终奖表';


CREATE TABLE dept_annual_bonus_operate(
    dept_annual_bonus_operate_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    dept_annual_bonus_id BIGINT UNSIGNED NOT NULL   COMMENT '部门年终奖ID' ,
    indicator_id BIGINT UNSIGNED NOT NULL   COMMENT '指标ID' ,
    indicator_name VARCHAR(64)    COMMENT '指标名称' ,
    target_value DECIMAL(14,2)    COMMENT '目标值' ,
    actual_value DECIMAL(14,2)    COMMENT '实际值' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (dept_annual_bonus_operate_id)
)  COMMENT = '部门年终奖经营绩效结果表';


CREATE TABLE dept_annual_bonus_factor(
    dept_annual_bonus_factor_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    dept_annual_bonus_id BIGINT UNSIGNED NOT NULL   COMMENT '部门年终奖ID' ,
    department_id BIGINT UNSIGNED NOT NULL   COMMENT '部门ID' ,
    weight DECIMAL(5,2)    COMMENT '权重' ,
    last_performance_resulted VARCHAR(128)    COMMENT '最近绩效结果' ,
    performance_rank_id BIGINT UNSIGNED    COMMENT '绩效等级ID' ,
    performance_rank_factor_id BIGINT UNSIGNED    COMMENT '绩效等级系数ID' ,
    performance_rank VARCHAR(16)    COMMENT '绩效' ,
    performance_bonus_factor DECIMAL(5,2)    COMMENT '组织绩效奖金系数' ,
    importance_factor DECIMAL(5,2)    COMMENT '组织重要性系数' ,
    bonus_percentage DECIMAL(5,2)    COMMENT '奖金占比' ,
    distribute_bonus DECIMAL(14,2)    COMMENT '可分配年终奖' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (dept_annual_bonus_factor_id)
)  COMMENT = '部门年终奖系数表';


CREATE TABLE employee_annual_bonus(
    employee_annual_bonus_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    annual_bonus_year SMALLINT UNSIGNED NOT NULL   COMMENT '年终奖年度' ,
    department_id BIGINT UNSIGNED NOT NULL   COMMENT '一级部门ID' ,
    department_name VARCHAR(64)    COMMENT '一级部门名称' ,
    apply_department_id BIGINT UNSIGNED    COMMENT '申请部门ID' ,
    apply_department_name VARCHAR(64)    COMMENT '申请部门名称' ,
    apply_employee_id BIGINT UNSIGNED    COMMENT '申请人ID' ,
    apply_employee_name VARCHAR(64)    COMMENT '申请人姓名' ,
    distribute_bonus_amount DECIMAL(14,2)    COMMENT '分配年终奖金额' ,
    comment_flag TINYINT UNSIGNED    COMMENT '发起评议流程标记:0否;1是' ,
    comment_step TINYINT UNSIGNED    COMMENT '评议环节:1管理团队评议;2主管初评+管理团队评议' ,
    comment_employee_id BIGINT UNSIGNED    COMMENT '管理团队评议人ID' ,
    comment_employee_name VARCHAR(64)    COMMENT '管理团队评议人' ,
    comment_date DATE    COMMENT '评议日期' ,
    status TINYINT UNSIGNED    COMMENT '状态:0草稿;1待初评;2待评议;3已评议' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (employee_annual_bonus_id)
)  COMMENT = '个人年终奖表';


CREATE TABLE emp_annual_bonus_objects(
    emp_annual_bonus_objects_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    employee_annual_bonus_id BIGINT UNSIGNED NOT NULL   COMMENT '个人年终奖ID' ,
    employee_id BIGINT UNSIGNED NOT NULL   COMMENT '员工ID' ,
    choice_flag TINYINT UNSIGNED NOT NULL   COMMENT '选中标记:0否;1是' ,
    performance_rank_id BIGINT UNSIGNED    COMMENT '绩效等级ID' ,
    performance_rank_factor_id BIGINT UNSIGNED    COMMENT '绩效等级系数ID' ,
    performance_rank VARCHAR(16)    COMMENT '绩效' ,
    performance_bonus_factor DECIMAL(5,2)    COMMENT '绩效奖金系数' ,
    attendance_factor DECIMAL(5,2)    COMMENT '考勤系数' ,
    recommend_value DECIMAL(18,2)    COMMENT '建议值' ,
    comment_value DECIMAL(18,2)    COMMENT '评议值' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (emp_annual_bonus_objects_id)
)  COMMENT = '个人年终奖发放对象表';


CREATE TABLE emp_annual_bonus_snapshot(
    emp_annual_bonus_snapshot_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    employee_annual_bonus_id BIGINT UNSIGNED NOT NULL   COMMENT '个人年终奖ID' ,
    emp_annual_bonus_objects_id BIGINT UNSIGNED NOT NULL   COMMENT '个人年终奖发放对象ID' ,
    employee_name VARCHAR(64)    COMMENT '员工姓名' ,
    employee_code VARCHAR(32)    COMMENT '员工工号' ,
    department_name VARCHAR(64)    COMMENT '部门名称' ,
    post_name VARCHAR(64)    COMMENT '岗位名称' ,
    official_rank_name VARCHAR(64)    COMMENT '职级名称' ,
    seniority VARCHAR(32)    COMMENT '司龄' ,
    employee_basic_wage DECIMAL(18,2)    COMMENT '基本工资' ,
    emolument_before_one DECIMAL(18,2)    COMMENT '前一年总薪酬' ,
    bonus_before_one DECIMAL(18,2)    COMMENT '前一年奖金' ,
    bonus_before_two DECIMAL(18,2)    COMMENT '前二年奖金' ,
    last_performance_resulted VARCHAR(128)    COMMENT '最近绩效结果' ,
    bonus_percentage_one DECIMAL(5,2)    COMMENT '奖金占比一' ,
    bonus_percentage_two DECIMAL(5,2)    COMMENT '奖金占比二' ,
    reference_value_one DECIMAL(18,2)    COMMENT '参考值一' ,
    reference_value_two DECIMAL(18,2)    COMMENT '参考值二' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (emp_annual_bonus_snapshot_id)
)  COMMENT = '个人年终奖发放快照信息表';


CREATE TABLE dept_salary_adjust_plan(
    dept_salary_adjust_plan_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    plan_year SMALLINT UNSIGNED NOT NULL   COMMENT '预算年度' ,
    salary_adjust_total DECIMAL(14,2)    COMMENT '调薪总数' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (dept_salary_adjust_plan_id)
)  COMMENT = '部门调薪计划表';


CREATE TABLE dept_salary_adjust_item(
    dept_salary_adjust_item_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    dept_salary_adjust_plan_id BIGINT UNSIGNED NOT NULL   COMMENT '部门调薪计划ID' ,
    department_id BIGINT UNSIGNED NOT NULL   COMMENT '部门ID' ,
    coverage_percentage DECIMAL(6,2)    COMMENT '覆盖比例' ,
    adjustment_percentage DECIMAL(6,2)    COMMENT '调整比例' ,
    adjustment_time DATE    COMMENT '调整时间' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (dept_salary_adjust_item_id)
)  COMMENT = '部门调薪项表';


CREATE TABLE emp_salary_adjust_plan(
    emp_salary_adjust_plan_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    employee_id BIGINT UNSIGNED NOT NULL   COMMENT '员工ID' ,
    effective_date DATETIME NOT NULL   COMMENT '生效日期' ,
    adjustment_type VARCHAR(16) NOT NULL   COMMENT '调整类型(1调岗;2调级;3调薪),多个用英文逗号隔开' ,
    adjust_department_id BIGINT UNSIGNED    COMMENT '调整部门ID' ,
    adjust_department_name VARCHAR(64)    COMMENT '调整部门名称' ,
    adjust_post_id BIGINT UNSIGNED    COMMENT '调整岗位ID' ,
    adjust_post_name VARCHAR(64)    COMMENT '调整岗位名称' ,
    adjust_official_rank_system_id BIGINT UNSIGNED    COMMENT '调整职级体系ID' ,
    adjust_official_rank_system_name VARCHAR(64)    COMMENT '调整职级体系名称' ,
    adjust_official_rank INT UNSIGNED    COMMENT '调整职级' ,
    adjust_official_rank_name VARCHAR(16)    COMMENT '调整职级名称' ,
    adjust_emolument DECIMAL(18,2)    COMMENT '调整薪酬' ,
    adjust_explain VARCHAR(256)    COMMENT '调整说明' ,
    status TINYINT UNSIGNED NOT NULL   COMMENT '状态(0草稿;1生效)' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (emp_salary_adjust_plan_id)
)  COMMENT = '个人调薪计划表';


CREATE TABLE emp_salary_adjust_snap(
    emp_salary_adjust_snap_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    emp_salary_adjust_plan_id BIGINT UNSIGNED NOT NULL   COMMENT '个人调薪计划ID' ,
    employee_name VARCHAR(64)    COMMENT '员工姓名' ,
    employment_date DATE    COMMENT '入职日期' ,
    seniority VARCHAR(32)    COMMENT '司龄' ,
    department_id BIGINT UNSIGNED    COMMENT '原部门ID' ,
    department_name VARCHAR(64)    COMMENT '原部门名称' ,
    department_leader_id BIGINT UNSIGNED    COMMENT '部门负责人ID' ,
    department_leader_name VARCHAR(64)    COMMENT '部门负责人姓名' ,
    post_id BIGINT UNSIGNED    COMMENT '原岗位ID' ,
    post_name VARCHAR(64)    COMMENT '原岗位名称' ,
    official_rank_system_id BIGINT UNSIGNED    COMMENT '原职级体系ID' ,
    official_rank_system_name VARCHAR(64)    COMMENT '原职级体系名称' ,
    official_rank INT UNSIGNED    COMMENT '原职级' ,
    official_rank_name VARCHAR(16)    COMMENT '原职级名称' ,
    basic_wage DECIMAL(18,2)    COMMENT '基本工资(当前薪酬)' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (emp_salary_adjust_snap_id)
)  COMMENT = '个人调薪快照表';


CREATE TABLE emp_salary_adjust_perform(
    emp_salary_adjust_perform_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    emp_salary_adjust_plan_id BIGINT UNSIGNED NOT NULL   COMMENT '个人调薪计划ID' ,
    performance_appraisal_id BIGINT UNSIGNED NOT NULL   COMMENT '绩效考核ID' ,
    perform_appraisal_objects_id BIGINT UNSIGNED NOT NULL   COMMENT '绩效考核对象ID' ,
    cycle_type TINYINT UNSIGNED NOT NULL   COMMENT '周期类型:1月度;2季度;3半年度;4年度' ,
    cycle_number TINYINT UNSIGNED NOT NULL   COMMENT '考核周期' ,
    filing_date DATE    COMMENT '归档日期' ,
    appraisal_result VARCHAR(64)    COMMENT '考核结果' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (emp_salary_adjust_perform_id)
)  COMMENT = '个人调薪绩效记录表';