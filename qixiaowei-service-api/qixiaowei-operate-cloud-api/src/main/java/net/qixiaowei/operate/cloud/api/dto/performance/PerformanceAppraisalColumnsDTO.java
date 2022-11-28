package net.qixiaowei.operate.cloud.api.dto.performance;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 绩效考核自定义列表
 *
 * @author Graves
 * @since 2022-11-28
 */
@Data
@Accessors(chain = true)
public class PerformanceAppraisalColumnsDTO {

    //查询检验
    public interface QueryPerformanceAppraisalColumnsDTO extends Default {

    }

    //新增检验
    public interface AddPerformanceAppraisalColumnsDTO extends Default {

    }

    //删除检验
    public interface DeletePerformanceAppraisalColumnsDTO extends Default {

    }

    //修改检验
    public interface UpdatePerformanceAppraisalColumnsDTO extends Default {

    }

    /**
     * ID
     */
    private Long performAppraisalColumnsId;
    /**
     * 绩效考核ID
     */
    private Long performanceAppraisalId;
    /**
     * 列名
     */
    private String columnName;
    /**
     * 列值(所有行集合的JSON格式数据)
     */
    private String columnValue;
    /**
     * 自定义列标记:0否;1是
     */
    private Integer selfDefinedColumnsFlag;
    /**
     * 是否可以下载附件
     */
    private Integer isAttachment;
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

}

