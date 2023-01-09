USE `system_manage`;
SET NAMES utf8mb4;

CREATE TABLE operation_log(
    operation_log_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    business_type SMALLINT UNSIGNED NOT NULL   COMMENT '业务类型' ,
    business_id BIGINT UNSIGNED NOT NULL   COMMENT '业务ID' ,
    operation_type TINYINT UNSIGNED NOT NULL   COMMENT '操作类型' ,
    operation_time DATETIME NOT NULL   COMMENT '操作时间' ,
    operator_user_account VARCHAR(128)    COMMENT '操作者用户帐号' ,
    operator_employee_name VARCHAR(64)    COMMENT '操作者姓名' ,
    operator_employee_code VARCHAR(32)    COMMENT '操作者工号' ,
    operator_department_name VARCHAR(64)    COMMENT '操作者部门名称' ,
    operator_post_name VARCHAR(64)    COMMENT '操作者岗位名称' ,
    user_agent VARCHAR(256)    COMMENT '用户代理' ,
    request_method VARCHAR(16)    COMMENT '请求方式' ,
    request_url VARCHAR(256)    COMMENT '请求URL' ,
    method VARCHAR(128)    COMMENT '请求方法' ,
    title VARCHAR(64)    COMMENT '标题' ,
    operator_ip VARCHAR(64)    COMMENT '主机地址' ,
    request_param VARCHAR(2048)    COMMENT '请求参数' ,
    result_data VARCHAR(4096)    COMMENT '返回数据' ,
    error_message VARCHAR(2048)    COMMENT '错误消息' ,
    status TINYINT UNSIGNED    COMMENT '操作状态:0错误;1正常' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (operation_log_id)
)  COMMENT = '操作日志表';