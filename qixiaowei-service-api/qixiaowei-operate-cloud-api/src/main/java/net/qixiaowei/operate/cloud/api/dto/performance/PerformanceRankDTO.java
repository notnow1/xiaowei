package net.qixiaowei.operate.cloud.api.dto.performance;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

/**
 * 绩效等级表
 *
 * @author Graves
 * @since 2022-10-06
 */
@Data
@Accessors(chain = true)
public class PerformanceRankDTO extends BaseDTO {

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
     * 绩效等级系数
     */
    private List<PerformanceRankFactorDTO> performanceRankFactorDTOS;
    /**
     * 是否可以编辑(0-不可以编辑，1-可以编辑)
     */
    private int isEdit;

}

