package net.qixiaowei.operate.cloud.excel.salary;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 工资条表
 *
 * @author TANGMICHI
 * @since 2022-09-30
 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class SalaryItemExcel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelIgnore
    private Long salaryItemId;
    /**
     * 一级项目:1总工资包;2总奖金包;3总扣减项
     */
    @ExcelProperty("一级工资项目")
    private String firstLevelItem;
    /**
     * 二级项目:1工资;2津贴;3福利;4奖金;5代扣代缴;6其他扣款
     */
    @ExcelProperty("二级工资项目")
    private String secondLevelItem;
    /**
     * 三级项目
     */
    @ExcelProperty("三级工资项目")
    private String thirdLevelItem;
    /**
     * 作用范围：1部门;2公司
     */
    @ExcelProperty("级别")
    private String scope;
    /**
     * 排序
     */
    @ExcelIgnore
    private Integer sort;
    /**
     * 删除标记:0未删除;1已删除
     */
    @ExcelIgnore
    private Integer deleteFlag;

}
