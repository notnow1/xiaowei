package net.qixiaowei.operate.cloud.excel.performance;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

/**
* 绩效考核对象表
* @author Graves
* @since 2022-11-24
*/
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class PerformanceAppraisalObjectsExcel{

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     @ExcelIgnore
     @ExcelProperty("ID")
     private  Long  performAppraisalObjectsId;
     /**
     * 绩效考核ID
     */
     @ExcelIgnore
     @ExcelProperty("绩效考核ID")
     private  Long  performanceAppraisalId;
     /**
     * 考核对象ID
     */
     @ExcelIgnore
     @ExcelProperty("考核对象ID")
     private  Long  appraisalObjectId;
     /**
     * 考核负责人ID
     */
     @ExcelIgnore
     @ExcelProperty("考核负责人ID")
     private  Long  appraisalPrincipalId;
     /**
     * 评议分数
     */
     @ExcelIgnore
     @ExcelProperty("评议分数")
     private BigDecimal evaluationScore;
     /**
     * 考核结果(绩效等级ID)
     */
     @ExcelIgnore
     @ExcelProperty("考核结果(绩效等级ID)")
     private  Long  appraisalResultId;
     /**
     * 自定义列标记:0否;1是
     */
     @ExcelIgnore
     @ExcelProperty("自定义列标记:0否;1是")
     private  Integer  selfDefinedColumnsFlag;
     /**
     * 考核对象状态:1制定目标;2评议;3排名;4归档
     */
     @ExcelIgnore
     @ExcelProperty("考核对象状态:1制定目标;2评议;3排名;4归档")
     private  Integer  appraisalObjectStatus;
     /**
     * 删除标记:0未删除;1已删除
     */
     @ExcelIgnore
     @ExcelProperty("删除标记:0未删除;1已删除")
     private  Integer  deleteFlag;

}

