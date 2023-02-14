USE `operate_cloud`;
SET NAMES utf8mb4;
-- ----------------------------
-- field_config、field_list_config add
-- ----------------------------
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