package net.qixiaowei.message.api.domain.message;

import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 消息接收表
 *
 * @author hzk
 * @since 2022-12-08
 */
@Data
@Accessors(chain = true)
public class MessageReceive extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long messageReceiveId;
    /**
     * 消息ID
     */
    private Long messageId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 已读时间
     */
    private Date readTime;
    /**
     * 状态:0未读;1已读
     */
    private Integer status;

}

