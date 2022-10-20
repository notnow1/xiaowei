package net.qixiaowei.system.manage.api.dto.basic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDTO;

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
     * 组织中间表ID
     */
    private  Long departmentPostId;
    /**
    * 部门ID
    */
    @NotNull(message = "id不能为空",groups = {DepartmentDTO.DeleteDepartmentDTO.class,DepartmentDTO.UpdateDepartmentDTO.class})
    private  Long departmentId;


    /**
     * 部门ID集合查询子集
     */
    private  List<String> departmentIdList;
    /**
    * 父级部门ID
    */
    private  Long parentDepartmentId;

    /**
     * 父级部门名称
     */
    private  String parentDepartmentName;
    /**
    * 祖级列表ID，按层级用英文逗号隔开
    */
    private  String ancestors;
    /**
    * 部门编码
    */
    @NotBlank(message = "组织编码不能为空",groups = {DepartmentDTO.AddDepartmentDTO.class,DepartmentDTO.UpdateDepartmentDTO.class})
    private  String departmentCode;

    /**
     * 部门编码集合
     */
    private  List<String> departmentCodeList;
    /**
    * 部门名称
    */
    @NotBlank(message = "组织名称不能为空",groups = {DepartmentDTO.AddDepartmentDTO.class,DepartmentDTO.UpdateDepartmentDTO.class})
    private  String departmentName;

    /**
     * 组织名称集合
     */
    private List<String> departmentNamelList;
    /**
    * 部门层级
    */
    @NotNull(message = "组织层级不能为空",groups = {DepartmentDTO.AddDepartmentDTO.class,DepartmentDTO.UpdateDepartmentDTO.class})
    private  Integer level;
    /**
    * 部门负责人ID
    */
    private  Long departmentLeaderId;
    /**
     * 部门负责人名称
     */
    private  String departmentLeaderName;

    /**
    * 部门负责人岗位ID
    */
    private  Long departmentLeaderPostId;


    /**
     * 部门负责人岗位名称
     */
    private  String departmentLeaderPostName;
    /**
    * 考核负责人ID
    */
    private  Long examinationLeaderId;

    /**
     * 考核负责人名称
     */
    private  String examinationLeaderName;
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
    /**
     * 部门岗位关联表
     */
    private List<DepartmentPostDTO>  departmentPostDTOList;


    /**
     * 岗位表
     */
    private List<PostDTO>  postDTOList;

    /**
     * 部门员工表
     */
    private List<EmployeeDTO>  employeeDTOList;
    /**
     * 组织子节点信息
     */
    private List<DepartmentDTO> children;
}

