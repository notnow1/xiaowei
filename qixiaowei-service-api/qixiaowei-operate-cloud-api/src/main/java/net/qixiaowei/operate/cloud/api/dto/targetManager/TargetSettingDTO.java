package net.qixiaowei.operate.cloud.api.dto.targetManager;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.operate.cloud.api.vo.TargetSettingIncomeVO;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 目标制定
 *
 * @author Graves
 * @since 2022-10-27
 */
@Data
@Accessors(chain = true)
public class TargetSettingDTO {

    //查询检验
    public interface QueryTargetSettingDTO extends Default {

    }

    //新增检验
    public interface AddTargetSettingDTO extends Default {

    }

    //删除检验
    public interface DeleteTargetSettingDTO extends Default {

    }

    //修改检验
    public interface UpdateTargetSettingDTO extends Default {

    }

    /**
     * ID
     */
    private Long targetSettingId;
    /**
     * 目标制定类型:0自定义;1销售订单;2销售收入;3销售回款
     */
    private Integer targetSettingType;
    /**
     * 指标ID
     */
    private Long indicatorId;
    /**
     * 指标名称
     */
    private String indicatorName;
    /**
     * 必选标记:0非必选;1必选
     */
    private Integer choiceFlag;
    /**
     * 指标编码
     */
    private String indicatorCode;
    /**
     * 父节点ID
     */
    private Long parentIndicatorId;
    /**
     * 指标ID集合
     */
    private List<Long> indicatorIds;
    /**
     * 标识（1-不可编辑和删除，2-可编辑不可删除，3-可编辑可删除,0-非预置数据）
     */
    private Integer isPreset;
    /**
     * 目标年度
     */
    @NotNull(message = "目标年度不能为空", groups = {TargetSettingDTO.class, TargetSettingDTO.UpdateTargetSettingDTO.class, TargetSettingDTO.QueryTargetSettingDTO.class})
    private Integer targetYear;
    /**
     * 百分比(%)
     */
    private BigDecimal percentage;
    /**
     * 挑战值
     */
    @NotNull(message = "挑战值不能为空", groups = {TargetSettingDTO.class, TargetSettingDTO.UpdateTargetSettingDTO.class})
    private BigDecimal challengeValue;
    /**
     * 目标值
     */
    @NotNull(message = "目标值不能为空", groups = {TargetSettingDTO.class, TargetSettingDTO.UpdateTargetSettingDTO.class})
    private BigDecimal targetValue;
    /**
     * 保底值
     */
    @NotNull(message = "保底值不能为空", groups = {TargetSettingDTO.class, TargetSettingDTO.UpdateTargetSettingDTO.class})
    private BigDecimal guaranteedValue;
    /**
     * 年度实际值
     */
    private BigDecimal actualTotal;

    /**
     * 去年实际值
     */
    private BigDecimal lastActualTotal;

    /**
     * 同比
     */
    private BigDecimal onBasis;


    /**
     * 目标完成率
     */
    private BigDecimal targetPercentageComplete;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 子级
     */
    private List<TargetSettingDTO> children;
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
     * 历史年度数
     */
    private Integer historyNum;
    /**
     * 开始年度
     */
    private Integer startYear;
    /**
     * 结束年度
     */
    private Integer endYear;
    /**
     * 订单目标制定
     */
    private TargetSettingDTO orderTargetSetting;
    /**
     * 销售订单List
     */
    private List<TargetSettingOrderDTO> targetSettingOrderDTOS;
    /**
     * 销售收入List
     */
    private List<TargetSettingIncomeVO> targetSettingIncomeVOS;
    /**
     * 目标制定销售回款表LIST
     */
    private List<Map<String, Object>> targetSettingRecoveryList;
    /**
     * 目标制定销售回款集合表
     */
    private List<TargetSettingRecoveriesDTO> targetSettingTypeDTOS;
    /**
     * 目标制定销售回款集合表
     */
    private List<TargetSettingRecoveriesDTO> targetSettingIndicatorDTOS;
}