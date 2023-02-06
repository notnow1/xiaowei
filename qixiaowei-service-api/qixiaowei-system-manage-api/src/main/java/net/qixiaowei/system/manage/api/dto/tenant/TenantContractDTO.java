package net.qixiaowei.system.manage.api.dto.tenant;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 租户合同信息
* @author TANGMICHI
* @since 2022-10-09
*/
@Data
@Accessors(chain = true)
public class TenantContractDTO {

    //查询检验
    public interface QueryTenantContractDTO extends Default{

    }
    //新增检验
    public interface AddTenantContractDTO extends Default{

    }

    //删除检验
    public interface DeleteTenantContractDTO extends Default{

    }
    //修改检验
    public interface UpdateTenantContractDTO extends Default{

    }
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
    @NotBlank(message = "销售合同编号不能为空",groups = {TenantDTO.AddTenantDTO.class,TenantDTO.UpdateTenantDTO.class})
    private  String salesContractNo;
    /**
    * 销售人员
    */
    @NotBlank(message = "销售人员不能为空",groups = {TenantDTO.AddTenantDTO.class,TenantDTO.UpdateTenantDTO.class})
    private  String salesPersonnel;
    /**
    * 合同金额
    */
    @NotNull(message = "合同金额不能为空",groups = {TenantDTO.AddTenantDTO.class,TenantDTO.UpdateTenantDTO.class})
    private BigDecimal contractAmount;
    /**
    * 合同开始时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd",timezone = "GMT+8")
    @NotNull(message = "合同开始时间不能为空",groups = {TenantDTO.AddTenantDTO.class,TenantDTO.UpdateTenantDTO.class})
    private  Date  contractStartTime;
    /**
    * 合同结束时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd",timezone = "GMT+8")
    @NotNull(message = "合同结束时间不能为空",groups = {TenantDTO.AddTenantDTO.class,TenantDTO.UpdateTenantDTO.class})
    private  Date  contractEndTime;
    /**
    * 开通的产品包
    */
    private  String productPackage;

    /**
     * 菜单组
     */
    private Set<Long> menuIds;

    /**
     * 开通的产品包名称
     */
    private  String productPackageName;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;

}

