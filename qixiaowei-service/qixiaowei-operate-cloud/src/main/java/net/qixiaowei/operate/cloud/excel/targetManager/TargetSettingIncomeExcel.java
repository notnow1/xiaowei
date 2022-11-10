package net.qixiaowei.operate.cloud.excel.targetManager;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 目标制定收入表
 *
 * @author Graves
 * @since 2022-11-10
 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class TargetSettingIncomeExcel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelIgnore
    @ExcelProperty("ID")
    private Long targetSettingIncomeId;
    /**
     * 目标制定ID
     */
    @ExcelIgnore
    @ExcelProperty("目标制定ID")
    private Long targetSettingId;
    /**
     * 一年前订单金额
     */
    @ExcelIgnore
    @ExcelProperty("一年前订单金额")
    private BigDecimal moneyBeforeOne;
    /**
     * 两年前订单金额
     */
    @ExcelIgnore
    @ExcelProperty("两年前订单金额")
    private BigDecimal moneyBeforeTwo;
    /**
     * 三年前订单金额
     */
    @ExcelIgnore
    @ExcelProperty("三年前订单金额")
    private BigDecimal moneyBeforeThree;
    /**
     * 一年前订单转化率
     */
    @ExcelIgnore
    @ExcelProperty("一年前订单转化率")
    private BigDecimal conversionBeforeOne;
    /**
     * 两年前订单转化率
     */
    @ExcelIgnore
    @ExcelProperty("两年前订单转化率")
    private BigDecimal conversionBeforeTwo;
    /**
     * 三年前订单转化率
     */
    @ExcelIgnore
    @ExcelProperty("三年前订单转化率")
    private BigDecimal conversionBeforeThree;
    /**
     * 删除标记:0未删除;1已删除
     */
    @ExcelIgnore
    @ExcelProperty("删除标记:0未删除;1已删除")
    private Integer deleteFlag;
    /**
     * 目标年度
     */
    @ExcelProperty("目标年度")
    private Integer targetYear;
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
     * 目标年度订单转化率（%）
     */
    @ExcelProperty("目标年度订单转化率（%）")
    private BigDecimal percentage;
}

