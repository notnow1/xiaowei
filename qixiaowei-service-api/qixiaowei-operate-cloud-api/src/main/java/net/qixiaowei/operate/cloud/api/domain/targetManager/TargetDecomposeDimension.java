package net.qixiaowei.operate.cloud.api.domain.targetManager;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 目标分解维度配置
* @author Graves
* @since 2022-09-26
*/
@Data
@Accessors(chain = true)
public class TargetDecomposeDimension extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  targetDecomposeDimensionId;
     /**
     * 分解维度(region,salesman,department,product,province,industry)
     */
     private  String  decompositionDimension;
     /**
     * 排序
     */
     private  Integer  sort;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

