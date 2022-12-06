package net.qixiaowei.operate.cloud.api.dto.performance;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 绩效考核对象快照表
* @author Graves
* @since 2022-12-05
*/
@Data
@Accessors(chain = true)
public class PerformAppraisalObjectSnapDTO {

    //查询检验
    public interface QueryPerformAppraisalObjectSnapDTO extends Default{

    }
    //新增检验
    public interface AddPerformAppraisalObjectSnapDTO extends Default{

    }

    //删除检验
    public interface DeletePerformAppraisalObjectSnapDTO extends Default{

    }
    //修改检验
    public interface UpdatePerformAppraisalObjectSnapDTO extends Default{

    }
    /**
    * ID
    */
    private  Long appraisalObjectSnapId;
    /**
    * 绩效考核对象ID
    */
    private  Long performAppraisalObjectsId;
    /**
    * 考核对象名称
    */
    private  String appraisalObjectName;
    /**
    * 考核对象编码
    */
    private  String appraisalObjectCode;
    /**
    * 部门ID
    */
    private  Long departmentId;
    /**
    * 部门名称
    */
    private  String departmentName;
    /**
    * 岗位ID
    */
    private  Long postId;
    /**
    * 岗位名称
    */
    private  String postName;
    /**
    * 职级体系ID
    */
    private  Long officialRankSystemId;
    /**
    * 职级
    */
    private  Integer officialRank;
    /**
    * 职级名称
    */
    private  String officialRankName;
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

