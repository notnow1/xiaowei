DROP DATABASE IF EXISTS `system-manage`;
CREATE DATABASE `system-manage` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `system-manage`;
SET NAMES utf8mb4;

CREATE TABLE file(
    file_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    tenant_id BIGINT    COMMENT '租户ID' ,
    source VARCHAR(64)    COMMENT '来源' ,
    file_name VARCHAR(1024)    COMMENT '文件名称' ,
    file_format VARCHAR(64)    COMMENT '文件格式' ,
    file_size BIGINT    COMMENT '大小' ,
    file_path VARCHAR(2048)    COMMENT '路径' ,
    delete_flag TINYINT    COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (file_id)
)  COMMENT = '文件表';


CREATE TABLE product_package
(
    product_package_id          BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    product_package_name        VARCHAR(64) COMMENT '产品包名',
    product_package_description VARCHAR(255) COMMENT '产品包描述',
    remark                      VARCHAR(255) COMMENT '备注',
    sort                        INT COMMENT '排序',
    delete_flag                 TINYINT DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by                   BIGINT COMMENT '创建人',
    create_time                 TIMESTAMP COMMENT '创建时间',
    update_by                   BIGINT COMMENT '更新人',
    update_time                 TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (product_package_id)
) COMMENT = '产品包表';


CREATE TABLE tenant
(
    tenant_id        BIGINT NOT NULL AUTO_INCREMENT COMMENT '租户ID',
    tenant_code      VARCHAR(32) COMMENT '租户编码',
    tenant_name      VARCHAR(255) COMMENT '租户名称',
    tenant_address   VARCHAR(255) COMMENT '租户地址',
    tenant_industry  BIGINT COMMENT '租户行业',
    domain           VARCHAR(90) COMMENT '域名',
    admin_account    VARCHAR(32) COMMENT '管理员帐号',
    admin_password   VARCHAR(32) COMMENT '管理员密码',
    support_staff    VARCHAR(2048) COMMENT '客服人员',
    login_background VARCHAR(512) COMMENT '租户登录背景图片URL',
    tenant_logo      VARCHAR(512) COMMENT '租户logo图片URL',
    tenant_status    TINYINT COMMENT '状态（0待初始化 1正常 2禁用 3过期）',
    delete_flag      TINYINT DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by        BIGINT COMMENT '创建人',
    create_time      TIMESTAMP COMMENT '创建时间',
    update_by        BIGINT COMMENT '更新人',
    update_time      TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (tenant_id)
) COMMENT = '租户表';


CREATE TABLE tenant_contacts
(
    tenant_contacts_id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    tenant_id          BIGINT COMMENT '租户ID',
    contact_name       VARCHAR(64) COMMENT '联系人姓名',
    contact_tel        VARCHAR(64) COMMENT '联系人手机号',
    contact_email      VARCHAR(255) COMMENT '联系人邮箱',
    contact_duty       VARCHAR(255) COMMENT '联系人职务',
    remark             VARCHAR(255) COMMENT '备注',
    delete_flag        TINYINT DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by          BIGINT COMMENT '创建人',
    create_time        TIMESTAMP COMMENT '创建时间',
    update_by          BIGINT COMMENT '更新人',
    update_time        TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (tenant_contacts_id)
) COMMENT = '租户联系人表';


CREATE TABLE tenant_contract
(
    tenant_contract_id  BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    tenant_id           BIGINT COMMENT '租户ID',
    sales_contract_no   VARCHAR(255) COMMENT '销售合同编号',
    sales_personnel     VARCHAR(255) COMMENT '销售人员',
    contract_amount     DECIMAL(12, 2) COMMENT '合同金额',
    contract_start_time TIMESTAMP COMMENT '合同开始时间',
    contract_end_time   TIMESTAMP COMMENT '合同结束时间',
    product_package     VARCHAR(255) COMMENT '开通的产品包',
    delete_flag         TINYINT DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by           BIGINT COMMENT '创建人',
    create_time         TIMESTAMP COMMENT '创建时间',
    update_by           BIGINT COMMENT '更新人',
    update_time         TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (tenant_contract_id)
) COMMENT = '租户合同信息表';


