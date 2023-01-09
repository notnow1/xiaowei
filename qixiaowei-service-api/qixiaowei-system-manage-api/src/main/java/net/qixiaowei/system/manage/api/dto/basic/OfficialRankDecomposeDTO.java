package net.qixiaowei.system.manage.api.dto.basic;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 职级分解表
 *
 * @author Graves
 * @since 2022-10-07
 */
@Data
@Accessors(chain = true)
public class OfficialRankDecomposeDTO {

    //查询检验
    public interface QueryOfficialRankDecomposeDTO extends Default {

    }

    //新增检验
    public interface AddOfficialRankDecomposeDTO extends Default {

    }

    //新增检验
    public interface DeleteOfficialRankDecomposeDTO extends Default {

    }

    //修改检验
    public interface UpdateOfficialRankDecomposeDTO extends Default {

    }

    /**
     * ID
     */
    private Long officialRankDecomposeId;
    /**
     * 职级体系ID
     */
    private Long officialRankSystemId;
    /**
     * 职级体系名称
     */
    private Long officialRankSystemName;
    /**
     * 职级分解维度:1部门;2区域;3省份;4产品
     */
    private Integer rankDecomposeDimension;
    /**
     * 具体分解维度的ID
     */
    private Long decomposeDimension;
    /**
     * 具体分解维度的名称
     */
    private String decomposeDimensionName;
    /**
     * 工资系数
     */
    private BigDecimal salaryFactor;
    /**
     * 工资上限
     */
    private BigDecimal salaryCap;
    /**
     * 工资下限
     */
    private BigDecimal salaryFloor;
    /**
     * 工资中位数
     */
    private BigDecimal salaryMedian;
    /**
     * 工资宽幅
     */
    private BigDecimal salaryWide;
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

}

