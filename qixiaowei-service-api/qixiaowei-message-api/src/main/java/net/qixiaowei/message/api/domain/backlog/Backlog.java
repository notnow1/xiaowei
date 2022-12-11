package net.qixiaowei.message.api.domain.backlog;


import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;


import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 待办事项表
 *
 * @author hzk
 * @since 2022-12-11
 */
@Data
@Accessors(chain = true)
public class Backlog extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long backlogId;
    /**
     * 业务类型
     */
    private Integer businessType;
    /**
     * 业务子类型
     */
    private Integer businessSubtype;
    /**
     * 业务ID
     */
    private Long businessId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 待办名称
     */
    private String backlogName;
    /**
     * 待办事项发起者
     */
    private Long backlogInitiator;
    /**
     * 待办事项发起者名称
     */
    private String backlogInitiatorName;
    /**
     * 待办事项发起时间
     */
    private Date backlogInitiationTime;
    /**
     * 待办事项处理时间
     */
    private Date backlogProcessTime;
    /**
     * 状态:0待办;1已办
     */
    private Integer status;

}

