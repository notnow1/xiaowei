package net.qixiaowei.operate.cloud.api.dto.targetManager;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.vo.TargetSettingIncomeVO;

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
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 历史年度数
     */
    @NotBlank(message = "历史年度数不能为空", groups = {TargetSettingDTO.class, TargetSettingDTO.QueryTargetSettingDTO.class})
    private Integer historyNum;
    /**
     * 销售订单List
     */
    private List<TargetSettingOrderDTO> targetSettingOrderDTOS;
    /**
     * 销售收入List
     */
    private List<TargetSettingIncomeVO> targetSettingIncomeVOS;

}

