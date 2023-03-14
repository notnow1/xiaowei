package net.qixiaowei.operate.cloud.api.dto.targetManager;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

/**
* 区域表
* @author Graves
* @since 2022-10-07
*/
@Data
@Accessors(chain = true)
public class AreaDTO extends BaseDTO {

    //查询检验
    public interface QueryAreaDTO extends Default{

    }
    //新增检验
    public interface AddAreaDTO extends Default{

    }

    //新增检验
    public interface DeleteAreaDTO extends Default{

    }
    //修改检验
    public interface UpdateAreaDTO extends Default{

    }
    /**
    * ID
    */
    private  Long areaId;
    /**
    * 区域编码
    */
    private  String areaCode;
    /**
    * 区域名称
    */
    private  String areaName;
    /**
    * 地区ID集合,用英文逗号隔开
    */
    private  String regionIds;
    /**
    * 地区名称集合,用英文逗号隔开
    */
    private  String regionNames;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;

}

