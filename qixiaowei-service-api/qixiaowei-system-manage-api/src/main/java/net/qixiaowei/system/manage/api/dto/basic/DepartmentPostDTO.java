package net.qixiaowei.system.manage.api.dto.basic;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 部门岗位关联表
* @author TANGMICHI
* @since 2022-09-27
*/
@Data
@Accessors(chain = true)
public class DepartmentPostDTO {

    //查询检验
    public interface QueryDepartmentPostDTO extends Default{

    }
    //新增检验
    public interface AddDepartmentPostDTO extends Default{

    }

    //新增检验
    public interface DeleteDepartmentPostDTO extends Default{

    }
    //修改检验
    public interface UpdateDepartmentPostDTO extends Default{

    }


    /**
     * 岗位职级
     */
    private  Integer postRank;
    /**
     * 岗位编码
     */
    private  String postCode;
    /**
     * 岗位名称
     */
    private  String postName;
    /**
     * 职级体系ID
     */
    private  Long officialRankSystemId;
    /**
    * ID
    */
    private  Long departmentPostId;
    /**
    * 部门ID
    */
    private  Long departmentId;
    /**
    * 岗位ID
    */
    private  Long postId;
    /**
    * 部门排序
    */
    private  Integer departmentSort;
    /**
    * 岗位排序
    */
    private  Integer postSort;
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

