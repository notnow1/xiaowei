USE `operate_cloud`;
SET NAMES utf8mb4;
-- ----------------------------
-- perform_appraisal_evaluate add
-- ----------------------------
CREATE TABLE perform_appraisal_evaluate(
    perform_appraisal_evaluate_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT 'ID' ,
    perform_appraisal_objects_id BIGINT UNSIGNED NOT NULL   COMMENT '绩效考核对象ID' ,
    perform_appraisal_items_id BIGINT UNSIGNED NOT NULL   COMMENT '绩效考核项目ID' ,
    evaluate_number TINYINT UNSIGNED NOT NULL   COMMENT '评议周期' ,
    actual_value DECIMAL(18,2)    COMMENT '实际值' ,
    delete_flag TINYINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '删除标记:0未删除;1已删除' ,
    create_by BIGINT UNSIGNED NOT NULL   COMMENT '创建人' ,
    create_time DATETIME NOT NULL   COMMENT '创建时间' ,
    update_by BIGINT UNSIGNED NOT NULL   COMMENT '更新人' ,
    update_time DATETIME NOT NULL   COMMENT '更新时间' ,
    tenant_id BIGINT UNSIGNED NOT NULL  DEFAULT 0 COMMENT '租户ID' ,
    PRIMARY KEY (perform_appraisal_evaluate_id)
)  COMMENT = '绩效考核评议表';

-- ----------------------------
-- perform_appraisal update
-- ----------------------------
ALTER TABLE performance_appraisal ADD evaluation_type TINYINT UNSIGNED NOT NULL COMMENT '评议周期类型:1月度;2季度;3半年度;4年度' AFTER cycle_type;

ALTER TABLE performance_appraisal_objects ADD remark VARCHAR(512)    COMMENT '备注' AFTER sort;

ALTER TABLE performance_appraisal_items ADD remark VARCHAR(512)    COMMENT '备注' AFTER weight;
