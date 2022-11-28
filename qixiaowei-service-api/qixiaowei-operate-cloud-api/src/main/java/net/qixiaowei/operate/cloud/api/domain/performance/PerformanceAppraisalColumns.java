package net.qixiaowei.operate.cloud.api.domain.performance;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 绩效考核自定义列表
* @author Graves
* @since 2022-11-28
*/
@Data
@Accessors(chain = true)
public class PerformanceAppraisalColumns extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  performAppraisalColumnsId;
     /**
     * 绩效考核ID
     */
     private  Long  performanceAppraisalId;
     /**
     * 列名
     */
     private  String  columnName;
     /**
     * 列值(所有行集合的JSON格式数据)
     */
     private  String  columnValue;
     /**
     * 排序
     */
     private  Integer  sort;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

