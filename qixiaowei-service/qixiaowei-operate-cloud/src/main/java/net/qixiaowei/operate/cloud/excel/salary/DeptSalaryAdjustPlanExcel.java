package net.qixiaowei.operate.cloud.excel.salary;



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
* 部门调薪计划表
* @author Graves
* @since 2022-12-11
*/
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class DeptSalaryAdjustPlanExcel{

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     @ExcelIgnore
     @ExcelProperty("ID")
     private  Long  deptSalaryAdjustPlanId;
     /**
     * 预算年度
     */
     @ExcelIgnore
     @ExcelProperty("预算年度")
     private  Integer  planYear;
     /**
     * 调薪总数
     */
     @ExcelIgnore
     @ExcelProperty("调薪总数")
     private  BigDecimal  salaryAdjustTotal;
     /**
     * 删除标记:0未删除;1已删除
     */
     @ExcelIgnore
     @ExcelProperty("删除标记:0未删除;1已删除")
     private  Integer  deleteFlag;
     /**
     * 租户ID
     */
     @ExcelIgnore
     @ExcelProperty("租户ID")
     private  Long  tenantId;

}

