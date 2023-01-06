package net.qixiaowei.system.manage.api.domain.log;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 操作日志表
* @author hzk
* @since 2023-01-04
*/
@Data
@Accessors(chain = true)
public class OperationLog extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  operationLogId;
     /**
     * 业务类型
     */
     private  Integer  businessType;
     /**
     * 业务ID
     */
     private  Long  businessId;
     /**
     * 操作类型
     */
     private  Integer  operationType;
     /**
     * 操作时间
     */
     private  Date   operationTime;
     /**
     * 操作者用户帐号
     */
     private  String  operatorUserAccount;
     /**
     * 操作者姓名
     */
     private  String  operatorEmployeeName;
     /**
     * 操作者工号
     */
     private  String  operatorEmployeeCode;
     /**
     * 操作者部门名称
     */
     private  String  operatorDepartmentName;
     /**
     * 操作者岗位名称
     */
     private  String  operatorPostName;
     /**
     * 用户代理
     */
     private  String  userAgent;
     /**
     * 请求方式
     */
     private  String  requestMethod;
     /**
     * 请求URL
     */
     private  String  requestUrl;
     /**
     * 请求方法
     */
     private  String  method;
     /**
     * 标题
     */
     private  String  title;
     /**
     * 主机地址
     */
     private  String  operatorIp;
     /**
     * 请求参数
     */
     private  String  requestParam;
     /**
     * 返回数据
     */
     private  String  resultData;
     /**
     * 错误消息
     */
     private  String  errorMessage;
     /**
     * 操作状态:0错误;1正常
     */
     private  Integer  status;

}

