package net.qixiaowei.system.manage.api.domain.basic;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;


/**
 * 字典数据表
 *
 * @author TANGMICHI
 * @since 2022-10-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DictionaryData extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long dictionaryDataId;
    /**
     * 字典类型ID
     */
    private Long dictionaryTypeId;
    /**
     * 字典标签
     */
    private String dictionaryLabel;
    /**
     * 字典值
     */
    private String dictionaryValue;
    /**
     * 默认标记:0否;1是
     */
    private Integer defaultFlag;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 状态:0失效;1生效
     */
    private Integer status;

}

