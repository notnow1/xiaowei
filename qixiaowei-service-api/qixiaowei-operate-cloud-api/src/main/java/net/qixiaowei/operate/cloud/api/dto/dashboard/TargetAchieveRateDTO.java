package net.qixiaowei.operate.cloud.api.dto.dashboard;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;

/**
 * 关键经营指标目标达成率
 *
 * @author Graves
 * @since 2022-11-22
 */
@Data
@Accessors(chain = true)
public class TargetAchieveRateDTO {
    //查询检验
    public interface QueryTargetAchieveRateDTO extends Default {

    }

    //新增检验
    public interface AddTargetAchieveRateDTO extends Default {

    }

    //删除检验
    public interface DeleteTargetAchieveRateDTO extends Default {

    }

    //修改检验
    public interface UpdateTargetAchieveRateDTO extends Default {

    }

    /**
     * 目标年度
     */
    private Integer targetYear;
    /**
     * 指标名称
     */
    private String indicatorName;
    /**
     * 指标编码
     */
    private String indicatorCode;
    /**
     * 年度实际值
     */
    private BigDecimal actualTotal;
    /**
     * 年度目标值
     */
    private BigDecimal targetValue;
    /**
     * 比例
     */
    private BigDecimal rate;
}
