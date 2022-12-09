package net.qixiaowei.message.api.dto.message;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 消息表
 *
 * @author hzk
 * @since 2022-12-09
 */
@Data
@Accessors(chain = true)
public class MessageSendDTO {

    /**
     * 消息类型
     */
    @NotNull(message = "消息类型不能为空")
    private Integer messageType;

    /**
     * 业务类型
     */
    @NotNull(message = "业务类型不能为空")
    private Integer businessType;
    /**
     * 业务子类型
     */
    @NotNull(message = "业务子类型不能为空")
    private Integer businessSubtype;
    /**
     * 业务ID
     */
    @NotNull(message = "业务ID不能为空")
    private Long businessId;
    /**
     * 发送用户ID
     */
    @NotNull(message = "发送用户ID不能为空")
    private Long sendUserId;
    /**
     * 接受者角色(0普通角色)
     */
    private Integer receiveRole = 0;
    /**
     * 消息接收者
     */
    @NotEmpty(message = "消息接收者不能为空")
    private List<MessageReceiverDTO> messageReceivers;
    /**
     * 消息标题
     */
    private String messageTitle;
    /**
     * 消息内容
     */
    private String messageContent;
    /**
     * 消息参数 key:value {"key":"value"}
     */
    private String messageParam;
    /**
     * 是否需要处理内容(如果模版配置的消息，可以设置true)
     */
    private Boolean handleContent = false;
    /**
     * 扩展参数
     */
    private String extendParam;


}

