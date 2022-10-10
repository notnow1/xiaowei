package net.qixiaowei.system.manage.api.dto.basic;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 员工信息
* @author TANGMICHI
* @since 2022-10-09
*/
@Data
@Accessors(chain = true)
public class EmployeeInfoDTO {

    //查询检验
    public interface QueryEmployeeInfoDTO extends Default{

    }
    //新增检验
    public interface AddEmployeeInfoDTO extends Default{

    }

    //删除检验
    public interface DeleteEmployeeInfoDTO extends Default{

    }
    //修改检验
    public interface UpdateEmployeeInfoDTO extends Default{

    }
    /**
    * ID
    */
    private  Long employeeInfoId;
    /**
    * 员工ID
    */
    private  Long employeeId;
    /**
    * 婚姻状况:0未婚;1已婚
    */
    private  Integer maritalStatus;
    /**
    * 国籍
    */
    private  String nationality;
    /**
    * 民族
    */
    private  String nation;
    /**
    * 户口所在地
    */
    private  String residentCity;
    /**
    * 参保地
    */
    private  String insuredCity;
    /**
    * 常住地
    */
    private  String permanentAddress;
    /**
    * 通信地址
    */
    private  String contactAddress;
    /**
    * 通信地址详情
    */
    private  String contactAddressDetail;
    /**
    * 微信号
    */
    private  String wechatCode;
    /**
    * 紧急联系人
    */
    private  String emergencyContact;
    /**
    * 紧急联系人电话
    */
    private  String emergencyMobile;
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

