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
* 个人调薪计划表
* @author Graves
* @since 2022-12-14
*/
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class EmpSalaryAdjustPlanExcel{

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     @ExcelIgnore
     @ExcelProperty("ID")
     private  Long  empSalaryAdjustPlanId;
     /**
     * 员工ID
     */
     @ExcelIgnore
     @ExcelProperty("员工ID")
     private  Long  employeeId;
     /**
     * 生效日期
     */
     @ExcelIgnore
     @ExcelProperty("生效日期")
     @DateTimeFormat(value = "yyyy/MM/dd")
     private  Date   effectiveDate;
     /**
     * 调整类型(1调岗;2调级;3调薪),多个用英文逗号隔开
     */
     @ExcelIgnore
     @ExcelProperty("调整类型(1调岗;2调级;3调薪),多个用英文逗号隔开")
     private  String  adjustmentType;
     /**
     * 调整部门ID
     */
     @ExcelIgnore
     @ExcelProperty("调整部门ID")
     private  Long  adjustDepartmentId;
     /**
     * 调整部门名称
     */
     @ExcelIgnore
     @ExcelProperty("调整部门名称")
     private  String  adjustDepartmentName;
     /**
     * 调整岗位ID
     */
     @ExcelIgnore
     @ExcelProperty("调整岗位ID")
     private  Long  adjustPostId;
     /**
     * 调整岗位名称
     */
     @ExcelIgnore
     @ExcelProperty("调整岗位名称")
     private  String  adjustPostName;
     /**
     * 调整职级体系ID
     */
     @ExcelIgnore
     @ExcelProperty("调整职级体系ID")
     private  Long  officialRankSystemId;
     /**
     * 调整职级
     */
     @ExcelIgnore
     @ExcelProperty("调整职级")
     private  Integer  adjustOfficialRank;
     /**
     * 调整职级名称
     */
     @ExcelIgnore
     @ExcelProperty("调整职级名称")
     private  String  adjustOfficialRankName;
     /**
     * 调整薪酬
     */
     @ExcelIgnore
     @ExcelProperty("调整薪酬")
     private BigDecimal adjustEmolument;
     /**
     * 调整说明
     */
     @ExcelIgnore
     @ExcelProperty("调整说明")
     private  String  adjustExplain;
     /**
     * 状态(0草稿;1生效)
     */
     @ExcelIgnore
     @ExcelProperty("状态(0草稿;1生效)")
     private  Integer  status;
     /**
     * 删除标记:0未删除;1已删除
     */
     @ExcelIgnore
     @ExcelProperty("删除标记:0未删除;1已删除")
     private  Integer  deleteFlag;
     /**
     * 租户ID
     */
     @ExcelIgnore
     @ExcelProperty("租户ID")
     private  Long  tenantId;

}

