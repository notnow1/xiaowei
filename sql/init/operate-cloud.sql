SET NAMES utf8mb4;

CREATE TABLE area(
    area_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    area_code VARCHAR(32)    COMMENT '区域编码' ,
    area_name VARCHAR(64)    COMMENT '区域名称' ,
    region_ids VARCHAR(2048)    COMMENT '地区ID集合,用英文逗号隔开' ,
    region_names VARCHAR(2048)    COMMENT '地区名称集合,用英文逗号隔开' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (area_id)
)  COMMENT = '区域表';


CREATE TABLE target_decompose_dimension(
    target_decompose_dimension_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    decomposition_dimension VARCHAR(255)    COMMENT '分解维度(region,salesman,department,product,province,industry)' ,
    sort INT    COMMENT '排序' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (target_decompose_dimension_id)
)  COMMENT = '目标分解维度表';


CREATE TABLE performance_rank(
    performance_rank_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    performance_rank_category TINYINT    COMMENT '绩效等级类别:1组织;2个人' ,
    performance_rank_name VARCHAR(64)    COMMENT '绩效等级名称' ,
    performance_rank_description VARCHAR(255)    COMMENT '绩效等级描述' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (performance_rank_id)
)  COMMENT = '绩效等级表';


CREATE TABLE performance_rank_factor(
    performance_rank_factor_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    performance_rank_id BIGINT    COMMENT '绩效等级ID' ,
    performance_rank_name VARCHAR(32)    COMMENT '绩效等级名称' ,
    bonus_factor DECIMAL(4,2)    COMMENT '奖金系数' ,
    sort INT    COMMENT '排序' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (performance_rank_factor_id)
)  COMMENT = '绩效等级系数表';


CREATE TABLE performance_percentage(
    performance_percentage_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    performance_percentage_name VARCHAR(64)    COMMENT '绩效比例名称' ,
    org_performance_rank_id BIGINT    COMMENT '组织绩效等级ID' ,
    person_performance_rank_id BIGINT    COMMENT '个人绩效等级ID' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (performance_percentage_id)
)  COMMENT = '绩效比例表';


CREATE TABLE performance_percentage_data(
    performance_percentage_data_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    performance_percentage_id BIGINT    COMMENT '绩效比例ID' ,
    org_rank_factor_id BIGINT    COMMENT '组织绩效等级系数ID' ,
    person_rank_factor_id BIGINT    COMMENT '个人绩效等级系数ID' ,
    value DECIMAL(5,2)    COMMENT '数值,单位:百分号%' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (performance_percentage_data_id)
)  COMMENT = '绩效比例数据表';


CREATE TABLE product_unit(
    product_unit_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    product_unit_code VARCHAR(32)    COMMENT '产品单位编码' ,
    product_unit_name VARCHAR(64)    COMMENT '产品单位名称' ,
    reserve_digit TINYINT    COMMENT '保留的小数位(0代表整数;1代表1位小数...)' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (product_unit_id)
)  COMMENT = '产品单位表';


CREATE TABLE product(
    product_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    parent_product_id BIGINT   DEFAULT 0 COMMENT '父级产品ID' ,
    ancestors VARCHAR(255)   DEFAULT '' COMMENT '祖级列表ID，按层级用英文逗号隔开' ,
    product_code VARCHAR(32)    COMMENT '产品编码' ,
    product_name VARCHAR(64)    COMMENT '产品名称' ,
    level TINYINT    COMMENT '层级' ,
    product_unit_id BIGINT    COMMENT '产品单位ID' ,
    product_category VARCHAR(128)    COMMENT '产品类别' ,
    product_description VARCHAR(512)    COMMENT '产品描述' ,
    list_price DECIMAL(12,2)    COMMENT '目录价' ,
    listing_flag TINYINT    COMMENT '上架标记：0下架;1上架' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (product_id)
)  COMMENT = '产品表';


CREATE TABLE product_file(
    product_file_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    product_id BIGINT    COMMENT '产品ID' ,
    product_file_name VARCHAR(128)    COMMENT '产品文件名称' ,
    product_file_format VARCHAR(16)    COMMENT '产品文件格式' ,
    product_file_size BIGINT    COMMENT '产品文件大小' ,
    product_file_path VARCHAR(255)    COMMENT '产品文件路径' ,
    sort TINYINT    COMMENT '排序' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (product_file_id)
)  COMMENT = '产品文件表';


CREATE TABLE product_specification(
    product_specification_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    product_id BIGINT    COMMENT '产品ID' ,
    specification_name VARCHAR(64)    COMMENT '规格名称' ,
    list_price DECIMAL(12,2)    COMMENT '目录价,单位元' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (product_specification_id)
)  COMMENT = '产品规格表';


CREATE TABLE product_specification_param(
    product_specification_param_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    product_id BIGINT    COMMENT '产品ID' ,
    specification_param_name VARCHAR(64)    COMMENT '规格参数名称' ,
    sort INT    COMMENT '排序' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (product_specification_param_id)
)  COMMENT = '产品规格参数表';


CREATE TABLE product_specification_data(
    product_specification_data_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    product_id BIGINT    COMMENT '产品ID' ,
    product_specification_id BIGINT    COMMENT '产品规格ID' ,
    product_specification_param_id BIGINT    COMMENT '产品规格参数ID' ,
    value VARCHAR(128)    COMMENT '产品规格参数数值' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (product_specification_data_id)
)  COMMENT = '产品规格数据表';


