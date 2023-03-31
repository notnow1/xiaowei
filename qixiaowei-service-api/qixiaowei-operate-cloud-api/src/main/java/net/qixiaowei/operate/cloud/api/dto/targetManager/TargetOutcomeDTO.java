package net.qixiaowei.operate.cloud.api.dto.targetManager;

import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

import javax.validation.groups.Default;
import java.util.List;
import java.util.Set;

/**
 * 目标结果表
 *
 * @author TANGMICHI
 * @since 2022-11-07
 */
@Data
@Accessors(chain = true)
public class TargetOutcomeDTO extends BaseDTO {

    //查询检验
    public interface QueryTargetOutcomeDTO extends Default {

    }

    //新增检验
    public interface AddTargetOutcomeDTO extends Default {

    }

    //删除检验
    public interface DeleteTargetOutcomeDTO extends Default {

    }

    //修改检验
    public interface UpdateTargetOutcomeDTO extends Default {

    }

    /**
     * ID
     */
    private Long targetOutcomeId;
    /**
     * 目标年度
     */
    private Integer targetYear;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 目标结果列表
     */
    private List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList;
    /**
     * 员工ID集合
     */
    private Set<Long> createBys;

}

