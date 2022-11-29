package net.qixiaowei.system.manage.api.domain.basic;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.math.BigDecimal;

/**
* 部门表
* @author TANGMICHI
* @since 2022-09-27
*/
@Data
@Accessors(chain = true)
public class Department extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * 部门ID
     */
     private  Long  departmentId;
     /**
     * 父级部门ID
     */
     private  Long  parentDepartmentId;
     /**
     * 祖级列表ID，按层级用英文逗号隔开
     */
     private  String  ancestors;
     /**
     * 部门编码
     */
     private  String  departmentCode;
     /**
     * 部门名称
     */
     private  String  departmentName;
     /**
     * 部门层级
     */
     private  Integer  level;
     /**
     * 部门负责人ID
     */
     private  Long  departmentLeaderId;
     /**
     * 部门负责人岗位ID
     */
     private  Long  departmentLeaderPostId;
     /**
     * 考核负责人ID
     */
     private  Long  examinationLeaderId;
     /**
     * 部门重要性系数
     */
     private BigDecimal departmentImportanceFactor;
     /**
     * 部门描述
     */
     private  String  departmentDescription;
     /**
     * 排序
     */
     private  Integer  sort;
     /**
     * 状态:0失效;1生效
     */
     private  Integer  status;

}

