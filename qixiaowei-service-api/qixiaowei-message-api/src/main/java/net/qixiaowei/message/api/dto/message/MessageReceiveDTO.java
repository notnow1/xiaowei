package net.qixiaowei.message.api.dto.message;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 消息接收表
 *
 * @author hzk
 * @since 2022-12-08
 */
@Data
@Accessors(chain = true)
public class MessageReceiveDTO {

    //查询检验
    public interface QueryMessageReceiveDTO extends Default {

    }

    //新增检验
    public interface AddMessageReceiveDTO extends Default {

    }

    //删除检验
    public interface DeleteMessageReceiveDTO extends Default {

    }

    //修改检验
    public interface UpdateMessageReceiveDTO extends Default {

    }

    /**
     * ID
     */
    @NotNull(message = "消息ID不能为空", groups = {MessageReceiveDTO.UpdateMessageReceiveDTO.class, MessageReceiveDTO.DeleteMessageReceiveDTO.class})
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
     * 消息标题
     */
    private String messageTitle;
    /**
     * 消息内容
     */
    private String messageContent;
    /**
     * 已读时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date readTime;
    /**
     * 状态:0未读;1已读
     */
    private Integer status;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


}

