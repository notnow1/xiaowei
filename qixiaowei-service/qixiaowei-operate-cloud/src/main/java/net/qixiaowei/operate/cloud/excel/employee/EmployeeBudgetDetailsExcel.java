package net.qixiaowei.operate.cloud.excel.employee;


import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDetailsDTO;

import javax.validation.constraints.NotNull;

import static com.alibaba.excel.enums.BooleanEnum.TRUE;

/**
 * 人力预算明细表
 *
 * @author TANGMICHI
 * @since 2022-11-25
 */
@Data
@HeadRowHeight(16)
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@HeadFontStyle(fontName = "微软雅黑", bold = TRUE, fontHeightInPoints = 11)
@ContentFontStyle(fontName = "微软雅黑")
@ContentRowHeight(15)
public class EmployeeBudgetDetailsExcel {

    private static final long serialVersionUID = 1L;

    /**
     * 预算部门名称
     */
    @ExcelProperty("部门")
    @ColumnWidth(16)
    private String departmentName;
    /**
     * 职级体系名称
     */
    @ExcelProperty("职级体系")
    @ColumnWidth(16)
    private String officialRankSystemName;

    /**
     * 岗位职级名称
     */
    @ExcelProperty("岗位职级")
    @ColumnWidth(16)
    private String officialRankName;

    /**
     * 规划新增人数
     */
    @ExcelProperty("规划新增人数")
    @ColumnWidth(16)
    private Integer countAdjust;

    /**
     * 平均规划新增人数
     */
    @ExcelProperty("平均规划新增人数")
    @ColumnWidth(16)
    private BigDecimal averageAdjust;

    /**
     * 上年平均工资
     */
    @ExcelProperty("上年平均工资")
    @ColumnWidth(16)
    private BigDecimal agePayAmountLastYear;

    /**
     * 增人/减人工资包
     */
    @ExcelProperty("增人/减人工资包")
    @ColumnWidth(16)
    private BigDecimal increaseAndDecreasePay;

}

