package net.qixiaowei.system.manage.api.dto.tenant;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
* 租户合同信息
* @author TANGMICHI
* @since 2022-09-24
*/
@Data
@Accessors(chain = true)
public class TenantContractDTO {
    /**
    * ID
    */
    private  Long tenantContractId;
    /**
    * 租户ID
    */
    private  Long tenantId;
    /**
    * 销售合同编号
    */
    @NotBlank(message = "销售合同编号不能为空")
    private  String salesContractNo;
    /**
    * 销售人员
    */
    @NotBlank(message = "销售人员不能为空")
    private  String salesPersonnel;
    /**
    * 合同金额
    */
    @NotNull(message = "合同金额不能为空")
    @DecimalMax("999999999999")
    private BigDecimal contractAmount;
    /**
    * 合同开始时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull(message = "租赁开始时间不能为空")
    private  Date  contractStartTime;
    /**
    * 合同结束时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull (message = "租赁结束时间不能为空")
    private  Date  contractEndTime;
    /**
    * 开通的产品包
    */
    @NotBlank(message = "租赁模块不能为空")
    private  String productPackage;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 创建人
    */
    private  Long createBy;
    /**
    * 创建时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;

}

