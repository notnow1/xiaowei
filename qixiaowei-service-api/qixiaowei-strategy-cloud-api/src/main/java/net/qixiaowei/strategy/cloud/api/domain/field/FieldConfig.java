package net.qixiaowei.strategy.cloud.api.domain.field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

/**
 * 字段配置表
 *
 * @author hzk
 * @since 2023-02-08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class FieldConfig extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long fieldConfigId;
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

}

