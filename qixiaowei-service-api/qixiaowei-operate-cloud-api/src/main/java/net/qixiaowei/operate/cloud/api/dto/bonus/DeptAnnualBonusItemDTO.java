package net.qixiaowei.operate.cloud.api.dto.bonus;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.List;

/**
* 部门可发工资项目年终奖
* @author TANGMICHI
* @since 2022-12-08
*/
@Data
@Accessors(chain = true)
public class DeptAnnualBonusItemDTO {

    //查询检验
    public interface QueryDeptAnnualBonusItemDTO extends Default{

    }
    //新增检验
    public interface AddDeptAnnualBonusItemDTO extends Default{

    }

    //删除检验
    public interface DeleteDeptAnnualBonusItemDTO extends Default{

    }
    //修改检验
    public interface UpdateDeptAnnualBonusItemDTO extends Default{

    }

    /**
     * 部门ID
     */
    private  Long departmentId;
    /**
     * 工资项ID
     */
    private  Long salaryItemId;
    /**
     * 工资项名称
     */
    private  String salaryItemName;
    /**
     * 奖金金额
     */
    private BigDecimal bonusAmount;
}

