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
* 人力预算明细表
* @author TANGMICHI
* @since 2022-11-25
*/
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class EmployeeBudgetDetailsExcel{

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     @ExcelIgnore
     @ExcelProperty("ID")
     private  Long  employeeBudgetDetailsId;
     /**
     * 人力预算ID
     */
     @ExcelIgnore
     @ExcelProperty("人力预算ID")
     private  Long  employeeBudgetId;
     /**
     * 岗位职级
     */
     @ExcelIgnore
     @ExcelProperty("岗位职级")
     private  Integer  officialRank;
     /**
     * 上年期末人数
     */
     @ExcelIgnore
     @ExcelProperty("上年期末人数")
     private  Integer  numberLastYear;
     /**
     * 本年新增人数
     */
     @ExcelIgnore
     @ExcelProperty("本年新增人数")
     private  Integer  countAdjust;
     /**
     * 平均新增数
     */
     @ExcelIgnore
     @ExcelProperty("平均新增数")
     private BigDecimal averageAdjust;
     /**
     * 删除标记:0未删除;1已删除
     */
     @ExcelIgnore
     @ExcelProperty("删除标记:0未删除;1已删除")
     private  Integer  deleteFlag;

}

