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
* 目标制定回款表
* @author Graves
* @since 2022-11-10
*/
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class TargetSettingRecoveryExcel{

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     @ExcelIgnore
     @ExcelProperty("ID")
     private  Long  targetSettingRecoveriesId;
     /**
     * 目标制定ID
     */
     @ExcelIgnore
     @ExcelProperty("目标制定ID")
     private  Long  targetSettingId;
     /**
     * 上年年末应收账款余额
     */
     @ExcelIgnore
     @ExcelProperty("上年年末应收账款余额")
     private BigDecimal balanceReceivables;
     /**
     * DSO(应收账款周转天数)基线
     */
     @ExcelIgnore
     @ExcelProperty("DSO(应收账款周转天数)基线")
     private  Integer  baselineValue;
     /**
     * DSO(应收账款周转天数)改进天数
     */
     @ExcelIgnore
     @ExcelProperty("DSO(应收账款周转天数)改进天数")
     private  Integer  improveDays;
     /**
     * 删除标记:0未删除;1已删除
     */
     @ExcelIgnore
     @ExcelProperty("删除标记:0未删除;1已删除")
     private  Integer  deleteFlag;

}

