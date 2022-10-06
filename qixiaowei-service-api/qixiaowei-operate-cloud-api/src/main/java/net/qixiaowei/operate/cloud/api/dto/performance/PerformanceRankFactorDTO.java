package net.qixiaowei.operate.cloud.api.dto.performance;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 绩效等级系数
 *
 * @author Graves
 * @since 2022-10-06
 */
@Data
@Accessors(chain = true)
public class PerformanceRankFactorDTO {

    //查询检验
    public interface QueryPerformanceRankFactorDTO extends Default {

    }

    //新增检验
    public interface AddPerformanceRankFactorDTO extends Default {

    }

    //新增检验
    public interface DeletePerformanceRankFactorDTO extends Default {

    }

    //修改检验
    public interface UpdatePerformanceRankFactorDTO extends Default {

    }

    /**
     * ID
     */
    private Long performanceRankFactorId;
    /**
     * 绩效等级ID
     */
    private Long performanceRankId;
    /**
     * 绩效等级名称
     */
    private String performanceRankName;
    /**
     * 奖金系数
     */
    private BigDecimal bonusFactor;
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

}

