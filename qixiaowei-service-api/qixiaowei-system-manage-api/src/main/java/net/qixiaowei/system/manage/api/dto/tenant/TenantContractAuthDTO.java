package net.qixiaowei.system.manage.api.dto.tenant;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 租户合同授权表
* @author hzk
* @since 2023-01-31
*/
@Data
@Accessors(chain = true)
public class TenantContractAuthDTO {

    //查询检验
    public interface QueryTenantContractAuthDTO extends Default{

    }
    //新增检验
    public interface AddTenantContractAuthDTO extends Default{

    }

    //删除检验
    public interface DeleteTenantContractAuthDTO extends Default{

    }
    //修改检验
    public interface UpdateTenantContractAuthDTO extends Default{

    }
    /**
    * ID
    */
    private  Long tenantContractAuthId;
    /**
    * 租户合同ID
    */
    private  Long tenantContractId;
    /**
    * 菜单ID
    */
    private  Long menuId;
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
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private  Date  updateTime;

}

