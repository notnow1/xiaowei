package net.qixiaowei.system.manage.api.dto.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.util.Date;
import java.util.List;

/**
 * 指标表
 *
 * @author Graves
 * @since 2022-09-28
 */
@Data
@Accessors(chain = true)
public class IndicatorDTO {

    //查询检验
    public interface QueryIndicatorDTO extends Default {

    }

    //新增检验
    public interface AddIndicatorDTO extends Default {

    }

    //新增检验
    public interface DeleteIndicatorDTO extends Default {

    }

    //修改检验
    public interface UpdateIndicatorDTO extends Default {

    }

    /**
     * ID
     */
    private Long indicatorId;
    /**
     * 父级指标ID
     */
    private Long parentIndicatorId;
    /**
     * 祖级列表ID，按层级用英文逗号隔开
     */
    private String ancestors;
    /**
     * 指标类型:1财务指标；2业务指标
     */
    private Integer indicatorType;
    /**
     * 指标编码
     */
    private String indicatorCode;
    /**
     * 指标名称
     */
    private String indicatorName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 层级
     */
    private Integer level;
    /**
     * 指标分类ID
     */
    private Long indicatorCategoryId;
    /**
     * 指标分类
     */
    private String indicatorCategoryName;
    /**
     * 指标值类型:1金额;2比率
     */
    private Integer indicatorValueType;
    /**
     * 必选标记:0非必选;1必选
     */
    private Integer choiceFlag;
    /**
     * 考核方向:0反向;1正向
     */
    private Integer examineDirection;
    /**
     * 指标定义
     */
    private String indicatorDefinition;
    /**
     * 指标公式
     */
    private String indicatorFormula;
    /**
     * 驱动因素标记:0否;1是
     */
    private Integer drivingFactorFlag;
    /**
     * 备注
     */
    private String remark;
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
     * 子级
     */
    private List<IndustryDTO> children;
    /**
     * 是否预置(1-预置，0-非预置)
     */
    private Integer isPreset;
    /**
     * 是否在targetSetting被选中了（1-选中，0-未选中）
     */
    private Integer isTarget;

}

