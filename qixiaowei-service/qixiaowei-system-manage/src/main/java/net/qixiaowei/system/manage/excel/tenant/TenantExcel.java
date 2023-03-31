package net.qixiaowei.system.manage.excel.tenant;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 租户表
 */
@Data
@ColumnWidth(16)
@HeadRowHeight(16)
@ContentRowHeight(16)
public class TenantExcel {
    private static final long serialVersionUID = 1L;

    /**
     * 租户编码
     */
    @ExcelProperty("企业编码")
    private  String tenantCode;

    /**
     * 租户名称
     */
    @ExcelProperty("企业名称")
    private  String tenantName;

    /**
     * 租户行业名称
     */
    @ExcelProperty("企业行业")
    private  String tenantIndustryName;


    /**
     * 状态（0待初始化 1正常 2禁用 3过期）
     */
    @ExcelProperty("租赁状态")
    private  String tenantStatusName;
}
