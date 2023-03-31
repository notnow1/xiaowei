package net.qixiaowei.operate.cloud.api.domain.performance;


import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;

import java.math.BigDecimal;

import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Date;

/**
 * 绩效考核表
 *
 * @author Graves
 * @since 2022-12-05
 */
@Data
@Accessors(chain = true)
public class PerformanceAppraisal extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long performanceAppraisalId;
    /**
     * 绩效等级ID
     */
    private Long performanceRankId;
    /**
     * 绩效等级名称
     */
    private String performanceRankName;
    /**
     * 考核年度
     */
    private Integer appraisalYear;
    /**
     * 考核名称
     */
    private String appraisalName;
    /**
     * 周期性考核标记:0否;1是
     */
    private Integer cycleFlag;
    /**
     * 周期类型:1月度;2季度;3半年度;4年度
     */
    private Integer cycleType;
    /**
     * 考核周期
     */
    private Integer cycleNumber;
    /**
     * 评议周期类型:1月度;2季度;3半年度;4年度
     */
    private Integer evaluationType;
    /**
     * 考核开始日期
     */
    private LocalDate appraisalStartDate;
    /**
     * 考核结束日期
     */
    private LocalDate appraisalEndDate;
    /**
     * 归档日期
     */
    private LocalDate filingDate;
    /**
     * 考核流程:1系统流程;2仅导入结果
     */
    private Integer appraisalFlow;
    /**
     * 考核对象:1组织;2员工
     */
    private Integer appraisalObject;
    /**
     * 自定义列标记:0否;1是
     */
    private Integer selfDefinedColumnsFlag;
    /**
     * 考核状态:1制定目标;2评议;3排名;4归档
     */
    private Integer appraisalStatus;

}

