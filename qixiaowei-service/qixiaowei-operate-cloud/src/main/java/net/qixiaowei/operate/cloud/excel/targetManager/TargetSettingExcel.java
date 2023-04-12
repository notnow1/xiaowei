package net.qixiaowei.operate.cloud.excel.targetManager;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import lombok.Data;

import java.math.BigDecimal;

import static com.alibaba.excel.enums.BooleanEnum.TRUE;

/**
 * 目标制定
 *
 * @author Graves
 * @since 2022-10-27
 */
@Data
@HeadRowHeight(16)
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT, wrapped = TRUE)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@HeadFontStyle(fontName = "微软雅黑", bold = TRUE, fontHeightInPoints = 11)
@ContentFontStyle(fontName = "微软雅黑")
@ContentRowHeight(15)
public class TargetSettingExcel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelIgnore
    @ExcelProperty(value = {"币种：人民币     单位：万元", "ID"})
    @ColumnWidth(16)
    private Long targetSettingId;
    /**
     * 目标制定类型:0自定义;1销售订单;2销售收入;3销售回款
     */
    @ExcelIgnore
    @ExcelProperty(value = {"币种：人民币     单位：万元", "目标制定类型:0自定义;1销售订单;2销售收入;3销售回款"})
    private Integer targetSettingType;
    /**
     * 指标ID
     */
    @ExcelIgnore
    @ExcelProperty(value = {"币种：人民币     单位：万元", "指标ID"})
    private Long indicatorId;
    /**
     * 指标ID
     */
    @ExcelProperty(value = {"币种：人民币     单位：万元", "指标名称"})
    @ColumnWidth(16)
    private String indicatorName;
    /**
     * 指标ID
     */
    @ExcelProperty(value = {"币种：人民币     单位：万元", "指标编码"})
    @ColumnWidth(16)
    private String indicatorCode;
    /**
     * 目标年度
     */
    @ExcelIgnore
    @ExcelProperty(value = {"币种：人民币     单位：万元", "目标年度"})
    private Integer targetYear;
    /**
     * 百分比(%)
     */
    @ExcelIgnore
    @ExcelProperty(value = {"币种：人民币     单位：万元", "百分比(%)"})
    private BigDecimal percentage;
    /**
     * 挑战值
     */
    @ExcelProperty(value = {"币种：人民币     单位：万元", "挑战值"})
    @ColumnWidth(16)
    @ContentStyle(dataFormat = 2)
    private BigDecimal challengeValue;
    /**
     * 目标值
     */
    @ExcelProperty(value = {"币种：人民币     单位：万元", "目标值"})
    @ColumnWidth(16)
    @ContentStyle(dataFormat = 2)
    private BigDecimal targetValue;
    /**
     * 保底值
     */
    @ExcelProperty(value = {"币种：人民币     单位：万元", "保底值"})
    @ColumnWidth(16)
    @ContentStyle(dataFormat = 2)
    private BigDecimal guaranteedValue;
    /**
     * 排序
     */
    @ExcelIgnore
    @ExcelProperty(value = {"币种：人民币     单位：万元", "排序"})
    private Integer sort;
    /**
     * 删除标记:0未删除;1已删除
     */
    @ExcelIgnore
    @ExcelProperty(value = {"币种：人民币     单位：万元", "删除标记:0未删除;1已删除"})
    private Integer deleteFlag;

}

