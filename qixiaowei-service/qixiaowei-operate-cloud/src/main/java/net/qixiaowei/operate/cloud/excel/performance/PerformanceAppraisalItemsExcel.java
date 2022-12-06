package net.qixiaowei.operate.cloud.excel.performance;


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
 * 绩效考核项目表
 *
 * @author Graves
 * @since 2022-12-06
 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class PerformanceAppraisalItemsExcel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelIgnore
    @ExcelProperty("ID")
    private Long performAppraisalItemsId;
    /**
     * 绩效考核对象ID
     */
    @ExcelIgnore
    @ExcelProperty("绩效考核对象ID")
    private Long performAppraisalObjectsId;
    /**
     * 指标ID
     */
    @ExcelIgnore
    @ExcelProperty("指标ID")
    private Long indicatorId;
    /**
     * 指标名称
     */
    @ExcelIgnore
    @ExcelProperty("指标名称")
    private String indicatorName;
    /**
     * 指标值类型:1金额;2比率
     */
    @ExcelIgnore
    @ExcelProperty("指标值类型:1金额;2比率")
    private Integer indicatorValueType;
    /**
     * 考核方向:0反向;1正向
     */
    @ExcelIgnore
    @ExcelProperty("考核方向:0反向;1正向")
    private Integer examineDirection;
    /**
     * 挑战值
     */
    @ExcelIgnore
    @ExcelProperty("挑战值")
    private BigDecimal challengeValue;
    /**
     * 目标值
     */
    @ExcelIgnore
    @ExcelProperty("目标值")
    private BigDecimal targetValue;
    /**
     * 保底值
     */
    @ExcelIgnore
    @ExcelProperty("保底值")
    private BigDecimal guaranteedValue;
    /**
     * 实际值
     */
    @ExcelIgnore
    @ExcelProperty("实际值")
    private BigDecimal actualValue;
    /**
     * 权重百分比(%)
     */
    @ExcelIgnore
    @ExcelProperty("权重百分比(%)")
    private BigDecimal weight;
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

