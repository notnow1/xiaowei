package net.qixiaowei.operate.cloud.api.dto.bonus;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 个人年终奖发放对象表
* @author TANGMICHI
* @since 2022-12-02
*/
@Data
@Accessors(chain = true)
public class EmpAnnualBonusObjectsDTO {

    //查询检验
    public interface QueryEmpAnnualBonusObjectsDTO extends Default{

    }
    //新增检验
    public interface AddEmpAnnualBonusObjectsDTO extends Default{

    }

    //删除检验
    public interface DeleteEmpAnnualBonusObjectsDTO extends Default{

    }
    //修改检验
    public interface UpdateEmpAnnualBonusObjectsDTO extends Default{

    }
    /**
    * ID
    */
    private  Long empAnnualBonusObjectsId;
    /**
    * 个人年终奖ID
    */
    private  Long employeeAnnualBonusId;
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
     * 状态:0草稿;1待初评;2待评议;3已评议
     */
    private  Integer status;
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

