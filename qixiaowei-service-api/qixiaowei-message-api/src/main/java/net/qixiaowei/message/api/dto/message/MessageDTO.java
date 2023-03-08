package net.qixiaowei.message.api.dto.message;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 消息表
* @author hzk
* @since 2022-12-09
*/
@Data
@Accessors(chain = true)
public class MessageDTO {

    //查询检验
    public interface QueryMessageDTO extends Default{

    }
    //新增检验
    public interface AddMessageDTO extends Default{

    }

    //删除检验
    public interface DeleteMessageDTO extends Default{

    }
    //修改检验
    public interface UpdateMessageDTO extends Default{

    }
    /**
    * ID
    */
    private  Long messageId;
    /**
     * 消息类型
     */
    private  Integer messageType;

    /**
    * 业务类型
    */
    private  Integer businessType;
    /**
    * 业务子类型
    */
    private  Integer businessSubtype;
    /**
    * 业务ID
    */
    private  Long businessId;
    /**
    * 发送用户ID
    */
    private  Long sendUserId;
    /**
    * 消息标题
    */
    private  String messageTitle;
    /**
    * 消息内容
    */
    private  String messageContent;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 创建人
    */
    private  Long createBy;
    /**
    * 创建时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;
    /**
    * 租户ID
    */
    private  Long tenantId;

}

