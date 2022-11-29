package net.qixiaowei.system.manage.api.domain.basic;

import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

/**
* 指标分类表
* @author Graves
* @since 2022-09-28
*/
@Data
@Accessors(chain = true)
public class IndicatorCategory extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  indicatorCategoryId;
     /**
     * 指标类型:1财务指标；2业务指标
     */
     private  Integer  indicatorType;
     /**
     * 指标分类编码
     */
     private  String  indicatorCategoryCode;
     /**
     * 指标分类名称
     */
     private  String  indicatorCategoryName;

}