CREATE TABLE tenant_domain_approval
(
    tenant_domain_approval_id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    tenant_id                 BIGINT COMMENT '租户ID',
    approval_domain           VARCHAR(90) COMMENT '申请域名',
    applicant_user_id         BIGINT COMMENT '申请人用户ID',
    applicant_user_account    VARCHAR(64) COMMENT '申请人账户',
    submission_time           TIMESTAMP COMMENT '提交时间',
    approval_time             TIMESTAMP COMMENT '审核时间',
    approval_user_id          BIGINT COMMENT '审核人用户ID',
    approval_status           TINYINT COMMENT '申请状态:0待审核;1审核通过;2审核驳回',
    remark                    VARCHAR(255) COMMENT '备注',
    delete_flag               TINYINT DEFAULT 0 COMMENT '删除标记',
    create_by                 BIGINT COMMENT '创建人',
    create_time               TIMESTAMP COMMENT '创建时间',
    update_by                 BIGINT COMMENT '更新人',
    update_time               TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (tenant_domain_approval_id)
) COMMENT = '租户域名申请表';



CREATE TABLE user
(
    user_id      BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    employee_id  BIGINT COMMENT '员工ID',
    user_account VARCHAR(128) COMMENT '用户帐号',
    password     VARCHAR(128) COMMENT '密码',
    user_name    VARCHAR(64) COMMENT '用户名称',
    mobile_phone VARCHAR(32) COMMENT '手机号码',
    email        VARCHAR(64) COMMENT '邮箱',
    avatar       VARCHAR(256) COMMENT '头像',
    remark       VARCHAR(512) COMMENT '备注',
    status       TINYINT COMMENT '状态:0失效;1生效',
    delete_flag  TINYINT DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by    BIGINT COMMENT '创建人',
    create_time  TIMESTAMP COMMENT '创建时间',
    update_by    BIGINT COMMENT '更新人',
    update_time  TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (user_id)
) COMMENT = '用户表';


CREATE TABLE role
(
    role_id         BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    role_code       VARCHAR(64) COMMENT '角色编码',
    role_name       VARCHAR(64) COMMENT '角色名称',
    data_scope      TINYINT COMMENT '数据范围:1全公司;2本部门及下属部门;3本部门;4本人及下属;5本人',
    product_package VARCHAR(512) COMMENT '分配的产品包',
    sort            INT COMMENT '排序',
    remark          VARCHAR(512) COMMENT '备注',
    status          TINYINT COMMENT '状态:0失效;1生效',
    delete_flag     TINYINT DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by       BIGINT COMMENT '创建人',
    create_time     TIMESTAMP COMMENT '创建时间',
    update_by       BIGINT COMMENT '更新人',
    update_time     TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (role_id)
) COMMENT = '角色表';


CREATE TABLE user_role
(
    user_role_id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    user_id      BIGINT COMMENT '用户ID',
    role_id      BIGINT COMMENT '角色ID',
    delete_flag  TINYINT DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by    BIGINT COMMENT '创建人',
    create_time  TIMESTAMP COMMENT '创建时间',
    update_by    BIGINT COMMENT '更新人',
    update_time  TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (user_role_id)
) COMMENT = '用户角色表';