CREATE TABLE salary_item(
    salary_item_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    first_level_item TINYINT    COMMENT '一级项目:1总工资包;2总奖金包;3总扣减项' ,
    second_level_item TINYINT    COMMENT '二级项目:1工资;2津贴;3福利;4奖金;5代扣代缴;6其他扣款' ,
    third_level_item VARCHAR(64)    COMMENT '三级项目' ,
    scope TINYINT    COMMENT '作用范围：1部门;2公司' ,
    sort INT   DEFAULT 0 COMMENT '排序' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (salary_item_id)
)  COMMENT = '工资项';


CREATE TABLE target_setting(
    target_setting_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    target_setting_type TINYINT    COMMENT '目标制定类型:0自定义;1销售订单;2销售收入;3销售回款' ,
    indicator_id BIGINT    COMMENT '指标ID' ,
    target_year INT    COMMENT '目标年度' ,
    percentage DECIMAL(6,2)    COMMENT '百分比(%)' ,
    challenge_value DECIMAL(14,2)    COMMENT '挑战值' ,
    target_value DECIMAL(14,2)    COMMENT '目标值' ,
    guaranteed_value DECIMAL(14,2)    COMMENT '保底值' ,
    sort INT    COMMENT '排序' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (target_setting_id)
)  COMMENT = '目标制定';


CREATE TABLE target_setting_income(
    target_setting_income_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    target_setting_id BIGINT    COMMENT '目标制定ID' ,
    money_before_one DECIMAL(14,2)    COMMENT '一年前订单金额' ,
    money_before_two DECIMAL(14,2)    COMMENT '两年前订单金额' ,
    money_before_three DECIMAL(14,2)    COMMENT '三年前订单金额' ,
    conversion_before_one DECIMAL(6,2)    COMMENT '一年前订单转化率' ,
    conversion_before_two DECIMAL(6,2)    COMMENT '两年前订单转化率' ,
    conversion_before_three DECIMAL(6,2)    COMMENT '三年前订单转化率' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (target_setting_income_id)
)  COMMENT = '目标制定收入表';


CREATE TABLE target_setting_recoveries(
    target_setting_recoveries_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    target_setting_id BIGINT    COMMENT '目标制定ID' ,
    type TINYINT    COMMENT '类型:1:应回尽回;2:逾期清理;3:提前还款;4:销售收入目标;5:期末应收账款余额' ,
    actual_last_year DECIMAL(14,2)    COMMENT '上年实际值' ,
    challenge_value DECIMAL(14,2)    COMMENT '挑战值' ,
    target_value DECIMAL(14,2)    COMMENT '目标值' ,
    guaranteed_value DECIMAL(14,2)    COMMENT '保底值' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (target_setting_recoveries_id)
)  COMMENT = '目标制定回款集合表';


CREATE TABLE target_setting_recovery(
    target_setting_recoveries_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    target_setting_id BIGINT    COMMENT '目标制定ID' ,
    balance_receivables DECIMAL(14,2)    COMMENT '上年年末应收账款余额' ,
    baseline_value INT    COMMENT 'DSO(应收账款周转天数)基线' ,
    improve_days INT    COMMENT 'DSO(应收账款周转天数)改进天数' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (target_setting_recoveries_id)
)  COMMENT = '目标制定回款表';


CREATE TABLE target_setting_order(
    target_setting_order_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    target_setting_id BIGINT    COMMENT '目标制定ID' ,
    history_year INT    COMMENT '历史年度' ,
    history_actual DECIMAL(14,2)    COMMENT '历史年度实际值' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (target_setting_order_id)
)  COMMENT = '目标制定订单表';


CREATE TABLE target_decompose(
    target_decompose_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    target_decompose_type TINYINT    COMMENT '目标分解类型:0自定义;1销售订单;2销售收入;3销售回款' ,
    indicator_id BIGINT    COMMENT '指标ID' ,
    target_year INT    COMMENT '目标年度' ,
    target_decompose_dimension_id BIGINT    COMMENT '目标分解维度ID' ,
    decomposition_dimension VARCHAR(256)    COMMENT '分解维度' ,
    time_dimension TINYINT    COMMENT '时间维度:1年度;2半年度;3季度;4月度;5周' ,
    decompose_target DECIMAL(14,2)    COMMENT '分解目标值' ,
    forecast_year DECIMAL(14,2)    COMMENT '年度预测值' ,
    actual_total DECIMAL(14,2)    COMMENT '累计实际值' ,
    status TINYINT    COMMENT '状态:0待录入;1已录入' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (target_decompose_id)
)  COMMENT = '目标分解表';


CREATE TABLE target_decompose_details(
    target_decompose_details_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    target_decompose_id BIGINT    COMMENT '目标分解ID' ,
    employee_id BIGINT    COMMENT '员工ID' ,
    area_id BIGINT    COMMENT '区域ID' ,
    department_id BIGINT    COMMENT '部门ID' ,
    product_id BIGINT    COMMENT '产品ID' ,
    region_id BIGINT    COMMENT '省份ID' ,
    industry_id BIGINT    COMMENT '行业ID' ,
    principal_employee_id BIGINT    COMMENT '负责人ID' ,
    amount_target DECIMAL(14,2)    COMMENT '汇总目标值' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (target_decompose_details_id)
)  COMMENT = '目标分解详情表';


CREATE TABLE decompose_detail_cycles(
    decompose_detail_cycles_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    target_decompose_details_id BIGINT    COMMENT '目标分解详情ID' ,
    cycle_number INT    COMMENT '周期数(顺序递增)' ,
    cycle_target DECIMAL(14,2)    COMMENT '周期目标值' ,
    cycle_forecast DECIMAL(14,2)    COMMENT '周期预测值' ,
    cycle_actual DECIMAL(14,2)    COMMENT '周期实际值' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (decompose_detail_cycles_id)
)  COMMENT = '目标分解详情周期表';



-- ----------------------------
-- 初始化数据
-- ----------------------------