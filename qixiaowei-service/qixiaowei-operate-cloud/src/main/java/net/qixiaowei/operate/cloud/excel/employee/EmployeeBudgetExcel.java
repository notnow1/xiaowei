package net.qixiaowei.operate.cloud.excel.employee;



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
* 人力预算表
* @author TANGMICHI
* @since 2022-11-18
*/
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class EmployeeBudgetExcel{

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     @ExcelIgnore
     @ExcelProperty("ID")
     private  Long  employeeBudgetId;
     /**
     * 预算年度
     */
     @ExcelIgnore
     @ExcelProperty("预算年度")
     private  Integer  budgetYear;
     /**
     * 预算部门ID
     */
     @ExcelIgnore
     @ExcelProperty("预算部门ID")
     private  Long  departmentId;
     /**
     * 职级体系ID
     */
     @ExcelIgnore
     @ExcelProperty("职级体系ID")
     private  Long  officialRankSystemId;
     /**
     * 预算周期:1季度;2月度
     */
     @ExcelIgnore
     @ExcelProperty("预算周期:1季度;2月度")
     private  Integer  budgetCycle;
     /**
     * 上年期末人数
     */
     @ExcelIgnore
     @ExcelProperty("上年期末人数")
     private  Integer  amountLastYear;
     /**
     * 本年新增人数
     */
     @ExcelIgnore
     @ExcelProperty("本年新增人数")
     private  Integer  amountAdjust;
     /**
     * 平均新增数
     */
     @ExcelIgnore
     @ExcelProperty("平均新增数")
     private BigDecimal amountAverageAdjust;
     /**
     * 删除标记:0未删除;1已删除
     */
     @ExcelIgnore
     @ExcelProperty("删除标记:0未删除;1已删除")
     private  Integer  deleteFlag;

}

