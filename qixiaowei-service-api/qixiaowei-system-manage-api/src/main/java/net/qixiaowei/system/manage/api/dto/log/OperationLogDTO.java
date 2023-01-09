package net.qixiaowei.system.manage.api.dto.log;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 操作日志表
 *
 * @author hzk
 * @since 2023-01-04
 */
@Data
@Accessors(chain = true)
public class OperationLogDTO {

    //查询检验
    public interface QueryOperationLogDTO extends Default {

    }

    //新增检验
    public interface AddOperationLogDTO extends Default {

    }

    //删除检验
    public interface DeleteOperationLogDTO extends Default {

    }

    //修改检验
    public interface UpdateOperationLogDTO extends Default {

    }

    /**
     * ID
     */
    private Long operationLogId;
    /**
     * 业务类型
     */
    @NotNull(message = "业务类型不能为空", groups = {OperationLogDTO.QueryOperationLogDTO.class})
    private Integer businessType;
    /**
     * 业务ID
     */
    private Long businessId;
    /**
     * 操作类型
     */
    private Integer operationType;
    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
    private Date operationTime;
    /**
     * 操作者用户帐号
     */
    private String operatorUserAccount;
    /**
     * 操作者姓名
     */
    private String operatorEmployeeName;
    /**
     * 操作者工号
     */
    private String operatorEmployeeCode;
    /**
     * 操作者部门名称
     */
    private String operatorDepartmentName;
    /**
     * 操作者岗位名称
     */
    private String operatorPostName;
    /**
     * 用户代理
     */
    private String userAgent;
    /**
     * 请求方式
     */
    private String requestMethod;
    /**
     * 请求URL
     */
    private String requestUrl;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 标题
     */
    private String title;
    /**
     * 主机地址
     */
    private String operatorIp;
    /**
     * 请求参数
     */
    private String requestParam;
    /**
     * 返回数据
     */
    private String resultData;
    /**
     * 错误消息
     */
    private String errorMessage;
    /**
     * 操作状态:0错误;1正常
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
    /**
     * 租户ID
     */
    private Long tenantId;

}

