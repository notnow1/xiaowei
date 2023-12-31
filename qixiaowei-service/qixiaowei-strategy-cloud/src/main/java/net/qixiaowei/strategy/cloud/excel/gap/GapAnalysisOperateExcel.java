package net.qixiaowei.strategy.cloud.excel.gap;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 差距分析经营情况表
 *
 * @author Graves
 * @since 2023-02-24
 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class GapAnalysisOperateExcel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelIgnore
    @ExcelProperty("ID")
    private Long gapAnalysisOperateId;
    /**
     * 差距分析ID
     */
    @ExcelIgnore
    @ExcelProperty("差距分析ID")
    private Long gapAnalysisId;
    /**
     * 指标ID
     */
    @ExcelIgnore
    @ExcelProperty("指标ID")
    private Long indicatorId;
    /**
     * 经营年度
     */
    @ExcelIgnore
    @ExcelProperty("经营年度")
    private Integer operateYear;
    /**
     * 目标值
     */
    @ExcelIgnore
    @ExcelProperty("目标值")
    private BigDecimal targetValue;
    /**
     * 实际值
     */
    @ExcelIgnore
    @ExcelProperty("实际值")
    private BigDecimal actualValue;
    /**
     * 排序
     */
    @ExcelIgnore
    @ExcelProperty("排序")
    private Integer sort;
    /**
     * 删除标记:0未删除;1已删除
     */
    @ExcelIgnore
    @ExcelProperty("删除标记:0未删除;1已删除")
    private Integer deleteFlag;
    /**
     * 租户ID
     */
    @ExcelIgnore
    @ExcelProperty("租户ID")
    private Long tenantId;

}

