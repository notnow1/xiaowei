package net.qixiaowei.operate.cloud.api.domain.salary;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
* 个人年终奖发放快照信息表
* @author TANGMICHI
* @since 2022-12-02
*/
@Data
@Accessors(chain = true)
public class EmpAnnualBonusSnapshot extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  empAnnualBonusSnapshotId;
     /**
     * 个人年终奖ID
     */
     private  Long  employeeAnnualBonusId;
     /**
     * 个人年终奖发放对象ID
     */
     private  Long  empAnnualBonusObjectsId;
     /**
     * 员工姓名
     */
     private  String  employeeName;
     /**
     * 员工工号
     */
     private  String  employeeCode;
     /**
     * 部门名称
     */
     private  String  departmentName;
     /**
     * 岗位名称
     */
     private  String  postName;
     /**
     * 职级名称
     */
     private  String  officialRankName;
     /**
     * 司龄
     */
     private  String  seniority;
     /**
     * 基本工资
     */
     private  BigDecimal  employeeBasicWage;
     /**
     * 前一年总薪酬
     */
     private BigDecimal emolumentBeforeOne;
     /**
     * 前一年奖金
     */
     private  BigDecimal  bonusBeforeOne;
     /**
     * 前二年奖金
     */
     private  BigDecimal  bonusBeforeTwo;
     /**
     * 最近绩效结果
     */
     private  String  lastPerformanceResulted;
     /**
     * 奖金占比一
     */
     private  BigDecimal  bonusPercentageOne;
     /**
     * 奖金占比二
     */
     private  BigDecimal  bonusPercentageTwo;
     /**
     * 参考值一
     */
     private  BigDecimal  referenceValueOne;
     /**
     * 参考值二
     */
     private  BigDecimal  referenceValueTwo;


}

