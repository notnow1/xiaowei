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
* 市场洞察行业预估表
* @author TANGMICHI
* @since 2023-03-03
*/
@Data
@Accessors(chain = true)
public class MiIndustryEstimateDTO extends BaseDTO {

    //查询检验
    public interface QueryMiIndustryEstimateDTO extends Default{

    }
    //新增检验
    public interface AddMiIndustryEstimateDTO extends Default{

    }

    //删除检验
    public interface DeleteMiIndustryEstimateDTO extends Default{

    }
    //修改检验
    public interface UpdateMiIndustryEstimateDTO extends Default{

    }
    /**
    * ID
    */
    private  Long miMacroEstimateId;
    /**
    * 市场洞察行业ID
    */
    private  Long marketInsightIndustryId;
    /**
    * 市场洞察行业详情ID
    */
    private  Long miIndustryDetailId;
    /**
    * 规划年度
    */
    private  Integer planYear;
    /**
    * 整体空间金额
    */
    private  BigDecimal overallSpaceAmount;
    /**
    * 可参与空间金额
    */
    private  BigDecimal participateSpaceAmount;
    /**
    * 目标市场空间金额
    */
    private  BigDecimal targetMarketSpaceAmount;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 租户ID
    */
    private  Long tenantId;

}

