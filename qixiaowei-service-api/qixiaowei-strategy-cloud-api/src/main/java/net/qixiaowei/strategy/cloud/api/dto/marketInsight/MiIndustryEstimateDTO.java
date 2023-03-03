package net.qixiaowei.strategy.cloud.api.dto.marketInsight;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Map;

/**
* 市场洞察行业预估表
* @author TANGMICHI
* @since 2023-03-03
*/
@Data
@Accessors(chain = true)
public class MiIndustryEstimateDTO {

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
    * 创建人
    */
    private  Long createBy;
    /**
    * 创建时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;
    /**
    * 租户ID
    */
    private  Long tenantId;

    /**
    * 请求参数
    */
    private Map<String, Object> params;
}

