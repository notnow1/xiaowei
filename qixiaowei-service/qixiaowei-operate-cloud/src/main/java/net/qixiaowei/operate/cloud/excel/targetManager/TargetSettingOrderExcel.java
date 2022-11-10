package net.qixiaowei.operate.cloud.excel.targetManager;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 目标制定订单表
 *
 * @author Graves
 * @since 2022-11-10
 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class TargetSettingOrderExcel {

    private static final long serialVersionUID = 1L;

    /**
     * 历史年度
     */
    @ExcelProperty("目标年度")
    private Integer historyYear;
    /**
     * 公司战略增长诉求（%）
     */
    @ExcelProperty("公司战略增长诉求（%）")
    private BigDecimal percentage;
    /**
     * 目标制定ID
     */
    @ExcelIgnore
    @ExcelProperty("目标制定ID")
    private Long targetSettingId;
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
}

