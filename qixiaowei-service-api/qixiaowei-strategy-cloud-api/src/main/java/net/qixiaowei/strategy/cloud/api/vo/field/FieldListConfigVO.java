package net.qixiaowei.strategy.cloud.api.vo.field;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 字段列表头部表
 *
 * @author hzk
 * @since 2023-02-08
 */
@Data
@Accessors(chain = true)
public class FieldListConfigVO {

    /**
     * ID
     */
    private Long fieldListConfigId;
    /**
     * 业务类型
     */
    private Integer businessType;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 字段标签
     */
    private String fieldLabel;
    /**
     * 字段类型
     */
    private Integer fieldType;
    /**
     * 字段宽度
     */
    private Integer fieldWidth;
    /**
     * 显示标记:0否;1是
     */
    private Integer showFlag;
    /**
     * 固定标记:0否;1是
     */
    private Integer fixationFlag;
    /**
     * 强制显示:0否;1是
     */
    private Integer showForce;
    /**
     * 强制固定:0否;1是
     */
    private Integer fixationForce;

}

