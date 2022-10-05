package net.qixiaowei.system.manage.api.domain.basic;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 员工信息
* @author TANGMICHI
* @since 2022-09-30
*/
@Data
@Accessors(chain = true)
public class EmployeeInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  employeeInfoId;
     /**
     * 员工ID
     */
     private  Long  employeeId;
     /**
     * 婚姻状况:0未婚;1已婚
     */
     private  Integer  maritalStatus;
     /**
     * 国籍
     */
     private  String  nationality;
     /**
     * 民族
     */
     private  String  nation;
     /**
     * 户口所在地
     */
     private  String  residentCity;
     /**
     * 参保地
     */
     private  String  insuredCity;
     /**
     * 常住地
     */
     private  String  permanentAddress;
     /**
     * 通信地址
     */
     private  String  contactAddress;
     /**
     * 通信地址详情
     */
     private  String  contactAddressDetail;
     /**
     * 微信号
     */
     private  String  wechatCode;
     /**
     * 紧急联系人
     */
     private  String  emergencyContact;
     /**
     * 紧急联系人电话
     */
     private  String  emergencyMobile;

}

