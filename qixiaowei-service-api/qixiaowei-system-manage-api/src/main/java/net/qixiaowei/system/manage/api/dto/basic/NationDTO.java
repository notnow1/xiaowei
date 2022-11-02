package net.qixiaowei.system.manage.api.dto.basic;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 民族表
* @author TANGMICHI
* @since 2022-10-20
*/
@Data
@Accessors(chain = true)
public class NationDTO {

    //查询检验
    public interface QueryNationDTO extends Default{

    }
    //新增检验
    public interface AddNationDTO extends Default{

    }

    //删除检验
    public interface DeleteNationDTO extends Default{

    }
    //修改检验
    public interface UpdateNationDTO extends Default{

    }
    /**
    * ID
    */
    private  Long nationId;
    /**
    * 民族名称
    */
    private  String nationName;
    /**
    * 排序
    */
    private  Integer sort;
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

