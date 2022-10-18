package net.qixiaowei.system.manage.api.dto.basic;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 配置表
* @author Graves
* @since 2022-10-18
*/
@Data
@Accessors(chain = true)
public class ConfigDTO {

    //查询检验
    public interface QueryConfigDTO extends Default{

    }
    //新增检验
    public interface AddConfigDTO extends Default{

    }

    //删除检验
    public interface DeleteConfigDTO extends Default{

    }
    //修改检验
    public interface UpdateConfigDTO extends Default{

    }
    /**
    * ID
    */
    private  Long configId;
    /**
    * 父级配置ID
    */
    private  Long parentConfigId;
    /**
    * 节点路径code,用英文.做连接
    */
    private  String pathCode;
    /**
    * 配置编码
    */
    private  String configCode;
    /**
    * 配置值
    */
    private  String configValue;
    /**
    * 备注
    */
    private  String remark;
    /**
    * 状态:0失效;1生效
    */
    private  Integer status;
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
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;

}

