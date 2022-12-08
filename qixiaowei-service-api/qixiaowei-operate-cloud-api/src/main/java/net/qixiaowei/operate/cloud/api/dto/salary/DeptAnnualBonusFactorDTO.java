package net.qixiaowei.operate.cloud.api.dto.salary;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 部门年终奖系数表
* @author TANGMICHI
* @since 2022-12-06
*/
@Data
@Accessors(chain = true)
public class DeptAnnualBonusFactorDTO {

    //查询检验
    public interface QueryDeptAnnualBonusFactorDTO extends Default{

    }
    //新增检验
    public interface AddDeptAnnualBonusFactorDTO extends Default{

    }

    //删除检验
    public interface DeleteDeptAnnualBonusFactorDTO extends Default{

    }
    //修改检验
    public interface UpdateDeptAnnualBonusFactorDTO extends Default{

    }
    /**
    * ID
    */
    private  Long deptAnnualBonusFactorId;
    /**
    * 部门年终奖ID
    */
    private  Long deptAnnualBonusId;
    /**
    * 部门ID
    */
    private  Long departmentId;
    /**
     * 部门名称
     */
    private  String departmentName;
    /**
    * 权重
    */
    private  BigDecimal weight;
    /**
    * 最近绩效结果
    */
    private  String lastPerformanceResulted;
    /**
    * 绩效等级ID
    */
    private  Long performanceRankId;
    /**
     *绩效名称集合
     */
    private List<String> performanceRanks;
    /**
    * 绩效等级系数ID
    */
    private  Long performanceRankFactorId;
    /**
    * 绩效
    */
    private  String performanceRank;
    /**
    * 组织绩效奖金系数
    */
    private  BigDecimal performanceBonusFactor;
    /**
    * 组织重要性系数
    */
    private  BigDecimal importanceFactor;
    /**
     * 奖金综合系数
     */
    private  BigDecimal syntheticalBonusFactor;
    /**
    * 奖金包占比终值（%)
    */
    private  BigDecimal bonusPercentage;

    /**
     * 部门奖金包占比参考值
     */
    private BigDecimal deptBonusPercentageReference;
    /**
    * 可分配年终奖
    */
    private  BigDecimal distributeBonus;
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
    /**
    * 租户ID
    */
    private  Long tenantId;

}

