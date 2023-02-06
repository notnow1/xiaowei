USE `system_manage`;
SET NAMES utf8mb4;

-- ----------------------------
-- tenant_contract_auth add
-- ----------------------------
CREATE TABLE tenant_contract_auth(
    tenant_contract_auth_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    tenant_contract_id BIGINT UNSIGNED NOT NULL   COMMENT '租户合同ID' ,
    menu_id BIGINT UNSIGNED NOT NULL   COMMENT '菜单ID' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    PRIMARY KEY (tenant_contract_auth_id)
)  COMMENT = '租户合同授权表';