CREATE TABLE menu
(
    menu_id            BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    parent_menu_id     BIGINT  DEFAULT 0 COMMENT '父级菜单ID',
    menu_type          TINYINT COMMENT '1目录;2菜单;3按钮',
    menu_name          VARCHAR(128) COMMENT '菜单名称',
    product_package_id BIGINT COMMENT '产品包ID',
    sort               INT COMMENT '排序',
    path               VARCHAR(255) COMMENT '路由地址',
    component          VARCHAR(255) COMMENT '组件路径',
    query              VARCHAR(255) COMMENT '路由参数',
    external_link_flag TINYINT COMMENT '外链标记:0否;1是',
    cache_flag         TINYINT COMMENT '缓存标记:0否;1是',
    visible_flag       TINYINT COMMENT '菜单可见状态:0否;1是',
    permission_code    VARCHAR(255) COMMENT '权限编码',
    icon               VARCHAR(128) COMMENT '菜单图标',
    status             TINYINT COMMENT '状态:0失效;1生效',
    delete_flag        TINYINT DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by          BIGINT COMMENT '创建人',
    create_time        TIMESTAMP COMMENT '创建时间',
    update_by          BIGINT COMMENT '更新人',
    update_time        TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (menu_id)
) COMMENT = '菜单表';


CREATE TABLE role_menu
(
    role_menu_id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    role_id      BIGINT COMMENT '角色ID',
    menu_id      BIGINT COMMENT '菜单ID',
    delete_flag  TINYINT DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by    BIGINT COMMENT '创建人',
    create_time  TIMESTAMP COMMENT '创建时间',
    update_by    BIGINT COMMENT '更新人',
    update_time  TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (role_menu_id)
) COMMENT = '角色菜单表';



CREATE TABLE department
(
    department_id                BIGINT NOT NULL AUTO_INCREMENT COMMENT '部门ID',
    parent_department_id         BIGINT       DEFAULT 0 COMMENT '父级部门ID',
    ancestors                    VARCHAR(255) DEFAULT '' COMMENT '祖级列表ID，按层级用英文逗号隔开',
    department_code              VARCHAR(32) COMMENT '部门编码',
    department_name              VARCHAR(64) COMMENT '部门名称',
    level                        TINYINT COMMENT '部门层级',
    department_leader_id         BIGINT COMMENT '部门负责人ID',
    department_leader_post_id    BIGINT COMMENT '部门负责人岗位ID',
    examination_leader_id        BIGINT COMMENT '考核负责人ID',
    department_importance_factor DECIMAL(6, 2) COMMENT '部门重要性系数',
    department_description       VARCHAR(255) COMMENT '部门描述',
    sort                         INT          DEFAULT 0 COMMENT '排序',
    status                       TINYINT COMMENT '状态:0失效;1生效',
    delete_flag                  TINYINT      DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by                    BIGINT COMMENT '创建人',
    create_time                  TIMESTAMP COMMENT '创建时间',
    update_by                    BIGINT COMMENT '更新人',
    update_time                  TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (department_id)
) COMMENT = '部门表';


CREATE TABLE department_post
(
    department_post_id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    department_id      BIGINT COMMENT '部门ID',
    post_id            BIGINT COMMENT '岗位ID',
    department_sort    INT COMMENT '部门排序',
    post_sort          INT COMMENT '岗位排序',
    delete_flag        TINYINT DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by          BIGINT COMMENT '创建人',
    create_time        TIMESTAMP COMMENT '创建时间',
    update_by          BIGINT COMMENT '更新人',
    update_time        TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (department_post_id)
) COMMENT = '部门岗位关联表';


CREATE TABLE post
(
    post_id                 BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    post_code               VARCHAR(32) COMMENT '岗位编码',
    post_name               VARCHAR(64) COMMENT '岗位名称',
    official_rank_system_id BIGINT COMMENT '职级体系ID',
    post_rank_lower         INT COMMENT '岗位职级下限',
    post_rank               INT COMMENT '岗位职级',
    post_rank_upper         INT COMMENT '岗位职级上限',
    post_description        VARCHAR(512) DEFAULT '' COMMENT '岗位说明书URL路径',
    status                  TINYINT COMMENT '状态:0失效;1生效',
    delete_flag             TINYINT      DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by               BIGINT COMMENT '创建人',
    create_time             TIMESTAMP COMMENT '创建时间',
    update_by               BIGINT COMMENT '更新人',
    update_time             TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (post_id)
) COMMENT = '岗位表';


