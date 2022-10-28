package net.qixiaowei.operate.cloud.api.dto.targetManager;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 目标制定订单表
* @author Graves
* @since 2022-10-27
*/
@Data
@Accessors(chain = true)
public class TargetSettingOrderDTO {

    //查询检验
    public interface QueryTargetSettingOrderDTO extends Default{

    }
    //新增检验
    public interface AddTargetSettingOrderDTO extends Default{

    }

    //删除检验
    public interface DeleteTargetSettingOrderDTO extends Default{

    }
    //修改检验
    public interface UpdateTargetSettingOrderDTO extends Default{

    }
    /**
    * ID
    */
    private  Long targetSettingOrderId;
    /**
    * 目标制定ID
    */
    private  Long targetSettingId;
    /**
    * 历史年度
    */
    private  Integer historyYear;
    /**
    * 历史年度实际值
    */
    private BigDecimal historyActual;
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

