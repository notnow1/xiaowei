package net.qixiaowei.system.manage.api.dto.basic;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.util.List;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 国家表
* @author TANGMICHI
* @since 2022-10-20
*/
@Data
@Accessors(chain = true)
public class CountryDTO {

    //查询检验
    public interface QueryCountryDTO extends Default{

    }
    //新增检验
    public interface AddCountryDTO extends Default{

    }

    //删除检验
    public interface DeleteCountryDTO extends Default{

    }
    //修改检验
    public interface UpdateCountryDTO extends Default{

    }
    /**
    * ID
    */
    private  Long countryId;
    /**
    * 父级国家ID
    */
    private  Long parentCountryId;
    /**
    * 国家名称
    */
    private  String countryName;
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
    private List<CountryDTO> children;
 }

