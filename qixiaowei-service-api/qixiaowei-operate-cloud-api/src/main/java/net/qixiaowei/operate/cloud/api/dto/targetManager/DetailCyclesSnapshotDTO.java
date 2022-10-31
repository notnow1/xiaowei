package net.qixiaowei.operate.cloud.api.dto.targetManager;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 目标分解详情周期快照表
* @author TANGMICHI
* @since 2022-10-31
*/
@Data
@Accessors(chain = true)
public class DetailCyclesSnapshotDTO {

    //查询检验
    public interface QueryDetailCyclesSnapshotDTO extends Default{

    }
    //新增检验
    public interface AddDetailCyclesSnapshotDTO extends Default{

    }

    //删除检验
    public interface DeleteDetailCyclesSnapshotDTO extends Default{

    }
    //修改检验
    public interface UpdateDetailCyclesSnapshotDTO extends Default{

    }
    /**
    * ID
    */
    private  Long detailCyclesSnapshotId;
    /**
    * 目标分解详情快照ID
    */
    private  Long decomposeDetailsSnapshotId;
    /**
    * 周期数(顺序递增)
    */
    private  Integer cycleNumber;
    /**
    * 周期目标值
    */
    private BigDecimal cycleTarget;
    /**
    * 周期预测值
    */
    private  BigDecimal cycleForecast;
    /**
    * 周期实际值
    */
    private  BigDecimal cycleActual;
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

