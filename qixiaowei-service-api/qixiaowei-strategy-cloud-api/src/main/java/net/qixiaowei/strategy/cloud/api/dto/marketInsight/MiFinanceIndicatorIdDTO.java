package net.qixiaowei.strategy.cloud.api.dto.marketInsight;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
* 对手财务具体经营值
* @author TANGMICHI
* @since 2023-03-12
*/
@Data
@Accessors(chain = true)
public class MiFinanceIndicatorIdDTO extends BaseDTO {

    //查询检验
    public interface QueryMiFinanceIndicatorIdDTO extends Default{

    }
    //新增检验
    public interface AddMiFinanceIndicatorIdDTO extends Default{

    }

    //删除检验
    public interface DeleteMiFinanceIndicatorIdDTO extends Default{

    }
    //修改检验
    public interface UpdateMiFinanceIndicatorIdDTO extends Default{

    }
    /**
    * ID
    */
    private  Long miOpponentFinanceId;
    /**
    * 经营年度
    */
    private  Integer operateYear;
    /**
    * 经营值
    */
    private  BigDecimal operateValue;

}

