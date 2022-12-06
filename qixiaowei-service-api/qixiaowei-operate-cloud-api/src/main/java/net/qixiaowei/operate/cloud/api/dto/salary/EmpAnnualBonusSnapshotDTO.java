package net.qixiaowei.operate.cloud.api.dto.salary;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.util.List;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 个人年终奖发放快照信息表
* @author TANGMICHI
* @since 2022-12-02
*/
@Data
@Accessors(chain = true)
public class EmpAnnualBonusSnapshotDTO {

    //查询检验
    public interface QueryEmpAnnualBonusSnapshotDTO extends Default{

    }
    //新增检验
    public interface AddEmpAnnualBonusSnapshotDTO extends Default{

    }

    //删除检验
    public interface DeleteEmpAnnualBonusSnapshotDTO extends Default{

    }
    //修改检验
    public interface UpdateEmpAnnualBonusSnapshotDTO extends Default{

    }
    /**
    * ID
    */
    private  Long empAnnualBonusSnapshotId;
    /**
    * 个人年终奖ID
    */
    private  Long employeeAnnualBonusId;
    /**
    * 个人年终奖发放对象ID
    */
    private  Long empAnnualBonusObjectsId;
    /**
    * 员工姓名
    */
    private  String employeeName;
    /**
    * 员工工号
    */
    private  String employeeCode;
    /**
    * 部门名称
    */
    private  String departmentName;
    /**
    * 岗位名称
    */
    private  String postName;
    /**
    * 职级名称
    */
    private  String officialRankName;
    /**
    * 司龄
    */
    private  String seniority;
    /**
    * 基本工资
    */
    private  BigDecimal employeeBasicWage;
    /**
    * 前一年总薪酬
    */
    private  BigDecimal emolumentBeforeOne;
    /**
    * 前一年奖金
    */
    private  BigDecimal bonusBeforeOne;
    /**
    * 前二年奖金
    */
    private BigDecimal bonusBeforeTwo;
    /**
    * 最近绩效结果
    */
    private  String lastPerformanceResulted;
    /**
    * 奖金占比一
    */
    private  BigDecimal bonusPercentageOne;
    /**
    * 奖金占比二
    */
    private  BigDecimal bonusPercentageTwo;
    /**
    * 参考值一
    */
    private  BigDecimal referenceValueOne;
    /**
    * 参考值二
    */
    private  BigDecimal referenceValueTwo;
    /**
     * 员工ID
     */
    private  Long employeeId;
    /**
     * 选中标记:0否;1是
     */
    private  Integer choiceFlag;
    /**
     * 绩效等级系数ID
     */
    private  Long performanceRankFactorId;
    /**
     * 绩效等级ID
     */
    private  Long performanceRankId;
    /**
     *绩效名称集合
     */
    private List<String> performanceRanks;
    /**
     * 绩效名称
     */
    private  String performanceRank;
    /**
     * 绩效奖金系数
     */
    private BigDecimal performanceBonusFactor;
    /**
     * 考勤系数
     */
    private  BigDecimal attendanceFactor;
    /**
     * 建议值
     */
    private  BigDecimal recommendValue;
    /**
     * 评议值
     */
    private  BigDecimal commentValue;

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

