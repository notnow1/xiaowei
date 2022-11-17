package net.qixiaowei.operate.cloud.excel.salary;



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
* 工资发薪明细表
* @author Graves
* @since 2022-11-17
*/
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class SalaryPayDetailsExcel{

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     @ExcelIgnore
     @ExcelProperty("ID")
     private  Long  salaryPayDetailsId;
     /**
     * 工资发薪ID
     */
     @ExcelIgnore
     @ExcelProperty("工资发薪ID")
     private  Long  salaryPayId;
     /**
     * 工资项ID
     */
     @ExcelIgnore
     @ExcelProperty("工资项ID")
     private  Long  salaryItemId;
     /**
     * 金额(单位/元)
     */
     @ExcelIgnore
     @ExcelProperty("金额(单位/元)")
     private BigDecimal amount;
     /**
     * 排序
     */
     @ExcelIgnore
     @ExcelProperty("排序")
     private  Integer  sort;
     /**
     * 删除标记:0未删除;1已删除
     */
     @ExcelIgnore
     @ExcelProperty("删除标记:0未删除;1已删除")
     private  Integer  deleteFlag;

}

