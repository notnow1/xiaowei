package net.qixiaowei.system.manage.api.dto.tenant;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 租户联系人表
* @author TANGMICHI
* @since 2022-10-09
*/
@Data
@Accessors(chain = true)
public class TenantContactsDTO {

    //查询检验
    public interface QueryTenantContactsDTO extends Default{

    }
    //新增检验
    public interface AddTenantContactsDTO extends Default{

    }

    //删除检验
    public interface DeleteTenantContactsDTO extends Default{

    }
    //修改检验
    public interface UpdateTenantContactsDTO extends Default{

    }
    /**
    * ID
    */
    private  Long tenantContactsId;
    /**
    * 租户ID
    */
    private  Long tenantId;
    /**
    * 联系人姓名
    */
    private  String contactName;
    /**
    * 联系人手机号
    */
    private  String contactTel;
    /**
    * 联系人邮箱
    */
    private  String contactEmail;
    /**
    * 联系人职务
    */
    private  String contactDuty;
    /**
    * 备注
    */
    private  String remark;
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

