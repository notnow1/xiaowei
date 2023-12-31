package net.qixiaowei.operate.cloud.api.domain.salary;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

/**
 * 工资项
 *
 * @author Graves
 * @since 2022-10-05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class SalaryItem extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long salaryItemId;
    /**
     * 一级项目:1总工资包;2总奖金包;3总扣减项
     */
    private Integer firstLevelItem;
    /**
     * 二级项目:1工资;2津贴;3福利;4奖金;5代扣代缴;6其他扣款
     */
    private Integer secondLevelItem;
    /**
     * 三级项目
     */
    private String thirdLevelItem;
    /**
     * 状态:0失效;1生效
     */
    private Integer status;
    /**
     * 作用范围：1部门;2公司
     */
    private Integer scope;
    /**
     * 排序
     */
    private Integer sort;

}

