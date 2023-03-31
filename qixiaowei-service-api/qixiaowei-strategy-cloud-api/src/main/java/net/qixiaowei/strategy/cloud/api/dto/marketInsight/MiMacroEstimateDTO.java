package net.qixiaowei.strategy.cloud.api.dto.marketInsight;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

import java.util.Map;

/**
* 市场洞察宏观预估表
* @author TANGMICHI
* @since 2023-02-28
*/
@Data
@Accessors(chain = true)
public class MiMacroEstimateDTO extends BaseDTO {

    //查询检验
    public interface QueryMiMacroEstimateDTO extends Default{

    }
    //新增检验
    public interface AddMiMacroEstimateDTO extends Default{

    }

    //删除检验
    public interface DeleteMiMacroEstimateDTO extends Default{

    }
    //修改检验
    public interface UpdateMiMacroEstimateDTO extends Default{

    }
    /**
    * ID
    */
    private  Long miMacroEstimateId;
    /**
    * 市场洞察宏观ID
    */
    private  Long marketInsightMacroId;
    /**
    * 市场洞察宏观详情ID
    */
    private  Long miMacroDetailId;
    /**
    * 规划年度
    */
    private  Integer planYear;
    /**
    * 预估机会点金额
    */
    private  BigDecimal estimateOpportunityAmount;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 租户ID
    */
    private  Long tenantId;

}

