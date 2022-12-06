package net.qixiaowei.operate.cloud.excel.performance;



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
* 绩效考核对象快照表
* @author Graves
* @since 2022-12-05
*/
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class PerformAppraisalObjectSnapExcel{

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     @ExcelIgnore
     @ExcelProperty("ID")
     private  Long  appraisalObjectSnapId;
     /**
     * 绩效考核对象ID
     */
     @ExcelIgnore
     @ExcelProperty("绩效考核对象ID")
     private  Long  performAppraisalObjectsId;
     /**
     * 考核对象名称
     */
     @ExcelIgnore
     @ExcelProperty("考核对象名称")
     private  String  appraisalObjectName;
     /**
     * 考核对象编码
     */
     @ExcelIgnore
     @ExcelProperty("考核对象编码")
     private  String  appraisalObjectCode;
     /**
     * 部门ID
     */
     @ExcelIgnore
     @ExcelProperty("部门ID")
     private  Long  departmentId;
     /**
     * 部门名称
     */
     @ExcelIgnore
     @ExcelProperty("部门名称")
     private  String  departmentName;
     /**
     * 岗位ID
     */
     @ExcelIgnore
     @ExcelProperty("岗位ID")
     private  Long  postId;
     /**
     * 岗位名称
     */
     @ExcelIgnore
     @ExcelProperty("岗位名称")
     private  String  postName;
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
     * 职级名称
     */
     @ExcelIgnore
     @ExcelProperty("职级名称")
     private  String  officialRankName;
     /**
     * 删除标记:0未删除;1已删除
     */
     @ExcelIgnore
     @ExcelProperty("删除标记:0未删除;1已删除")
     private  Integer  deleteFlag;

}

