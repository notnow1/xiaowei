package net.qixiaowei.message.api.dto.message;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 消息接收者
 *
 * @author hzk
 * @since 2022-12-09
 */
@Data
public class MessageReceiverDTO {

    /**
     * 用户信息，手机号，邮箱，用户ID
     */
    private String user;
    /**
     * 用户ID
     */
    @NotNull(message = "接收者userId不能为空")
    private Long userId;


}
