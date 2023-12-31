package net.qixiaowei.system.manage.api.dto.basic;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.util.Map;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

/**
* 字典数据表
* @author TANGMICHI
* @since 2022-10-15
*/
@Data
@Accessors(chain = true)
public class DictionaryDataDTO extends BaseDTO {

    //查询检验
    public interface QueryDictionaryDataDTO extends Default{

    }
    //新增检验
    public interface AddDictionaryDataDTO extends Default{

    }

    //删除检验
    public interface DeleteDictionaryDataDTO extends Default{

    }
    //修改检验
    public interface UpdateDictionaryDataDTO extends Default{

    }
    /**
    * ID
    */
    private  Long dictionaryDataId;

    /**
     * 字典类型
     */
    private  String dictionaryType;
    /**
    * 字典类型ID
    */
    private  Long dictionaryTypeId;
    /**
    * 字典标签
    */
    private  String dictionaryLabel;
    /**
    * 字典值
    */
    private  String dictionaryValue;
    /**
    * 默认标记:0否;1是
    */
    private  Integer defaultFlag;
    /**
    * 排序
    */
    private  Integer sort;
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
}

