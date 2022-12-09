package net.qixiaowei.message.api.dto.message;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 消息内容配置表
* @author hzk
* @since 2022-12-08
*/
@Data
@Accessors(chain = true)
public class MessageContentConfigDTO {

    //查询检验
    public interface QueryMessageContentConfigDTO extends Default{

    }
    //新增检验
    public interface AddMessageContentConfigDTO extends Default{

    }

    //删除检验
    public interface DeleteMessageContentConfigDTO extends Default{

    }
    //修改检验
    public interface UpdateMessageContentConfigDTO extends Default{

    }
    /**
    * ID
    */
    private  Long messageContentConfigId;
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
    * 接受者角色(0普通角色)
    */
    private  Integer receiveRole;
    /**
    * 消息模版
    */
    private  String messageTemplate;
    /**
    * 消息额外用户，用英文逗号分隔
    */
    private  String receiveUser;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;

}

