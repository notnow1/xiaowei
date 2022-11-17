package net.qixiaowei.operate.cloud.api.domain.targetManager;


import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 目标分解详情表
 *
 * @author TANGMICHI
 * @since 2022-10-28
 */
@Data
@Accessors(chain = true)
public class TargetDecomposeDetails extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long targetDecomposeDetailsId;
    /**
     * 目标分解ID
     */
    private Long targetDecomposeId;
    /**
     * 员工ID
     */
    private Long employeeId;
    /**
     * 员工ID集合
     */
    private List<Long> employeeIds;
    /**
     * 区域ID
     */
    private Long areaId;
    /**
     * 区域ID集合
     */
    private List<Long> areaIds;


    /**
     * 部门ID
     */
    private Long departmentId;
    /**
     * 部门ID集合
     */
    private List<Long> departmentIds;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 产品ID集合
     */
    private List<Long> productIds;
    /**
     * 省份ID
     */
    private Long regionId;
    /**
     * 省份ID集合
     */
    private List<Long> regionIds;
    /**
     * 行业ID
     */
    private Long industryId;
    /**
     * 行业ID集合
     */
    private List<Long> industryIds;
    /**
     * 负责人ID
     */
    private Long principalEmployeeId;
    /**
     * 汇总目标值
     */
    private BigDecimal amountTarget;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;

}

