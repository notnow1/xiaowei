package net.qixiaowei.message.api.domain.message;


import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 消息表
 *
 * @author hzk
 * @since 2022-12-09
 */
@Data
@Accessors(chain = true)
public class Message extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long messageId;
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
     * 发送用户ID
     */
    private Long sendUserId;
    /**
     * 消息标题
     */
    private String messageTitle;
    /**
     * 消息内容
     */
    private String messageContent;

}

