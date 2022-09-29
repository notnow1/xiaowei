package net.qixiaowei.system.manage.api.domain.basic;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 指标分类表
* @author Graves
* @since 2022-09-28
*/
@Data
@Accessors(chain = true)
public class IndicatorCategory extends BaseEntity {

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
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

