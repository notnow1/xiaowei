package net.qixiaowei.operate.cloud.api.dto.salary;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;

/**
* 奖金预算参数表
* @author TANGMICHI
* @since 2022-11-26
*/
@Data
@Accessors(chain = true)
public class BonusBudgetLaddertersDTO {

    //查询检验
    public interface QueryBonusBudgetLaddertersDTO extends Default{

    }
    //新增检验
    public interface AddBonusBudgetLaddertersDTO extends Default{

    }

    //删除检验
    public interface DeleteBonusBudgetLaddertersDTO extends Default{

    }
    //修改检验
    public interface UpdateBonusBudgetLaddertersDTO extends Default{

    }
    

}
