package net.qixiaowei.system.manage.api.dto.basic;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 字典类型表
* @author TANGMICHI
* @since 2022-10-07
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

    //新增检验
    public interface DeleteDictionaryTypeDTO extends Default{

    }
    //修改检验
    public interface UpdateDictionaryTypeDTO extends Default{

    }
    /**
    * ID
    */
    @NotNull(message = "字典ID不能为空",groups = {DictionaryTypeDTO.DeleteDictionaryTypeDTO.class,DictionaryTypeDTO.UpdateDictionaryTypeDTO.class})
    private  Long dictionaryTypeId;
    /**
    * 字典类型
    */
    @NotBlank(message = "字典类型不能为空",groups = {DictionaryTypeDTO.AddDictionaryTypeDTO.class,DictionaryTypeDTO.UpdateDictionaryTypeDTO.class})
    private  String dictionaryType;
    /**
    * 字典名称
    */
    @NotBlank(message = "字典名称不能为空",groups = {DictionaryTypeDTO.AddDictionaryTypeDTO.class,DictionaryTypeDTO.UpdateDictionaryTypeDTO.class})
    private  String dictionaryName;
    /**
    * 菜单0层级名称
    */
    @NotBlank(message = "菜单0层级名称不能为空",groups = {DictionaryTypeDTO.AddDictionaryTypeDTO.class,DictionaryTypeDTO.UpdateDictionaryTypeDTO.class})
    private  String menuZerothName;
    /**
    * 菜单1层级名称
    */
    @NotBlank(message = "菜单1层级名称不能为空",groups = {DictionaryTypeDTO.AddDictionaryTypeDTO.class,DictionaryTypeDTO.UpdateDictionaryTypeDTO.class})
    private  String menuFirstName;
    /**
    * 菜单2层级名称
    */
    @NotBlank(message = "菜单2层级名称不能为空",groups = {DictionaryTypeDTO.AddDictionaryTypeDTO.class,DictionaryTypeDTO.UpdateDictionaryTypeDTO.class})
    private  String menuSecondName;
    /**
    * 备注
    */
    private  String remark;
    /**
    * 状态:0失效;1生效
    */
    @NotNull(message = "字典状态不能为空",groups = {DictionaryTypeDTO.AddDictionaryTypeDTO.class,DictionaryTypeDTO.UpdateDictionaryTypeDTO.class})
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

