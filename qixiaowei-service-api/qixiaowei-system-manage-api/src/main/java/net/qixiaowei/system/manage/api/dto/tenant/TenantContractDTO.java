package net.qixiaowei.system.manage.api.dto.tenant;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
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
    private  String salesContractNo;
    /**
    * 销售人员
    */
    private  String salesPersonnel;
    /**
    * 合同金额
    */
    private BigDecimal contractAmount;
    /**
    * 合同开始时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private  Date  contractStartTime;
    /**
    * 合同结束时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private  Date  contractEndTime;
    /**
    * 开通的产品包
    */
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

