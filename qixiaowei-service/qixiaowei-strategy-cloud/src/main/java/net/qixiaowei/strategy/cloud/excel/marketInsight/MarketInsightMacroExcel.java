package net.qixiaowei.strategy.cloud.excel.marketInsight;



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
* 市场洞察宏观表
* @author TANGMICHI
* @since 2023-02-28
*/
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class MarketInsightMacroExcel{

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     @ExcelIgnore
     @ExcelProperty("ID")
     private  Long  marketInsightMacroId;
     /**
     * 规划年度
     */
     @ExcelIgnore
     @ExcelProperty("规划年度")
     private  Integer  planYear;
     /**
     * 规划业务单元ID
     */
     @ExcelIgnore
     @ExcelProperty("规划业务单元ID")
     private  Long  planBusinessUnitId;
     /**
     * 规划业务单元维度(region,department,product,industry)
     */
     @ExcelIgnore
     @ExcelProperty("规划业务单元维度(region,department,product,industry)")
     private  String  businessUnitDecompose;
     /**
     * 区域ID
     */
     @ExcelIgnore
     @ExcelProperty("区域ID")
     private  Long  areaId;
     /**
     * 部门ID
     */
     @ExcelIgnore
     @ExcelProperty("部门ID")
     private  Long  departmentId;
     /**
     * 产品ID
     */
     @ExcelIgnore
     @ExcelProperty("产品ID")
     private  Long  productId;
     /**
     * 行业ID
     */
     @ExcelIgnore
     @ExcelProperty("行业ID")
     private  Long  industryId;
     /**
     * 删除标记:0未删除;1已删除
     */
     @ExcelIgnore
     @ExcelProperty("删除标记:0未删除;1已删除")
     private  Integer  deleteFlag;


}

