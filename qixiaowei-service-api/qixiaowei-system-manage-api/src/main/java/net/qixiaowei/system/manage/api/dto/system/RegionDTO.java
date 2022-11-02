package net.qixiaowei.system.manage.api.dto.system;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 区域表
* @author hzk
* @since 2022-10-20
*/
@Data
@Accessors(chain = true)
public class RegionDTO {

    //查询检验
    public interface QueryRegionDTO extends Default{

    }
    //新增检验
    public interface AddRegionDTO extends Default{

    }

    //删除检验
    public interface DeleteRegionDTO extends Default{

    }
    //修改检验
    public interface UpdateRegionDTO extends Default{

    }
    /**
    * ID
    */
    private  Long regionId;
    /**
    * 父级区域ID
    */
    private  Long parentRegionId;
    /**
    * 祖级列表ID，按层级用英文逗号隔开
    */
    private  String ancestors;
    /**
    * 区域名称
    */
    private  String regionName;
    /**
    * 省级区划编号
    */
    private  String provinceCode;
    /**
    * 省级名称
    */
    private  String provinceName;
    /**
    * 市级区划编号
    */
    private  String cityCode;
    /**
    * 市级名称
    */
    private  String cityName;
    /**
    * 区级区划编号
    */
    private  String districtCode;
    /**
    * 区级名称
    */
    private  String districtName;
    /**
    * 镇级区划编号
    */
    private  String townCode;
    /**
    * 镇级名称
    */
    private  String townName;
    /**
    * 村级区划编号
    */
    private  String villageCode;
    /**
    * 村级名称
    */
    private  String villageName;
    /**
    * 层级
    */
    private  Integer level;
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

