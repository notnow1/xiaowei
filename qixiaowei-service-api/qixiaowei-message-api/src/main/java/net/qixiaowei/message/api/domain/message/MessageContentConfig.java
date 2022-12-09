package net.qixiaowei.message.api.domain.message;

import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 消息内容配置表
 *
 * @author hzk
 * @since 2022-12-08
 */
@Data
@Accessors(chain = true)
public class MessageContentConfig extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long messageContentConfigId;
    /**
     * 消息类型
     */
    private Integer messageType;
    /**
     * 业务类型
     */
    private Integer businessType;
    /**
     * 业务子类型
     */
    private Integer businessSubtype;
    /**
     * 接受者角色(0普通角色)
     */
    private Integer receiveRole;
    /**
     * 消息模版
     */
    private String messageTemplate;
    /**
     * 消息额外用户，用英文逗号分隔
     */
    private String receiveUser;

}

