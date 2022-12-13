package net.qixiaowei.message.api.dto.backlog;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;


/**
 * 待办事项表
 *
 * @author hzk
 * @since 2022-12-11
 */
@Data
@Accessors(chain = true)
public class BacklogSendDTO {

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
     * 接收用户ID
     */
    @NotNull(message = "接收用户ID不能为空")
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


}

