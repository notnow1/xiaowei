package net.qixiaowei.operate.cloud.excel.salary;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import lombok.Data;

import java.math.BigDecimal;

import static com.alibaba.excel.enums.BooleanEnum.TRUE;

/**
 * 职级薪酬表
 *
 * @author Graves
 * @since 2022-11-30
 */
@Data
@HeadRowHeight(16)
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@HeadFontStyle(fontName = "微软雅黑", bold = TRUE, fontHeightInPoints = 11)
@ContentFontStyle(fontName = "微软雅黑")
@ContentRowHeight(16)
public class ErrorOfficialRankEmolumentExcel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelIgnore
    @ExcelProperty("ID")
    private Long officialRankEmolumentId;
    /**
     * 错误提示
     */
    @ExcelIgnore
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币   单位：元", "职级体系：管理序列", "职级"})
    private Long officialRankSystemId;
    /**
     * 职级体系ID
     */
    @ColumnWidth(22)
    @ExcelProperty(value = {"币种：人民币   单位：元", "职级体系：管理序列", "错误信息"})
    private String error;
    /**
     * 职级
     */
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币   单位：元", "职级体系：管理序列", "职级"})
    private String officialRankName;
    /**
     * 工资上限
     */
    @ColumnWidth(16)
    @ContentStyle(dataFormat = 2)
    @ExcelProperty(value = {"币种：人民币   单位：元", "职级体系：管理序列", "工资上限"})
    private BigDecimal salaryCap;
    /**
     * 工资下限
     */
    @ColumnWidth(16)
    @ContentStyle(dataFormat = 2)
    @ExcelProperty(value = {"币种：人民币   单位：元", "职级体系：管理序列", "工资下限"})
    private BigDecimal salaryFloor;
    /**
     * 工资中位数
     */
    @ColumnWidth(16)
    @ContentStyle(dataFormat = 2)
    @ExcelProperty(value = {"币种：人民币   单位：元", "职级体系：管理序列", "工资中位数"})
    private BigDecimal salaryMedian;
    /**
     * 删除标记:0未删除;1已删除
     */
    @ExcelIgnore
    @ExcelProperty("删除标记:0未删除;1已删除")
    private Integer deleteFlag;

}

