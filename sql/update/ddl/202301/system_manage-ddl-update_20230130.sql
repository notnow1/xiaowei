USE `system_manage`;
SET NAMES utf8mb4;

-- ----------------------------
-- user_config add
-- ----------------------------
CREATE TABLE user_config(
    user_config_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    user_id BIGINT UNSIGNED NOT NULL   COMMENT '用户ID' ,
    user_config_type TINYINT UNSIGNED NOT NULL   COMMENT '用户配置类型' ,
    user_config_value TEXT    COMMENT '用户配置值' ,
    status TINYINT UNSIGNED NOT NULL   COMMENT '状态:0关闭;1启用' ,
    remark VARCHAR(512)    COMMENT '备注' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (user_config_id)
)  COMMENT = '用户配置表';