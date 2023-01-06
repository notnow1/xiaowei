package net.qixiaowei.system.manage.api.vo.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 操作日志返回VO
 *
 * @author hzk
 * @since 2023-01-04
 */
@Data
@Accessors(chain = true)
public class OperationLogVO {

    /**
     * ID
     */
    private Long operationLogId;
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
     * 标题
     */
    private String title;
    /**
     * 操作状态:0错误;1正常
     */
    private Integer status;

}

