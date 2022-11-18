package net.qixiaowei.operate.cloud.api.dto.salary;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 人力预算表
* @author TANGMICHI
* @since 2022-11-18
*/
@Data
@Accessors(chain = true)
public class EmployeeBudgetDTO {

    //查询检验
    public interface QueryEmployeeBudgetDTO extends Default{

    }
    //新增检验
    public interface AddEmployeeBudgetDTO extends Default{

    }

    //删除检验
    public interface DeleteEmployeeBudgetDTO extends Default{

    }
    //修改检验
    public interface UpdateEmployeeBudgetDTO extends Default{

    }
    /**
    * ID
    */
    private  Long employeeBudgetId;
    /**
    * 预算年度
    */
    private  Integer budgetYear;
    /**
    * 预算部门ID
    */
    private  Long departmentId;
    /**
    * 职级体系ID
    */
    private  Long officialRankSystemId;
    /**
    * 预算周期:1季度;2月度
    */
    private  Integer budgetCycle;
    /**
    * 上年期末人数
    */
    private  Integer amountLastYear;
    /**
    * 本年新增人数
    */
    private  Integer amountAdjust;
    /**
    * 平均新增数
    */
    private BigDecimal amountAverageAdjust;
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

