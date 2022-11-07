package net.qixiaowei.operate.cloud.api.dto.targetManager;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.operate.cloud.api.vo.TargetSettingIncomeVO;

import javax.validation.constraints.NotBlank;
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
    @NotBlank(message = "目标制定ID不能为空", groups = {TargetSettingDTO.class, TargetSettingDTO.UpdateTargetSettingDTO.class})
    private Long targetSettingId;
    /**
     * 目标制定类型:0自定义;1销售订单;2销售收入;3销售回款
     */
    @NotBlank(message = "目标制定类型不能为空", groups = {TargetSettingDTO.class, TargetSettingDTO.UpdateTargetSettingDTO.class})
    private Integer targetSettingType;
    /**
     * 指标ID
     */
    private Long indicatorId;
    /**
     * 指标ID
     */
    private String indicatorName;
    /**
     * 必选标记:0非必选;1必选
     */
    private Integer choiceFlag;
    /**
     * 指标ID集合
     */
    private List<Long> indicatorIds;
    /**
     * 目标年度
     */
    @NotBlank(message = "目标制定类型不能为空", groups = {TargetSettingDTO.class, TargetSettingDTO.UpdateTargetSettingDTO.class})
    private Integer targetYear;
    /**
     * 百分比(%)
     */
    @NotBlank(message = "百分比(%)不能为空", groups = {TargetSettingDTO.class, TargetSettingDTO.UpdateTargetSettingDTO.class})
    private BigDecimal percentage;
    /**
     * 挑战值
     */
    @NotBlank(message = "挑战值不能为空", groups = {TargetSettingDTO.class, TargetSettingDTO.UpdateTargetSettingDTO.class})
    private BigDecimal challengeValue;
    /**
     * 目标值
     */
    @NotBlank(message = "目标值不能为空", groups = {TargetSettingDTO.class, TargetSettingDTO.UpdateTargetSettingDTO.class})
    private BigDecimal targetValue;
    /**
     * 保底值
     */
    @NotBlank(message = "保底值不能为空", groups = {TargetSettingDTO.class, TargetSettingDTO.UpdateTargetSettingDTO.class})
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
     * 历史年度数
     */
    @NotBlank(message = "历史年度数不能为空", groups = {TargetSettingDTO.class, TargetSettingDTO.QueryTargetSettingDTO.class})
    private Integer historyNum;
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
    /**
     * 父节点ID
     */
    private Long parentIndicatorId;
}

