package net.qixiaowei.operate.cloud.excel.targetManager;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import lombok.Data;

import java.math.BigDecimal;

import static com.alibaba.excel.enums.BooleanEnum.TRUE;

/**
 * 目标制定订单表
 *
 * @author Graves
 * @since 2022-11-10
 */
@Data
@HeadRowHeight(16)
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@HeadFontStyle(fontName = "微软雅黑", bold = TRUE, fontHeightInPoints = 11)
@ContentFontStyle(fontName = "微软雅黑")
@ContentRowHeight(15)
public class TargetSettingOrderExcel {

    private static final long serialVersionUID = 1L;

    /**
     * 历史年度
     */
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币     单位：万元", "目标年度"})
    private Integer historyYear;
    /**
     * 公司战略增长诉求（%）
     */
    @ColumnWidth(22)
    @ExcelProperty(value = {"币种：人民币     单位：万元", "公司战略增长诉求（%）"})
    private BigDecimal percentage;
    /**
     * 目标制定ID
     */
    @ExcelIgnore
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币     单位：万元", "目标制定ID"})
    private Long targetSettingId;
    /**
     * 挑战值
     */
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币     单位：万元", "挑战值"})
    private BigDecimal challengeValue;
    /**
     * 目标值
     */
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币     单位：万元", "目标值"})
    private BigDecimal targetValue;
    /**
     * 保底值
     */
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币     单位：万元", "保底值"})
    private BigDecimal guaranteedValue;
}

