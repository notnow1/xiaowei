package net.qixiaowei.operate.cloud.api.dto.bonus;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.List;

/**
* 奖金发放台账工资项集合
* @author TANGMICHI
* @since 2022-12-08
*/
@Data
@Accessors(chain = true)
public class BonusPaySalaryDTO {

    //查询检验
    public interface QueryBonusPaySalaryDTODTO extends Default{

    }
    //新增检验
    public interface AddBonusPaySalaryDTODTO extends Default{

    }

    //删除检验
    public interface DeleteBonusPaySalaryDTODTO extends Default{

    }
    //修改检验
    public interface UpdateBonusPaySalaryDTODTO extends Default{

    }
    /**
     * 奖项类别,工资条ID
     */
    private  Long salaryItemId;
    /**
     * 奖项名称
     */
    private  String awardName;
}

