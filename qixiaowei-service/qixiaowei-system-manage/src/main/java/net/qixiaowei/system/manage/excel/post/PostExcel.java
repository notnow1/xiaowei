package net.qixiaowei.system.manage.excel.post;



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
* 岗位表
* @author TANGMICHI
* @since 2023-02-02
*/
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class PostExcel{


     /**
     * 岗位编码
     */
     @ExcelIgnore
     @ExcelProperty("岗位编码")
     private  String  postCode;
     /**
     * 岗位名称
     */
     @ExcelIgnore
     @ExcelProperty("岗位名称")
     private  String  postName;
     /**
     * 职级体系名称
     */
     @ExcelIgnore
     @ExcelProperty("职级体系ID")
     private  String  officialRankSystemName;
     /**
      * 岗位职级上限
      */
     @ExcelIgnore
     @ExcelProperty("岗位职级上限")
     private  String  postRankUpperName;
     /**
     * 岗位职级下限
     */
     @ExcelIgnore
     @ExcelProperty("岗位职级下限")
     private  String  postRankLowerName;

     /**
     * 状态:失效;生效
     */
     @ExcelIgnore
     @ExcelProperty("状态:失效;生效")
     private  String  status;
     /**
      * 部门名称(excel用)
      */
     @ExcelIgnore
     @ExcelProperty("部门名称(excel用)")
     private String parentDepartmentExcelName;

}

