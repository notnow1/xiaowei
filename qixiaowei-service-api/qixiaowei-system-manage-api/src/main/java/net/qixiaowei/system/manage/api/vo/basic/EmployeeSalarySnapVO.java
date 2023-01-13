package net.qixiaowei.system.manage.api.vo.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data

public class EmployeeSalarySnapVO {
    private static final long serialVersionUID = 1L;
    /**
     * 调整时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date effectiveDate;
    /**
     * 调整类型(1调岗;2调级;3调薪),多个用英文逗号隔开
     */
    private String adjustmentType;
    /**
     * 调整类型(1调岗;2调级;3调薪),多个用英文逗号隔开
     */
    private List<Integer> adjustmentTypeList;
    /**
     * 调整类型(1调岗;2调级;3调薪),多个用英文逗号隔开
     */
    private List<String> adjustmentTypeNameList;
    /**
     * 人员ID
     */
    private Long employeeId;
    /**
     * 调整岗位ID
     */
    private Long adjustPostId;
    /**
     * 调整岗位名称
     */
    private String adjustPostName;
    /**
     * 调整职级体系ID
     */
    private Long adjustOfficialRankSystemId;
    /**
     * 调整职级体系名称
     */
    private String adjustOfficialRankSystemName;
    /**
     * 调整职级
     */
    private Integer adjustOfficialRank;
    /**
     * 调整职级名称
     */
    private String adjustOfficialRankName;
    /**
     * 调整薪酬
     */
    private BigDecimal adjustEmolument;
    /**
     * 调整说明
     */
    private String adjustExplain;

    //==============================员工调整前的数据==================================//

    /**
     * 原岗位ID
     */
    private Long postId;
    /**
     * 原岗位名称
     */
    private String postName;
    /**
     * 原职级体系ID
     */
    private Long officialRankSystemId;
    /**
     * 原职级体系名称
     */
    private String officialRankSystemName;
    /**
     * 原职级
     */
    private Integer officialRank;
    /**
     * 原职级名称
     */
    private String officialRankName;
    /**
     * 原工资
     */
    private BigDecimal basicWage;
}
