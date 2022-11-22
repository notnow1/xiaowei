package net.qixiaowei.operate.cloud.api.dto.employee;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.util.List;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 人力预算明细表
* @author TANGMICHI
* @since 2022-11-22
*/
@Data
@Accessors(chain = true)
public class EmployeeBudgetDetailsDTO {

    //查询检验
    public interface QueryEmployeeBudgetDetailsDTO extends Default{

    }
    //新增检验
    public interface AddEmployeeBudgetDetailsDTO extends Default{

    }

    //删除检验
    public interface DeleteEmployeeBudgetDetailsDTO extends Default{

    }
    //修改检验
    public interface UpdateEmployeeBudgetDetailsDTO extends Default{

    }
    /**
    * ID
    */
    private  Long employeeBudgetDetailsId;
    /**
    * 人力预算ID
    */
    private  Long employeeBudgetId;
    /**
    * 岗位职级
    */
    private  Integer officialRank;
    /**
    * 上年期末人数
    */
    private  Integer numberLastYear;
    /**
    * 本年新增人数
    */
    private  Integer countAdjust;
    /**
    * 平均新增数
    */
    private BigDecimal averageAdjust;
    /**
     * 年度平均人数
     */
    private Integer annualAverageNUm;

    /**
     * 人力预算调整表集合
     */
    private List<EmployeeBudgetAdjustsDTO> employeeBudgetAdjustsDTOS;
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

