package net.qixiaowei.operate.cloud.excel.targetManager;


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

import static com.alibaba.excel.enums.BooleanEnum.TRUE;

/**
 * 目标分解表
 *
 * @author TANGMICHI
 * @since 2022-10-27
 */
@Data
@HeadRowHeight(16)
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT, wrapped = TRUE)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@HeadFontStyle(fontName = "微软雅黑", bold = TRUE, fontHeightInPoints = 11)
@ContentFontStyle(fontName = "微软雅黑")
@ContentRowHeight(15)
public class TargetDecomposeExcel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelIgnore
    @ExcelProperty("ID")
    private Long targetDecomposeId;
    /**
     * 目标分解类型:0自定义;1销售订单;2销售收入;3销售回款
     */
    @ExcelIgnore
    @ExcelProperty("目标分解类型:0自定义;1销售订单;2销售收入;3销售回款")
    private Integer targetDecomposeType;
    /**
     * 指标ID
     */
    @ExcelIgnore
    @ExcelProperty("指标ID")
    private Long indicatorId;

    /**
     * 目标年度
     */
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币     单位：万元", "目标年度"})
    private Integer targetYear;
    /**
     * 指标名称
     */
    @ExcelIgnore
    private String indicatorName;
    /**
     * 目标分解维度ID
     */
    @ExcelIgnore
    @ExcelProperty("目标分解维度ID")
    private Long targetDecomposeDimensionId;
    /**
     * 分解维度
     */
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币     单位：万元", "分解维度"})
    private String decompositionDimension;
    /**
     * 时间维度:1年度;2半年度;3季度;4月度;5周
     */
    @ExcelIgnore
    @ExcelProperty(value = {"币种：人民币     单位：万元", "时间维度"})
    private Integer timeDimension;

    /**
     * 时间维度:1年度;2半年度;3季度;4月度;5周
     */
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币     单位：万元", "时间维度"})
    private String timeDimensionName;
    /**
     * 目标值(公司目标)
     */
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币     单位：万元", "公司目标"})
    private BigDecimal targetValue;

    /**
     * 分解目标值
     */
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币     单位：万元", "分解目标"})
    private BigDecimal decomposeTarget;


    /**
     * 年度预测值
     */
    @ExcelIgnore
    @ExcelProperty("年度预测值")
    private BigDecimal forecastYear;
    /**
     * 累计实际值
     */
    @ExcelIgnore
    @ExcelProperty("累计实际值")
    private BigDecimal actualTotal;
    /**
     * 状态:0待录入;1已录入
     */
    @ExcelIgnore
    @ExcelProperty("状态:0待录入;1已录入")
    private Integer status;
    /**
     * 删除标记:0未删除;1已删除
     */
    @ExcelIgnore
    @ExcelProperty("删除标记:0未删除;1已删除")
    private Integer deleteFlag;

}

