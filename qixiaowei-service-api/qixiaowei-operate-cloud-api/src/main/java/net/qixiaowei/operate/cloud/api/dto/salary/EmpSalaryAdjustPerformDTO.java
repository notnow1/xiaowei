package net.qixiaowei.operate.cloud.api.dto.salary;

import java.time.LocalDate;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.math.BigDecimal;
import javax.naming.Name;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 个人调薪绩效记录表
 *
 * @author Graves
 * @since 2022-12-14
 */
@Data
@Accessors(chain = true)
public class EmpSalaryAdjustPerformDTO {

    //查询检验
    public interface QueryEmpSalaryAdjustPerformDTO extends Default {

    }

    //新增检验
    public interface AddEmpSalaryAdjustPerformDTO extends Default {

    }

    //删除检验
    public interface DeleteEmpSalaryAdjustPerformDTO extends Default {

    }

    //修改检验
    public interface UpdateEmpSalaryAdjustPerformDTO extends Default {

    }

    /**
     * ID
     */
    private Long empSalaryAdjustPerformId;
    /**
     * 个人调薪计划ID
     */
    private Long empSalaryAdjustPlanId;
    /**
     * 绩效考核ID
     */
    private Long performanceAppraisalId;
    /**
     * 绩效考核对象ID
     */
    private Long performAppraisalObjectsId;
    /**
     * 周期类型:1月度;2季度;3半年度;4年度
     */
    private Integer cycleType;
    private String cycleTypeName;
    /**
     * 考核周期
     */
    private Integer cycleNumber;
    private String cycleNumberName;
    /**
     * 归档日期
     */
    private LocalDate filingDate;
    /**
     * 考核结果
     */
    private String appraisalResult;
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
     * 租户ID
     */
    private Long tenantId;

}