CREATE TABLE dictionary_type
(
    dictionary_type_id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    dictionary_type    VARCHAR(128) COMMENT '字典类型',
    dictionary_name    VARCHAR(128) COMMENT '字典名称',
    menu_zeroth_name   VARCHAR(64) COMMENT '菜单0层级名称',
    menu_first_name    VARCHAR(64) COMMENT '菜单1层级名称',
    menu_second_name   VARCHAR(64) COMMENT '菜单2层级名称',
    remark             VARCHAR(512) COMMENT '备注',
    status             TINYINT COMMENT '状态:0失效;1生效',
    delete_flag        TINYINT DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by          BIGINT COMMENT '创建人',
    create_time        TIMESTAMP COMMENT '创建时间',
    update_by          BIGINT COMMENT '更新人',
    update_time        TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (dictionary_type_id)
) COMMENT = '字典类型表';


CREATE TABLE dictionary_data
(
    dictionary_data_id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    dictionary_type_id BIGINT COMMENT '字典类型ID',
    dictionary_label   VARCHAR(128) COMMENT '字典标签',
    dictionary_value   VARCHAR(64) COMMENT '字典值',
    default_flag       TINYINT COMMENT '默认标记:0否;1是',
    sort               INT COMMENT '排序',
    remark             VARCHAR(255) COMMENT '备注',
    status             TINYINT COMMENT '状态:0失效;1生效',
    delete_flag        TINYINT DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by          BIGINT COMMENT '创建人',
    create_time        TIMESTAMP COMMENT '创建时间',
    update_by          BIGINT COMMENT '更新人',
    update_time        TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (dictionary_data_id)
) COMMENT = '字典数据表';


CREATE TABLE employee
(
    employee_id            BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    employee_code          VARCHAR(32) COMMENT '工号',
    employee_name          VARCHAR(64) COMMENT '姓名',
    employee_gender        TINYINT COMMENT '性别:1男;2女',
    identity_type          TINYINT COMMENT '证件类型',
    identity_card          VARCHAR(32) COMMENT '证件号码',
    employee_birthday      DATE COMMENT '出生日期',
    employee_mobile        VARCHAR(32) COMMENT '员工手机号',
    employee_email         VARCHAR(128) COMMENT '员工邮箱',
    employee_department_id BIGINT COMMENT '员工部门ID',
    employee_post_id       BIGINT COMMENT '员工岗位ID',
    employee_rank          INT COMMENT '员工职级',
    employee_basic_wage    DECIMAL(12, 2) COMMENT '员工基本工资',
    employment_date        DATE COMMENT '入职日期',
    departure_date         DATE COMMENT '离职日期',
    employment_status      TINYINT COMMENT '用工状态:0离职;1在职',
    status                 TINYINT COMMENT '状态:0失效;1生效',
    delete_flag            TINYINT DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by              BIGINT COMMENT '创建人',
    create_time            TIMESTAMP COMMENT '创建时间',
    update_by              BIGINT COMMENT '更新人',
    update_time            TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (employee_id)
) COMMENT = '员工表';


CREATE TABLE employee_info
(
    employee_info_id       BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    employee_id            BIGINT COMMENT '员工ID',
    marital_status         TINYINT COMMENT '婚姻状况:0未婚;1已婚',
    nationality            VARCHAR(32) COMMENT '国籍',
    nation                 VARCHAR(32) COMMENT '民族',
    resident_city          VARCHAR(64) COMMENT '户口所在地',
    insured_city           VARCHAR(64) COMMENT '参保地',
    permanent_address      VARCHAR(64) COMMENT '常住地',
    contact_address        VARCHAR(64) COMMENT '通信地址',
    contact_address_detail VARCHAR(128) COMMENT '通信地址详情',
    wechat_code            VARCHAR(64) COMMENT '微信号',
    emergency_contact      VARCHAR(64) COMMENT '紧急联系人',
    emergency_mobile       VARCHAR(32) COMMENT '紧急联系人电话',
    create_by              BIGINT COMMENT '创建人',
    create_time            TIMESTAMP COMMENT '创建时间',
    update_by              BIGINT COMMENT '更新人',
    update_time            TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (employee_info_id)
) COMMENT = '员工信息';


