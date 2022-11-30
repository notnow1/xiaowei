package net.qixiaowei.operate.cloud.api.dto.salary;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 部门奖金预算明细表
* @author TANGMICHI
* @since 2022-11-30
*/
@Data
@Accessors(chain = true)
public class DeptBonusBudgetDetailsDTO {

    //查询检验
    public interface QueryDeptBonusBudgetDetailsDTO extends Default{

    }
    //新增检验
    public interface AddDeptBonusBudgetDetailsDTO extends Default{

    }

    //删除检验
    public interface DeleteDeptBonusBudgetDetailsDTO extends Default{

    }
    //修改检验
    public interface UpdateDeptBonusBudgetDetailsDTO extends Default{

    }
    /**
    * ID
    */
    private  Long deptBonusBudgetDetailsId;
    /**
    * 部门奖金包预算ID
    */
    private  Long deptBonusBudgetId;
    /**
    * 部门ID
    */
    private  Long departmentId;
    /**
    * 部门奖金占比
    */
    private BigDecimal deptBonusPercentage;
    /**
    * 部门重要性系数
    */
    private  BigDecimal departmentImportanceFactor;
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

