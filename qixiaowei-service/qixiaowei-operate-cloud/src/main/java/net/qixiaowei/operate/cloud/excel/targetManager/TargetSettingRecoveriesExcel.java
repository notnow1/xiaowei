package net.qixiaowei.operate.cloud.excel.targetManager;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 目标制定回款集合表
 *
 * @author Graves
 * @since 2022-11-10
 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class TargetSettingRecoveriesExcel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelIgnore
    @ExcelProperty("ID")
    private Long targetSettingRecoveriesId;
    /**
     * 目标制定ID
     */
    @ExcelIgnore
    @ExcelProperty("目标制定ID")
    private Long targetSettingId;
    /**
     * 类型:1:应回尽回;2:逾期清理;3:提前还款;4:销售收入目标;5:期末应收账款余额
     */
    @ExcelIgnore
    @ExcelProperty("类型:1:应回尽回;2:逾期清理;3:提前还款;4:销售收入目标;5:期末应收账款余额")
    private Integer type;
    /**
     * 上年实际值
     */
    @ExcelIgnore
    @ExcelProperty("上年实际值")
    private BigDecimal actualLastYear;
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
     * 删除标记:0未删除;1已删除
     */
    @ExcelIgnore
    @ExcelProperty("删除标记:0未删除;1已删除")
    private Integer deleteFlag;

}

