package net.qixiaowei.operate.cloud.api.dto.employee;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

/**
* 人力预算调整表
* @author TANGMICHI
* @since 2022-11-22
*/
@Data
@Accessors(chain = true)
public class EmployeeBudgetAdjustsDTO extends BaseDTO {

    //查询检验
    public interface QueryEmployeeBudgetAdjustsDTO extends Default{

    }
    //新增检验
    public interface AddEmployeeBudgetAdjustsDTO extends Default{

    }

    //删除检验
    public interface DeleteEmployeeBudgetAdjustsDTO extends Default{

    }
    //修改检验
    public interface UpdateEmployeeBudgetAdjustsDTO extends Default{

    }
    /**
    * ID
    */
    private  Long employeeBudgetAdjustsId;
    /**
    * 人力预算明细ID
    */
    private  Long employeeBudgetDetailsId;
    /**
    * 周期数(顺序递增)
    */
    private  Integer cycleNumber;
    /**
    * 调整人数
    */
    @NotNull(message = "调整人数人数不能为空", groups = {EmployeeBudgetAdjustsDTO.AddEmployeeBudgetAdjustsDTO.class, EmployeeBudgetAdjustsDTO.UpdateEmployeeBudgetAdjustsDTO.class})
    private  Integer numberAdjust;

}

