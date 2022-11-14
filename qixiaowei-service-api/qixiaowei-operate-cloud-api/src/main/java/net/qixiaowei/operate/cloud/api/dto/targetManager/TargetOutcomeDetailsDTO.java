package net.qixiaowei.operate.cloud.api.dto.targetManager;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 目标结果详情表
 *
 * @author TANGMICHI
 * @since 2022-11-07
 */
@Data
@Accessors(chain = true)
public class TargetOutcomeDetailsDTO {

    //查询检验
    public interface QueryTargetOutcomeDetailsDTO extends Default {

    }

    //新增检验
    public interface AddTargetOutcomeDetailsDTO extends Default {

    }

    //删除检验
    public interface DeleteTargetOutcomeDetailsDTO extends Default {

    }

    //修改检验
    public interface UpdateTargetOutcomeDetailsDTO extends Default {

    }

    /**
     * ID
     */
    private Long targetOutcomeDetailsId;
    /**
     * 目标结果ID
     */
    private Long targetOutcomeId;
    /**
     * 指标ID
     */
    private Long indicatorId;
    /**
     * 指标名称
     */
    private String indicatorName;
    /**
     * 累计实际值
     */
    private BigDecimal actualTotal;
    /**
     * 目标值
     */
    private BigDecimal targetValue;
    /**
     * 目标完成率（%）
     */
    private BigDecimal targetCompletionRate;
    /**
     * 一月实际值
     */
    private BigDecimal actualJanuary;
    /**
     * 二月实际值
     */
    private BigDecimal actualFebruary;
    /**
     * 三月实际值
     */
    private BigDecimal actualMarch;
    /**
     * 四月实际值
     */
    private BigDecimal actualApril;
    /**
     * 五月实际值
     */
    private BigDecimal actualMay;
    /**
     * 六月实际值
     */
    private BigDecimal actualJune;
    /**
     * 七月实际值
     */
    private BigDecimal actualJuly;
    /**
     * 八月实际值
     */
    private BigDecimal actualAugust;
    /**
     * 九月实际值
     */
    private BigDecimal actualSeptember;
    /**
     * 十月实际值
     */
    private BigDecimal actualOctober;
    /**
     * 十一月实际值
     */
    private BigDecimal actualNovember;
    /**
     * 十二月实际值
     */
    private BigDecimal actualDecember;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 目标年度
     */
    private Integer targetYear;
    /**
     * 针对性的月份值
     */
    private List<BigDecimal> monthValue;
}