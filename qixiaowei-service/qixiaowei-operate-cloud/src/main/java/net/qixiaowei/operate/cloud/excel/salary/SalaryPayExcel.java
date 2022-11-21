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
 * 工资发薪表
 *
 * @author Graves
 * @since 2022-11-17
 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class SalaryPayExcel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelIgnore
    @ExcelProperty("ID")
    private Long salaryPayId;
    /**
     * 员工ID
     */
    @ExcelProperty("员工ID")
    private Long employeeId;
    /**
     * 发薪年份
     */
    @ExcelIgnore
    @ExcelProperty("发薪年份")
    private Integer payYear;
    /**
     * 发薪月份
     */
    @ExcelIgnore
    @ExcelProperty("发薪月份")
    private Integer payMonth;
    /**
     * 发薪年月
     */
    @ExcelProperty("发薪年月")
    private Date payTime;
    /**
     * 工资金额
     */
    @ExcelIgnore
    @ExcelProperty("工资金额")
    private BigDecimal salaryAmount;
    /**
     * 津贴金额
     */
    @ExcelIgnore
    @ExcelProperty("津贴金额")
    private BigDecimal allowanceAmount;
    /**
     * 福利金额
     */
    @ExcelIgnore
    @ExcelProperty("福利金额")
    private BigDecimal welfareAmount;
    /**
     * 奖金金额
     */
    @ExcelIgnore
    @ExcelProperty("奖金金额")
    private BigDecimal bonusAmount;
    /**
     * 代扣代缴金额
     */
    @ExcelIgnore
    @ExcelProperty("代扣代缴金额")
    private BigDecimal withholdRemitTax;
    /**
     * 其他扣款金额
     */
    @ExcelIgnore
    @ExcelProperty("其他扣款金额")
    private BigDecimal otherDeductions;
    /**
     * 发薪金额
     */
    @ExcelIgnore
    @ExcelProperty("发薪金额")
    private BigDecimal payAmount;
    /**
     * 删除标记:0未删除;1已删除
     */
    @ExcelIgnore
    @ExcelProperty("删除标记:0未删除;1已删除")
    private Integer deleteFlag;

}

