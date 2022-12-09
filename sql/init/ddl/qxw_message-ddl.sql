DROP DATABASE IF EXISTS `qxw_message`;
CREATE DATABASE `qxw_message` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `qxw_message`;
SET NAMES utf8mb4;


CREATE TABLE message(
    message_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    business_type SMALLINT UNSIGNED NOT NULL   COMMENT '业务类型' ,
    business_subtype SMALLINT UNSIGNED NOT NULL   COMMENT '业务子类型' ,
    business_id BIGINT UNSIGNED NOT NULL   COMMENT '业务ID' ,
    send_user_id BIGINT UNSIGNED NOT NULL   COMMENT '发送用户ID' ,
    message_title VARCHAR(128)    COMMENT '消息标题' ,
    message_content TEXT    COMMENT '消息内容' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (message_id)
)  COMMENT = '消息表';


CREATE TABLE message_receive(
    message_receive_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    message_id BIGINT UNSIGNED NOT NULL   COMMENT '消息ID' ,
    user_id BIGINT UNSIGNED NOT NULL   COMMENT '用户ID' ,
    read_time DATETIME    COMMENT '已读时间' ,
    status TINYINT UNSIGNED NOT NULL   COMMENT '状态:0未读;1已读' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (message_receive_id)
)  COMMENT = '消息接收表';


CREATE TABLE message_content_config(
    message_content_config_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    message_type TINYINT UNSIGNED NOT NULL   COMMENT '消息类型' ,
    business_type SMALLINT UNSIGNED NOT NULL   COMMENT '业务类型' ,
    business_subtype SMALLINT UNSIGNED NOT NULL   COMMENT '业务子类型' ,
    receive_role TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '接受者角色(0普通角色)' ,
    message_template TEXT    COMMENT '消息模版' ,
    receive_user VARCHAR(256)    COMMENT '消息额外用户，用英文逗号分隔' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    PRIMARY KEY (message_content_config_id)
)  COMMENT = '消息内容配置表';


