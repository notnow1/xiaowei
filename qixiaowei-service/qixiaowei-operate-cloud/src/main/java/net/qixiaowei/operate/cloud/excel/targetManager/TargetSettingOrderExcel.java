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
     * ID
     */
    @ExcelIgnore
    @ExcelProperty("ID")
    private Long targetSettingOrderId;
    /**
     * 目标制定ID
     */
    @ExcelIgnore
    @ExcelProperty("目标制定ID")
    private Long targetSettingId;
    /**
     * 历史年度
     */
    @ExcelIgnore
    @ExcelProperty("历史年度")
    private Integer historyYear;
    /**
     * 历史年度实际值
     */
    @ExcelIgnore
    @ExcelProperty("历史年度实际值")
    private BigDecimal historyActual;
    /**
     * 删除标记:0未删除;1已删除
     */
    @ExcelIgnore
    @ExcelProperty("删除标记:0未删除;1已删除")
    private Integer deleteFlag;

}

