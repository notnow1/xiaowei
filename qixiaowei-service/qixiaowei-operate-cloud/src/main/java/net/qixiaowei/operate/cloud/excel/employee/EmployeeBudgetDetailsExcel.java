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
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDetailsDTO;

import javax.validation.constraints.NotNull;

/**
 * 人力预算明细表
 *
 * @author TANGMICHI
 * @since 2022-11-25
 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class EmployeeBudgetDetailsExcel {

    private static final long serialVersionUID = 1L;

    /**
     * 预算部门名称
     */
    @ExcelProperty("部门")
    private String departmentName;
    /**
     * 职级体系名称
     */
    @ExcelProperty("职级体系")
    private String officialRankSystemName;

    /**
     * 岗位职级名称
     */
    @ExcelProperty("岗位职级")
    private String officialRankName;

    /**
     * 规划新增人数
     */
    @ExcelProperty("规划新增人数")
    private Integer countAdjust;

    /**
     * 平均规划新增人数
     */
    @ExcelProperty("平均规划新增人数")
    private BigDecimal averageAdjust;

    /**
     * 上年平均工资
     */
    @ExcelProperty("上年平均工资")
    private BigDecimal agePayAmountLastYear;

    /**
     * 增人/减人工资包
     */
    @ExcelProperty("增人/减人工资包")
    private BigDecimal increaseAndDecreasePay;

}