CREATE TABLE industry
(
    industry_id        BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    parent_industry_id BIGINT       DEFAULT 0 COMMENT '父级行业ID',
    ancestors          VARCHAR(255) DEFAULT '' COMMENT '祖级列表ID，按层级用英文逗号隔开',
    industry_code      VARCHAR(32) COMMENT '行业编码',
    industry_name      VARCHAR(64) COMMENT '行业名称',
    level              TINYINT COMMENT '层级',
    status             TINYINT COMMENT '状态:0失效;1生效',
    delete_flag        TINYINT      DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by          BIGINT COMMENT '创建人',
    create_time        TIMESTAMP COMMENT '创建时间',
    update_by          BIGINT COMMENT '更新人',
    update_time        TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (industry_id)
) COMMENT = '行业表';


CREATE TABLE industry_default
(
    industry_id        BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    parent_industry_id BIGINT       DEFAULT 0 COMMENT '父级行业ID',
    ancestors          VARCHAR(255) DEFAULT '' COMMENT '祖级列表ID，按层级用英文逗号隔开',
    industry_code      VARCHAR(32) COMMENT '行业编码',
    industry_name      VARCHAR(64) COMMENT '行业名称',
    level              TINYINT COMMENT '层级',
    status             TINYINT COMMENT '状态:0失效;1生效',
    delete_flag        TINYINT      DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by          BIGINT COMMENT '创建人',
    create_time        TIMESTAMP COMMENT '创建时间',
    update_by          BIGINT COMMENT '更新人',
    update_time        TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (industry_id)
) COMMENT = '默认行业表';


CREATE TABLE indicator_category
(
    indicator_category_id   BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    indicator_type          TINYINT COMMENT '指标类型:1财务指标;2业务指标',
    indicator_category_code VARCHAR(32) COMMENT '指标分类编码',
    indicator_category_name VARCHAR(64) COMMENT '指标分类名称',
    delete_flag             TINYINT DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by               BIGINT COMMENT '创建人',
    create_time             TIMESTAMP COMMENT '创建时间',
    update_by               BIGINT COMMENT '更新人',
    update_time             TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (indicator_category_id)
) COMMENT = '指标分类表';


CREATE TABLE indicator
(
    indicator_id          BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    parent_indicator_id   BIGINT       DEFAULT 0 COMMENT '父级指标ID',
    ancestors             VARCHAR(255) DEFAULT '' COMMENT '祖级列表ID，按层级用英文逗号隔开',
    indicator_type        TINYINT COMMENT '指标类型:1财务指标；2业务指标',
    indicator_code        VARCHAR(32) COMMENT '指标编码',
    indicator_name        VARCHAR(64) COMMENT '指标名称',
    sort                  INT COMMENT '排序',
    level                 TINYINT COMMENT '层级',
    indicator_category_id BIGINT COMMENT '指标分类ID',
    indicator_value_type  TINYINT COMMENT '指标值类型:1金额;2比率',
    choice_flag           TINYINT COMMENT '必选标记:0非必选;1必选',
    examine_direction     TINYINT COMMENT '考核方向:0反向;1正向',
    indicator_definition  VARCHAR(255) COMMENT '指标定义',
    indicator_formula     VARCHAR(255) COMMENT '指标公式',
    driving_factor_flag   TINYINT COMMENT '驱动因素标记:0否;1是',
    remark                VARCHAR(255) COMMENT '备注',
    delete_flag           TINYINT      DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by             BIGINT COMMENT '创建人',
    create_time           TIMESTAMP COMMENT '创建时间',
    update_by             BIGINT COMMENT '更新人',
    update_time           TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (indicator_id)
) COMMENT = '指标表';


