package net.qixiaowei.strategy.cloud.api.dto.businessDesign;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 业务设计轴配置表
 *
 * @author Graves
 * @since 2023-02-28
 */
@Data
@Accessors(chain = true)
public class BusinessDesignAxisConfigDTO {

    //查询检验
    public interface QueryBusinessDesignAxisConfigDTO extends Default {

    }

    //新增检验
    public interface AddBusinessDesignAxisConfigDTO extends Default {

    }

    //删除检验
    public interface DeleteBusinessDesignAxisConfigDTO extends Default {

    }

    //修改检验
    public interface UpdateBusinessDesignAxisConfigDTO extends Default {

    }

    /**
     * ID
     */
    private Long businessDesignAxisConfigId;
    /**
     * 业务设计ID
     */
    private Long businessDesignId;
    /**
     * 参数维度:1产品;2客户;3区域
     */
    private Integer paramDimension;
    /**
     * 坐标轴:1 x轴;2 y轴
     */
    private Integer coordinateAxis;
    /**
     * 高区值
     */
    private BigDecimal upperValue;
    /**
     * 低区值
     */
    private BigDecimal lowerValue;

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

