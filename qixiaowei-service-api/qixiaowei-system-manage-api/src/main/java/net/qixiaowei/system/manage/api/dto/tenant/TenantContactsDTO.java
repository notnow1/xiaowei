package net.qixiaowei.system.manage.api.dto.tenant;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.utils.Phone;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
* 租户联系人表
* @author TANGMICHI
* @since 2022-09-24
*/
@Data
@Accessors(chain = true)
public class TenantContactsDTO {
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
    @NotBlank(message = "联系人姓名不能为空")
    private  String contactName;
    /**
    * 联系人手机号
    */
    @NotBlank(message = "联系人手机号不能为空")
    @Pattern(regexp = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$", message = "手机号码有误！")
    private  String contactTel;
    /**
    * 联系人邮箱
    */
    @NotBlank(message = "联系人邮箱不能为空")
    @Email
    private  String contactEmail;
    /**
    * 联系人职务
    */
    @NotBlank(message = "联系人职务不能为空")
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

