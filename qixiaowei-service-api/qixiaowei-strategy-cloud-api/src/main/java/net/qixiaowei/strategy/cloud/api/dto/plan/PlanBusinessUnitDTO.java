package net.qixiaowei.strategy.cloud.api.dto.plan;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

import javax.validation.groups.Default;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 规划业务单元
 *
 * @author wangchen
 * @since 2023-02-17
 */
@Data
@Accessors(chain = true)
public class PlanBusinessUnitDTO extends BaseDTO {

    //查询检验
    public interface QueryPlanBusinessUnitDTO extends Default {

    }

    //新增检验
    public interface AddPlanBusinessUnitDTO extends Default {

    }

    //删除检验
    public interface DeletePlanBusinessUnitDTO extends Default {

    }

    //修改检验
    public interface UpdatePlanBusinessUnitDTO extends Default {

    }

    /**
     * ID
     */
    private Long planBusinessUnitId;
    /**
     * 规划业务单元编码
     */
    private String businessUnitCode;
    /**
     * 规划业务单元名称
     */
    private String businessUnitName;
    /**
     * 规划业务单元维度(region,department,product,industry,company)
     */
    private String businessUnitDecompose;
    /**
     * 规划业务单元维度(region,department,product,industry,company)
     */
    private String businessUnitDecomposeName;
    /**
     * 状态:0失效;1生效
     */
    private Integer status;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 规划业务单元维度列表
     */
    List<Map<String, Object>> businessUnitDecomposes;

}

