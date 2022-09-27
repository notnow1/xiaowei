package net.qixiaowei.system.manage.api.dto.basic;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 部门表
* @author TANGMICHI
* @since 2022-09-27
*/
@Data
@Accessors(chain = true)
public class DepartmentDTO {

    //查询检验
    public interface QueryDepartmentDTO extends Default{

    }
    //新增检验
    public interface AddDepartmentDTO extends Default{

    }

    //新增检验
    public interface DeleteDepartmentDTO extends Default{

    }
    //修改检验
    public interface UpdateDepartmentDTO extends Default{

    }
    /**
    * 部门ID
    */
    private  Long departmentId;
    /**
    * 父级部门ID
    */
    private  Long parentDepartmentId;
    /**
    * 祖级列表ID，按层级用英文逗号隔开
    */
    private  String ancestors;
    /**
    * 部门编码
    */
    private  String departmentCode;
    /**
    * 部门名称
    */
    private  String departmentName;
    /**
    * 部门层级
    */
    private  Integer level;
    /**
    * 部门负责人ID
    */
    private  Long departmentLeaderId;
    /**
    * 部门负责人岗位ID
    */
    private  Long departmentLeaderPostId;
    /**
    * 考核负责人ID
    */
    private  Long examinationLeaderId;
    /**
    * 部门重要性系数
    */
    private BigDecimal departmentImportanceFactor;
    /**
    * 部门描述
    */
    private  String departmentDescription;
    /**
    * 排序
    */
    private  Integer sort;
    /**
    * 状态:0失效;1生效
    */
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

