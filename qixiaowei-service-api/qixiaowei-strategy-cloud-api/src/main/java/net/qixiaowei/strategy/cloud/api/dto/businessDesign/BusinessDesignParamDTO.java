package net.qixiaowei.strategy.cloud.api.dto.businessDesign;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Map;

/**
 * 业务设计参数表
 *
 * @author Graves
 * @since 2023-02-28
 */
@Data
@Accessors(chain = true)
public class BusinessDesignParamDTO {

    //查询检验
    public interface QueryBusinessDesignParamDTO extends Default {

    }

    //新增检验
    public interface AddBusinessDesignParamDTO extends Default {

    }

    //删除检验
    public interface DeleteBusinessDesignParamDTO extends Default {

    }

    //修改检验
    public interface UpdateBusinessDesignParamDTO extends Default {

    }

    /**
     * ID
     */
    private Long businessDesignParamId;
    /**
     * 业务设计ID
     */
    private Long businessDesignId;
    /**
     * 参数维度:1产品;2客户;3区域
     */
    private Integer paramDimension;
    /**
     * 参数关联ID
     */
    private Long paramRelationId;
    /**
     * 参数名称
     */
    private String paramName;
    /**
     * 历史平均毛利率
     */
    private BigDecimal historyAverageRate;
    /**
     * 历史权重
     */
    private BigDecimal historyWeight;
    /**
     * 预测毛利率
     */
    private BigDecimal forecastRate;
    /**
     * 预测权重
     */
    private BigDecimal forecastWeight;
    /**
     * 综合毛利率综合订单额
     */
    private BigDecimal synthesizeGrossMargin;
    /**
     * 历史订单额
     */
    private BigDecimal historyOrderAmount;
    /**
     * 历史订单权重
     */
    private BigDecimal historyOrderWeight;
    /**
     * 预测订单额
     */
    private BigDecimal forecastOrderAmount;
    /**
     * 预测订单权重
     */
    private BigDecimal forecastOrderWeight;
    /**
     * 综合订单额
     */
    private BigDecimal synthesizeOrderAmount;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 请求参数
     */
    private Map<String, Object> params;
}

