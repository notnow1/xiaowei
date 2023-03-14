package net.qixiaowei.strategy.cloud.api.dto.businessDesign;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisDTO;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 业务设计表
 *
 * @author Graves
 * @since 2023-02-28
 */
@Data
@Accessors(chain = true)
public class BusinessDesignDTO {

    //查询检验
    public interface QueryBusinessDesignDTO extends Default {

    }

    //新增检验
    public interface AddBusinessDesignDTO extends Default {

    }

    //删除检验
    public interface DeleteBusinessDesignDTO extends Default {

    }

    //修改检验
    public interface UpdateBusinessDesignDTO extends Default {

    }

    /**
     * ID
     */
    @NotNull(message = "id不能为空", groups = {BusinessDesignDTO.DeleteBusinessDesignDTO.class, GapAnalysisDTO.UpdateGapAnalysisDTO.class})
    private Long businessDesignId;
    /**
     * 规划年度
     */
    @NotNull(message = "规划年度不能为空", groups = {BusinessDesignDTO.AddBusinessDesignDTO.class})
    private Integer planYear;
    /**
     * 规划业务单元ID
     */
    @NotNull(message = "规划年度不能为空", groups = {BusinessDesignDTO.AddBusinessDesignDTO.class})
    private Long planBusinessUnitId;
    /**
     * 规划业务单元名称
     */
    private String businessUnitName;
    /**
     * 规划业务单元维度(region,department,product,industry)
     */
    private String businessUnitDecompose;
    /**
     * 规划业务单元维度列表
     */
    List<Map<String, Object>> businessUnitDecomposes;
    /**
     * 规划业务单元维度(region,department,product,industry)
     */
    private String businessUnitDecomposeName;
    /**
     * 区域ID
     */
    private Long areaId;
    /**
     * 部门ID
     */
    private Long departmentId;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 行业ID
     */
    private Long industryId;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 行业名称
     */
    private String industryName;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 创建人
     */
    private String createByName;
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
    /**
     * 业务设计参数表
     */
    private List<BusinessDesignParamDTO> businessDesignParamDTOS;
    /**
     * 业务设计轴配置表
     */
    private List<Map<String, Object>> businessDesignAxisConfigMap;
}

