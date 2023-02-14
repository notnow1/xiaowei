package net.qixiaowei.operate.cloud.api.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.math.BigDecimal;
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
    private Integer startYear;
    /**
     * 目标年度
     */
    private Integer endYear;
    /**
     * 目标分解ID
     */
    private Long targetDecomposeId;
    /**
     * 目标分解详情ID
     */
    private Long targetDecomposeDetailsId;
    /**
     * 目标分解周期ID
     */
    private Long decomposeDetailCyclesId;
    /**
     * 周期数(顺序递增)
     */
    private Integer cycleNumber;
    /**
     * 开始周期数(顺序递增)
     */
    private Integer cycleNumberStart;
    /**
     * 结束周期数(顺序递增)
     */
    private Integer cycleNumberEnd;
    /**
     * 周期数(顺序递增)
     */
    private String cycleNumberName;
    /**
     * 周期目标值
     */
    private BigDecimal cycleTarget;
    /**
     * 周期预测值
     */
    private BigDecimal cycleForecast;
    /**
     * 周期实际值
     */
    private BigDecimal cycleActual;
    /**
     * 周期目标值总和
     */
    private BigDecimal cycleTargetSum;
    /**
     * 周期预测值总和
     */
    private BigDecimal cycleForecastSum;
    /**
     * 周期实际值总和
     */
    private BigDecimal cycleActualSum;
    /**
     * 目标完成率
     */
    private BigDecimal completionRate;
    /**
     * 预测偏差率
     */
    private BigDecimal deviationRate;

}
