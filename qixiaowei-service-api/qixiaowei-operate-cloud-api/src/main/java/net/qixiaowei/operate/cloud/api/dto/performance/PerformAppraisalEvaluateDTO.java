package net.qixiaowei.operate.cloud.api.dto.performance;

import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

import javax.validation.groups.Default;
import java.math.BigDecimal;

/**
 * 绩效考核评议表
 *
 * @author Graves
 * @since 2023-03-23
 */
@Data
@Accessors(chain = true)
public class PerformAppraisalEvaluateDTO extends BaseDTO {

    //查询检验
    public interface QueryPerformAppraisalEvaluateDTO extends Default {

    }

    //新增检验
    public interface AddPerformAppraisalEvaluateDTO extends Default {

    }

    //删除检验
    public interface DeletePerformAppraisalEvaluateDTO extends Default {

    }

    //修改检验
    public interface UpdatePerformAppraisalEvaluateDTO extends Default {

    }

    /**
     * ID
     */
    private Long performAppraisalEvaluateId;
    /**
     * 绩效考核对象ID
     */
    private Long performAppraisalObjectsId;
    /**
     * 绩效考核项目ID
     */
    private Long performAppraisalItemsId;
    /**
     * 评议周期
     */
    private Integer evaluateNumber;
    /**
     * 实际值
     */
    private BigDecimal actualValue;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer delete_flag;
    /**
     * 租户ID
     */
    private Long tenantId;

}

