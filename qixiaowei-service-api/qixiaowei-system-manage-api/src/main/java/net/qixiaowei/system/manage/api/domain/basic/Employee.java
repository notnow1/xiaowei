package net.qixiaowei.system.manage.api.domain.basic;


import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 员工表
 *
 * @author TANGMICHI
 * @since 2022-09-30
 */
@Data
@Accessors(chain = true)
public class Employee extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long employeeId;
    /**
     * 人员ID集合
     */
    private List<Long> employeeIds;
    /**
     * 工号
     */
    private String employeeCode;
    /**
     * 姓名
     */
    private String employeeName;
    /**
     * 性别:1男;2女
     */
    private Integer employeeGender;
    /**
     * 证件类型
     */
    private Integer identityType;
    /**
     * 证件号码
     */
    private String identityCard;
    /**
     * 出生日期
     */
    private Date employeeBirthday;
    /**
     * 员工手机号
     */
    private String employeeMobile;
    /**
     * 员工邮箱
     */
    private String employeeEmail;
    /**
     * 员工部门ID
     */
    private Long employeeDepartmentId;
    /**
     * 员工岗位ID
     */
    private Long employeePostId;
    /**
     * 员工职级
     */
    private Integer employeeRank;
    /**
     * 员工基本工资
     */
    private BigDecimal employeeBasicWage;
    /**
     * 入职日期
     */
    private Date employmentDate;
    /**
     * 离职日期
     */
    private Date departureDate;
    /**
     * 用工状态:0离职;1在职
     */
    private Integer employmentStatus;
    /**
     * 状态:0暂存;1生效
     */
    private Integer status;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 员工岗位名称
     */
    private String employeePostName;
    /**
     * 岗位职级
     */
    private String postRankName;
    /**
     * 员工部门名称
     */
    private String employeeDepartmentName;
    /**
     * 个人职级
     */
    private String employeeRankName;
    /**
     * 婚姻状况:0未婚;1已婚
     */
    private  Integer maritalStatus;
    /**
     * 国籍
     */
    private  String nationality;
    private  String nationalityName;
    /**
     * 民族
     */
    private  String nation;
    private  String nationName;
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
     * 户口所在地名称
     */
    private  String residentCityName;
    /**
     * 参保地名称
     */
    private  String insuredCityName;
    /**
     * 常住地名称
     */
    private  String permanentAddressName;
    /**
     * 通信地址名称
     */
    private  String contactAddressName;
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
     * 职级体系ID
     */
    private  Long officialRankSystemId;
}

