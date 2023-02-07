package net.qixiaowei.operate.cloud.api.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Date;
import java.util.Map;

/**
 * 关键经营指标目标达成率
 *
 * @author Graves
 * @since 2022-11-22
 */
@Data
@Accessors(chain = true)
public class TargetAchieveAnalysisDTO {

    //查询检验
    public interface QueryTargetAchieveAnalysisDTO extends Default {

    }

    //新增检验
    public interface AddTargetAchieveAnalysisDTO extends Default {

    }

    //删除检验
    public interface DeleteTargetAchieveAnalysisDTO extends Default {

    }

    //修改检验
    public interface UpdateTargetAchieveAnalysisDTO extends Default {

    }

    /**
     * 指标ID
     */
    @NotNull(message = "指标ID不能为空", groups = {TargetAchieveAnalysisDTO.QueryTargetAchieveAnalysisDTO.class})
    private Long indicatorId;
    /**
     * 目标分解维度ID
     */
    @NotNull(message = "目标分解维度ID不能为空", groups = {TargetAchieveAnalysisDTO.QueryTargetAchieveAnalysisDTO.class})
    private Long targetDecomposeDimensionId;
    /**
     * 分解维度
     */
    @NotBlank(message = "分解维度名称不能为空", groups = {TargetAchieveAnalysisDTO.QueryTargetAchieveAnalysisDTO.class})
    private String decompositionDimension;
    /**
     * 时间维度:1年度;2半年度;3季度;4月度;5周
     */
    @NotNull(message = "时间维度不能为空", groups = {TargetAchieveAnalysisDTO.QueryTargetAchieveAnalysisDTO.class})
    private Integer timeDimension;
    /**
     * 目标年度
     */
    @NotNull(message = "目标年度不能为空", groups = {TargetAchieveAnalysisDTO.QueryTargetAchieveAnalysisDTO.class})
    private Integer targetYear;
    /**
     * 目标年度
     */
    private String timeX;
    /**
     * 请求参数
     */
    private Map<String, Object> params;
}
