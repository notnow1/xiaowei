package net.qixiaowei.operate.cloud.api.dto.targetManager;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 目标结果表
* @author TANGMICHI
* @since 2022-11-07
*/
@Data
@Accessors(chain = true)
public class TargetOutcomeDTO {

    //查询检验
    public interface QueryTargetOutcomeDTO extends Default{

    }
    //新增检验
    public interface AddTargetOutcomeDTO extends Default{

    }

    //删除检验
    public interface DeleteTargetOutcomeDTO extends Default{

    }
    //修改检验
    public interface UpdateTargetOutcomeDTO extends Default{

    }
    /**
    * ID
    */
    private  Long targetOutcomeId;
    /**
    * 目标年度
    */
    private  Integer targetYear;
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

