package net.qixiaowei.operate.cloud.excel.targetManager;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 目标分解详情表
 *
 * @author TANGMICHI
 * @since 2022-10-27
 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class TargetDecomposeDetailsExcel {
    /**
     *分解维度数据集合
     */
    @ExcelIgnore
    private List<String> decompositionDimensions;

    /**
     * 负责人名称
     */
    @ExcelIgnore
    private  String principalEmployeeName;

    /**
     * 汇总金额
     */
    @ExcelIgnore
    private  String amountTarget;
    /**
     *周期目标值集合
     */
    @ExcelIgnore
    private List<String> cycleTargets;

    /**
     *周期目标值集合
     */
    @ExcelIgnore
    private List<String> cycleTargetSum;
    /**
     * 汇总金额合计
     */
    @ExcelIgnore
    private String amountTargetSum;
}

