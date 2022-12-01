package net.qixiaowei.operate.cloud.excel.salary;



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
* 职级薪酬表
* @author Graves
* @since 2022-11-30
*/
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class OfficialRankEmolumentExcel{

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     @ExcelIgnore
     @ExcelProperty("ID")
     private  Long  officialRankEmolumentId;
     /**
     * 职级体系ID
     */
     @ExcelIgnore
     @ExcelProperty("职级体系ID")
     private  Long  officialRankSystemId;
     /**
     * 职级
     */
     @ExcelIgnore
     @ExcelProperty("职级")
     private  Integer  officialRank;
     /**
     * 工资上限
     */
     @ExcelIgnore
     @ExcelProperty("工资上限")
     private BigDecimal salaryCap;
     /**
     * 工资下限
     */
     @ExcelIgnore
     @ExcelProperty("工资下限")
     private  BigDecimal  salaryFloor;
     /**
     * 工资中位数
     */
     @ExcelIgnore
     @ExcelProperty("工资中位数")
     private  BigDecimal  salaryMedian;
     /**
     * 删除标记:0未删除;1已删除
     */
     @ExcelIgnore
     @ExcelProperty("删除标记:0未删除;1已删除")
     private  Integer  deleteFlag;

}

