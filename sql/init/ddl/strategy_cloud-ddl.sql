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