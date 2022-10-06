package net.qixiaowei.operate.cloud.api.dto.performance;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 绩效等级表
 *
 * @author Graves
 * @since 2022-10-06
 */
@Data
@Accessors(chain = true)
public class PerformanceRankDTO {

    //查询检验
    public interface QueryPerformanceRankDTO extends Default {

    }

    //新增检验
    public interface AddPerformanceRankDTO extends Default {

    }

    //新增检验
    public interface DeletePerformanceRankDTO extends Default {

    }

    //修改检验
    public interface UpdatePerformanceRankDTO extends Default {

    }

    /**
     * ID
     */
    private Long performanceRankId;
    /**
     * 绩效等级类别:1组织;2个人
     */
    private Integer performanceRankCategory;
    /**
     * 绩效等级名称
     */
    private String performanceRankName;
    /**
     * 绩效等级描述
     */
    private String performanceRankDescription;
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
     * 绩效等级系数
     */
    private List<PerformanceRankFactorDTO> performanceRankFactorDTOS;

}

