package net.qixiaowei.system.manage.api.dto.basic;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 字典类型表
* @author TANGMICHI
* @since 2022-10-15
*/
@Data
@Accessors(chain = true)
public class DictionaryTypeDTO {

    //查询检验
    public interface QueryDictionaryTypeDTO extends Default{

    }
    //新增检验
    public interface AddDictionaryTypeDTO extends Default{

    }

    //删除检验
    public interface DeleteDictionaryTypeDTO extends Default{

    }
    //修改检验
    public interface UpdateDictionaryTypeDTO extends Default{

    }
    /**
    * ID
    */
    private  Long dictionaryTypeId;
    /**
    * 字典类型
    */
    private  String dictionaryType;
    /**
    * 字典名称
    */
    private  String dictionaryName;
    /**
    * 菜单0层级名称
    */
    private  String menuZerothName;
    /**
    * 菜单1层级名称
    */
    private  String menuFirstName;
    /**
    * 菜单2层级名称
    */
    private  String menuSecondName;
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

