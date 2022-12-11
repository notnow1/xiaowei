package net.qixiaowei.message.api.dto.backlog;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 待办事项表
 *
 * @author hzk
 * @since 2022-12-11
 */
@Data
@Accessors(chain = true)
public class BacklogDTO {

    //查询检验
    public interface QueryBacklogDTO extends Default {

    }

    //新增检验
    public interface AddBacklogDTO extends Default {

    }

    //删除检验
    public interface DeleteBacklogDTO extends Default {

    }

    //修改检验
    public interface UpdateBacklogDTO extends Default {

    }

    /**
     * ID
     */
    private Long backlogId;
    /**
     * 业务类型
     */
    @NotNull(message = "业务类型不能为空", groups = {BacklogDTO.AddBacklogDTO.class})
    private Integer businessType;
    /**
     * 业务子类型
     */
    @NotNull(message = "业务子类型不能为空", groups = {BacklogDTO.AddBacklogDTO.class, BacklogDTO.UpdateBacklogDTO.class})
    private Integer businessSubtype;
    /**
     * 业务ID
     */
    @NotNull(message = "业务ID不能为空", groups = {BacklogDTO.AddBacklogDTO.class, BacklogDTO.UpdateBacklogDTO.class})
    private Long businessId;
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空", groups = {BacklogDTO.AddBacklogDTO.class})
    private Long userId;
    /**
     * 待办名称
     */
    private String backlogName;
    /**
     * 待办事项发起者
     */
    private Long backlogInitiator;
    /**
     * 待办事项发起者名称
     */
    private String backlogInitiatorName;
    /**
     * 待办事项发起时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date backlogInitiationTime;
    /**
     * 待办事项处理时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date backlogProcessTime;
    /**
     * 状态:0待办;1已办
     */
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


}

