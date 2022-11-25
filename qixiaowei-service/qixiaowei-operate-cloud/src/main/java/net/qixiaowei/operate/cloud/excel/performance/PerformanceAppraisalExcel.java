package net.qixiaowei.operate.cloud.excel.performance;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

/**
* 绩效考核表
* @author Graves
* @since 2022-11-24
*/
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class PerformanceAppraisalExcel{

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     @ExcelIgnore
     @ExcelProperty("ID")
     private  Long  performanceAppraisalId;
     /**
     * 绩效等级ID
     */
     @ExcelIgnore
     @ExcelProperty("绩效等级ID")
     private  Long  performanceRankId;
     /**
     * 考核年度
     */
     @ExcelIgnore
     @ExcelProperty("考核年度")
     private  Integer  appraisalYear;
     /**
     * 考核名称
     */
     @ExcelIgnore
     @ExcelProperty("考核名称")
     private  String  appraisalName;
     /**
     * 周期性考核标记:0否;1是
     */
     @ExcelIgnore
     @ExcelProperty("周期性考核标记:0否;1是")
     private  Integer  cycleFlag;
     /**
     * 周期类型:1月度;2季度;3半年度;4年度
     */
     @ExcelIgnore
     @ExcelProperty("周期类型:1月度;2季度;3半年度;4年度")
     private  Integer  cycleType;
     /**
     * 考核周期
     */
     @ExcelIgnore
     @ExcelProperty("考核周期")
     private  Integer  cycleNumber;
     /**
     * 考核开始日期
     */
     @ExcelIgnore
     @ExcelProperty("考核开始日期")
     @DateTimeFormat(value = "yyyy/MM/dd")
     private  Date   appraisalStartDate;
     /**
     * 考核结束日期
     */
     @ExcelIgnore
     @ExcelProperty("考核结束日期")
     @DateTimeFormat(value = "yyyy/MM/dd")
     private  Date   appraisalEndDate;
     /**
     * 归档日期
     */
     @ExcelIgnore
     @ExcelProperty("归档日期")
     @DateTimeFormat(value = "yyyy/MM/dd")
     private  Date   filingDate;
     /**
     * 考核流程:1系统流程;2仅导入结果
     */
     @ExcelIgnore
     @ExcelProperty("考核流程:1系统流程;2仅导入结果")
     private  Integer  appraisalFlow;
     /**
     * 考核对象:1组织;2员工
     */
     @ExcelIgnore
     @ExcelProperty("考核对象:1组织;2员工")
     private  Integer  appraisalObject;
     /**
     * 考核状态:1制定目标;2评议;3排名;4归档
     */
     @ExcelIgnore
     @ExcelProperty("考核状态:1制定目标;2评议;3排名;4归档")
     private  Integer  appraisalStatus;
     /**
     * 删除标记:0未删除;1已删除
     */
     @ExcelIgnore
     @ExcelProperty("删除标记:0未删除;1已删除")
     private  Integer  deleteFlag;

}

