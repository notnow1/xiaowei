package net.qixiaowei.strategy.cloud.api.domain.strategyDecode;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 战略举措清单任务表
* @author Graves
* @since 2023-03-07
*/
@Data
@Accessors(chain = true)
public class StrategyMeasureTask extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  strategyMeasureTaskId;
     /**
     * 战略举措清单ID
     */
     private  Long  strategyMeasureId;
     /**
     * 战略举措清单详情ID
     */
     private  Long  strategyMeasureDetailId;
     /**
     * 关键任务
     */
     private  String  keyTask;
     /**
     * 闭环标准
     */
     private  String  closeStandard;
     /**
     * 责任部门
     */
     private  Long  dutyDepartmentId;
     /**
     * 责任人员ID
     */
     private  Long  dutyEmployeeId;
     /**
     * 责任人员姓名
     */
     private  String  dutyEmployeeName;
     /**
     * 责任人员编码
     */
     private  String  dutyEmployeeCode;
     /**
     * 排序
     */
     private  Integer  sort;

}

