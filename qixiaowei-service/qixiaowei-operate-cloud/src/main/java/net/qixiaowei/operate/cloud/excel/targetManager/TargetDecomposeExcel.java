package net.qixiaowei.operate.cloud.excel.targetManager;


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
 * 目标分解表
 *
 * @author TANGMICHI
 * @since 2022-10-27
 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
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
    @ExcelProperty("目标年度")
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
    @ExcelProperty("分解维度")
    private String decompositionDimension;
    /**
     * 时间维度:1年度;2半年度;3季度;4月度;5周
     */
    @ExcelIgnore
    @ExcelProperty("时间维度")
    private Integer timeDimension;

    /**
     * 时间维度:1年度;2半年度;3季度;4月度;5周
     */
    @ExcelProperty("时间维度")
    private String timeDimensionName;
    /**
     * 目标值(公司目标)
     */
    @ExcelProperty("公司目标")
    private BigDecimal targetValue;

    /**
     * 分解目标值
     */
    @ExcelProperty("分解目标")
    private BigDecimal decomposeTarget;

    /**
     * 目标差异
     */
    @ExcelProperty("目标差异")
    private BigDecimal targetDifference;

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

