package net.qixiaowei.operate.cloud.api.dto.targetManager;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 目标分解详情快照表
* @author TANGMICHI
* @since 2022-10-31
*/
@Data
@Accessors(chain = true)
public class DecomposeDetailsSnapshotDTO {

    //查询检验
    public interface QueryDecomposeDetailsSnapshotDTO extends Default{

    }
    //新增检验
    public interface AddDecomposeDetailsSnapshotDTO extends Default{

    }

    //删除检验
    public interface DeleteDecomposeDetailsSnapshotDTO extends Default{

    }
    //修改检验
    public interface UpdateDecomposeDetailsSnapshotDTO extends Default{

    }
    /**
    * ID
    */
    private  Long decomposeDetailsSnapshotId;
    /**
    * 目标分解历史版本ID
    */
    private  Long targetDecomposeHistoryId;
    /**
    * 员工ID
    */
    private  Long employeeId;
    /**
    * 区域ID
    */
    private  Long areaId;
    /**
    * 部门ID
    */
    private  Long departmentId;
    /**
    * 产品ID
    */
    private  Long productId;
    /**
    * 省份ID
    */
    private  Long regionId;
    /**
    * 行业ID
    */
    private  Long industryId;
    /**
    * 负责人ID
    */
    private  Long principalEmployeeId;
    /**
    * 汇总目标值
    */
    private BigDecimal amountTarget;
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

