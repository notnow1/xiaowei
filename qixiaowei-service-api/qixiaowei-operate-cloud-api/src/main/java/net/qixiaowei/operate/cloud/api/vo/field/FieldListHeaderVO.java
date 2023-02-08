package net.qixiaowei.operate.cloud.api.vo.field;

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
public class FieldListHeaderVO {

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
     * 固定标记:0否;1是
     */
    private Integer fixationFlag;

}

