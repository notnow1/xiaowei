package net.qixiaowei.operate.cloud.excel.targetManager;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

/**
* 目标分解历史版本表
* @author TANGMICHI
* @since 2022-10-31
*/
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class TargetDecomposeHistoryExcel{

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     @ExcelIgnore
     @ExcelProperty("ID")
     private  Long  targetDecomposeHistoryId;
     /**
     * 目标分解ID
     */
     @ExcelIgnore
     @ExcelProperty("目标分解ID")
     private  Long  targetDecomposeId;
     /**
     * 版本号
     */
     @ExcelIgnore
     @ExcelProperty("版本号")
     private  String  version;
     /**
     * 预测周期
     */
     @ExcelIgnore
     @ExcelProperty("预测周期")
     private  String  forecastCycle;
     /**
     * 删除标记:0未删除;1已删除
     */
     @ExcelIgnore
     @ExcelProperty("删除标记:0未删除;1已删除")
     private  Integer  deleteFlag;

}

