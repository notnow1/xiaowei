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
* 目标结果详情表
* @author Graves
* @since 2022-11-10
*/
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class TargetOutcomeDetailsExcel{

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     @ExcelIgnore
     @ExcelProperty("ID")
     private  Long  targetOutcomeDetailsId;
     /**
     * 目标结果ID
     */
     @ExcelIgnore
     @ExcelProperty("目标结果ID")
     private  Long  targetOutcomeId;
     /**
     * 指标ID
     */
     @ExcelIgnore
     @ExcelProperty("指标ID")
     private  Long  indicatorId;
     /**
     * 累计实际值
     */
     @ExcelIgnore
     @ExcelProperty("累计实际值")
     private  BigDecimal  actualTotal;
     /**
     * 一月实际值
     */
     @ExcelIgnore
     @ExcelProperty("一月实际值")
     private  BigDecimal  actualJanuary;
     /**
     * 二月实际值
     */
     @ExcelIgnore
     @ExcelProperty("二月实际值")
     private  BigDecimal  actualFebruary;
     /**
     * 三月实际值
     */
     @ExcelIgnore
     @ExcelProperty("三月实际值")
     private BigDecimal actualMarch;
     /**
     * 四月实际值
     */
     @ExcelIgnore
     @ExcelProperty("四月实际值")
     private  BigDecimal  actualApril;
     /**
     * 五月实际值
     */
     @ExcelIgnore
     @ExcelProperty("五月实际值")
     private  BigDecimal  actualMay;
     /**
     * 六月实际值
     */
     @ExcelIgnore
     @ExcelProperty("六月实际值")
     private  BigDecimal  actualJune;
     /**
     * 七月实际值
     */
     @ExcelIgnore
     @ExcelProperty("七月实际值")
     private  BigDecimal  actualJuly;
     /**
     * 八月实际值
     */
     @ExcelIgnore
     @ExcelProperty("八月实际值")
     private  BigDecimal  actualAugust;
     /**
     * 九月实际值
     */
     @ExcelIgnore
     @ExcelProperty("九月实际值")
     private  BigDecimal  actualSeptember;
     /**
     * 十月实际值
     */
     @ExcelIgnore
     @ExcelProperty("十月实际值")
     private  BigDecimal  actualOctober;
     /**
     * 十一月实际值
     */
     @ExcelIgnore
     @ExcelProperty("十一月实际值")
     private  BigDecimal  actualNovember;
     /**
     * 十二月实际值
     */
     @ExcelIgnore
     @ExcelProperty("十二月实际值")
     private  BigDecimal  actualDecember;
     /**
     * 删除标记:0未删除;1已删除
     */
     @ExcelIgnore
     @ExcelProperty("删除标记:0未删除;1已删除")
     private  Integer  deleteFlag;

}

