package net.qixiaowei.operate.cloud.api.domain.bonus;


import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;

import java.math.BigDecimal;

import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 奖金发放申请表
 *
 * @author TANGMICHI
 * @since 2022-12-08
 */
@Data
@Accessors(chain = true)
public class BonusPayApplication extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long bonusPayApplicationId;
    /**
     * 奖项类别,工资条ID
     */
    private Long salaryItemId;
    /**
     * 三级项目(奖项类别名称)
     */
    private String thirdLevelItem;
    /**
     * 预算部门ID
     */
    private  Long departmentId;
    /**
     * 预算部门名称
     */
    private  String departmentName;
    /**
     * 奖项编码
     */
    private String awardCode;
    /**
     * 奖项名称
     */
    private String awardName;
    /**
     * 获奖时间-年
     */
    private Integer awardYear;
    /**
     * 获奖时间-月
     */
    private Integer awardMonth;

    /**
     * 获奖时间-年月
     */
    private String awardYearMonth;
    /**
     * 申请部门ID
     */
    private Long applyDepartmentId;
    /**
     * 奖项总金额
     */
    private BigDecimal awardTotalAmount;
    /**
     * 奖金发放对象:1部门;2员工;3部门+员工
     */
    private Integer bonusPayObject;
    /**
     * 奖项事迹描述
     */
    private String awardDescription;
    /**
     * 预算部门名称
     */
    private String budgetDepartmentNames;
    /**
     * 预算部门等于
     */
    private String budgetDepartmentIdsEqual;
    /**
     * 预算部门不等于
     */
    private String budgetDepartmentIdsNotEqual;

    /**
     * 预算部门ID集合
     */
    private List<Long> budgetDepartmentIds;
}

