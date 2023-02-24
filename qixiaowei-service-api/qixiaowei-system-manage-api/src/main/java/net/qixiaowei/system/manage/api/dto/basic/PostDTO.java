package net.qixiaowei.system.manage.api.dto.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 岗位表
 *
 * @author TANGMICHI
 * @since 2022-09-30
 */
@Data
@Accessors(chain = true)
public class PostDTO {

    //查询检验
    public interface QueryPostDTO extends Default {

    }

    //新增检验
    public interface AddPostDTO extends Default {

    }

    //删除检验
    public interface DeletePostDTO extends Default {

    }

    //修改检验
    public interface UpdatePostDTO extends Default {

    }

    /**
     * ID
     */
    @NotNull(message = "岗位ID不能为空", groups = {PostDTO.UpdatePostDTO.class, PostDTO.DeletePostDTO.class})
    private Long postId;
    /**
     * 岗位编码
     */
    @NotBlank(message = "岗位编码不能为空", groups = {PostDTO.AddPostDTO.class, PostDTO.UpdatePostDTO.class})
    private String postCode;

    /**
     * 级别前缀编码
     */
    private String rankPrefixCode;
    /**
     * 岗位名称
     */
    @NotBlank(message = "岗位名称不能为空", groups = {PostDTO.AddPostDTO.class, PostDTO.UpdatePostDTO.class})
    private String postName;
    /**
     * 职级体系ID
     */
//    @NotNull(message = "职级体系不能为空",groups = {PostDTO.AddPostDTO.class,PostDTO.UpdatePostDTO.class})
    private Long officialRankSystemId;


    /**
     * 职级体系名称
     */
    private String officialRankSystemName;
    /**
     * 岗位职级下限
     */
//    @NotNull(message = "岗位职级下限不能为空",groups = {PostDTO.AddPostDTO.class,PostDTO.UpdatePostDTO.class})
    private Integer postRankLower;

    private String postRankLowerName;
    /**
     * 岗位职级
     */
//    @NotNull(message = "岗位职级不能为空",groups = {PostDTO.AddPostDTO.class,PostDTO.UpdatePostDTO.class})
    private Integer postRank;

    private String postRankName;
    /**
     * 岗位职级上限
     */
//    @NotNull(message = "岗位职级上限不能为空",groups = {PostDTO.AddPostDTO.class,PostDTO.UpdatePostDTO.class})
    private Integer postRankUpper;

    private String postRankUpperName;
    /**
     * 岗位说明书URL路径
     */
//    @Pattern(regexp = "^http://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$",message = "请输入正确的url地址",groups = {TenantDTO.AddTenantDTO.class,TenantDTO.UpdateTenantDTO.class,TenantDTO.UpdateTenantInfoDTO.class})
    private String postDescription;
    /**
     * 状态:0失效;1生效
     */
//    @NotNull(message = "岗位状态不能为空",groups = {PostDTO.AddPostDTO.class,PostDTO.UpdatePostDTO.class})
    private Integer status;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 部门表集合
     */
    private List<DepartmentDTO> departmentDTOList;
    /**
     * 部门ID
     */
    private Long departmentId;
    /**
     * 部门编码
     */
    private  String departmentCode;
    /**
     * 部门名称
     */
    private  String departmentName;
    /**
     * 请求参数
     */
    private Map<String, Object> params;
}

