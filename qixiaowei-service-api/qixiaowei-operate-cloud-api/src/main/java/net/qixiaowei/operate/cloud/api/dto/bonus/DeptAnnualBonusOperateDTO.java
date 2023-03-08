package net.qixiaowei.operate.cloud.api.dto.bonus;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 部门年终奖经营绩效结果表
* @author TANGMICHI
* @since 2022-12-06
*/
@Data
@Accessors(chain = true)
public class DeptAnnualBonusOperateDTO {

    //查询检验
    public interface QueryDeptAnnualBonusOperateDTO extends Default{

    }
    //新增检验
    public interface AddDeptAnnualBonusOperateDTO extends Default{

    }

    //删除检验
    public interface DeleteDeptAnnualBonusOperateDTO extends Default{

    }
    //修改检验
    public interface UpdateDeptAnnualBonusOperateDTO extends Default{

    }
    /**
    * ID
    */
    private  Long deptAnnualBonusOperateId;
    /**
    * 部门年终奖ID
    */
    private  Long deptAnnualBonusId;
    /**
    * 指标ID
    */
    private  Long indicatorId;
    /**
    * 指标名称
    */
    private  String indicatorName;
    /**
     * 奖金权重(%)
     */
    private  BigDecimal bonusWeight;
    /**
    * 目标值
    */
    private  BigDecimal targetValue;
    /**
    * 实际值
    */
    private  BigDecimal actualValue;
    /**
     * 目标超额完成率（%）
     */
    private  BigDecimal targetExcessPerComp;
    /**
     *奖金系数（实际）
     */
    private  BigDecimal actualPerformanceBonusFactor;
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
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;
    /**
    * 租户ID
    */
    private  Long tenantId;

}

