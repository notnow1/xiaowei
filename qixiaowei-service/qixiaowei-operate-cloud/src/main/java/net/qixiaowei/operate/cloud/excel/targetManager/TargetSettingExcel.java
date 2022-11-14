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
 * 目标制定
 *
 * @author Graves
 * @since 2022-10-27
 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class TargetSettingExcel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelIgnore
    @ExcelProperty("ID")
    private Long targetSettingId;
    /**
     * 目标制定类型:0自定义;1销售订单;2销售收入;3销售回款
     */
    @ExcelIgnore
    @ExcelProperty("目标制定类型:0自定义;1销售订单;2销售收入;3销售回款")
    private Integer targetSettingType;
    /**
     * 指标ID
     */
    @ExcelIgnore
    @ExcelProperty("指标ID")
    private Long indicatorId;
    /**
     * 指标ID
     */
    @ExcelProperty("指标名称")
    private String indicatorName;
    /**
     * 指标ID
     */
    @ExcelProperty("指标编码")
    private String indicatorCode;
    /**
     * 目标年度
     */
    @ExcelIgnore
    @ExcelProperty("目标年度")
    private Integer targetYear;
    /**
     * 百分比(%)
     */
    @ExcelIgnore
    @ExcelProperty("百分比(%)")
    private BigDecimal percentage;
    /**
     * 挑战值
     */
    @ExcelProperty("挑战值")
    private BigDecimal challengeValue;
    /**
     * 目标值
     */
    @ExcelProperty("目标值")
    private BigDecimal targetValue;
    /**
     * 保底值
     */
    @ExcelProperty("保底值")
    private BigDecimal guaranteedValue;
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

}