CREATE TABLE official_rank_system
(
    official_rank_system_id   BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    official_rank_system_name VARCHAR(64) COMMENT '职级体系名称',
    rank_prefix_code          VARCHAR(16) COMMENT '级别前缀编码',
    rank_start                INT COMMENT '起始级别',
    rank_end                  INT COMMENT '终止级别',
    rank_decompose_dimension  TINYINT COMMENT '职级分解维度:1部门;2区域;3省份;4产品',
    status                    TINYINT COMMENT '状态:0失效;1生效',
    delete_flag               TINYINT DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by                 BIGINT COMMENT '创建人',
    create_time               TIMESTAMP COMMENT '创建时间',
    update_by                 BIGINT COMMENT '更新人',
    update_time               TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (official_rank_system_id)
) COMMENT = '职级体系表';


CREATE TABLE official_rank_decompose
(
    official_rank_decompose_id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    official_rank_system_id    VARCHAR(255) COMMENT '职级体系ID',
    rank_decompose_dimension   TINYINT COMMENT '职级分解维度:1部门;2区域;3省份;4产品',
    decompose_dimension        BIGINT COMMENT '具体分解维度的ID',
    salary_factor              DECIMAL(4, 1) COMMENT '工资系数',
    delete_flag                TINYINT DEFAULT 0 COMMENT '删除标记:0未删除;1已删除',
    create_by                  BIGINT COMMENT '创建人',
    create_time                TIMESTAMP COMMENT '创建时间',
    update_by                  BIGINT COMMENT '更新人',
    update_time                TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (official_rank_decompose_id)
) COMMENT = '职级分解表';


CREATE TABLE config(
    config_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    parent_config_id BIGINT    COMMENT '父级配置ID' ,
    path_code VARCHAR(256)    COMMENT '节点路径code,用英文.做连接' ,
    config_code VARCHAR(128)    COMMENT '配置编码' ,
    config_value VARCHAR(64)    COMMENT '配置值' ,
    status TINYINT    COMMENT '状态:0失效;1生效' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (config_id)
)  COMMENT = '配置表';

CREATE TABLE nation(
    nation_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    nation_name VARCHAR(64)    COMMENT '民族名称' ,
    sort INT    COMMENT '排序' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (nation_id)
)  COMMENT = '民族表';

CREATE TABLE country(
    country_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    parent_country_id BIGINT    COMMENT '父级国家ID' ,
    country_name VARCHAR(128)    COMMENT '国家名称' ,
    sort INT    COMMENT '排序' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (country_id)
)  COMMENT = '国家表';


CREATE TABLE region(
    region_id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    parent_region_id BIGINT    COMMENT '父级区域ID' ,
    ancestors VARCHAR(256)    COMMENT '祖级列表ID，按层级用英文逗号隔开' ,
    region_name VARCHAR(64)    COMMENT '区域名称' ,
    province_code VARCHAR(32)    COMMENT '省级区划编号' ,
    province_name VARCHAR(64)    COMMENT '省级名称' ,
    city_code VARCHAR(32)    COMMENT '市级区划编号' ,
    city_name VARCHAR(64)    COMMENT '市级名称' ,
    district_code VARCHAR(32)    COMMENT '区级区划编号' ,
    district_name VARCHAR(64)    COMMENT '区级名称' ,
    town_code VARCHAR(32)    COMMENT '镇级区划编号' ,
    town_name VARCHAR(64)    COMMENT '镇级名称' ,
    village_code VARCHAR(32)    COMMENT '村级区划编号' ,
    village_name VARCHAR(64)    COMMENT '村级名称' ,
    level TINYINT    COMMENT '层级' ,
    sort INT    COMMENT '排序' ,
    delete_flag TINYINT   DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT    COMMENT '创建人' ,
    create_time TIMESTAMP    COMMENT '创建时间' ,
    update_by BIGINT    COMMENT '更新人' ,
    update_time TIMESTAMP    COMMENT '更新时间' ,
    PRIMARY KEY (region_id)
)  COMMENT = '区域表';
