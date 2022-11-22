package net.qixiaowei.operate.cloud.api.domain.employee;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 人力预算调整表
* @author TANGMICHI
* @since 2022-11-22
*/
@Data
@Accessors(chain = true)
public class EmployeeBudgetAdjusts extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  employeeBudgetAdjustsId;
     /**
     * 人力预算明细ID
     */
     private  Long  employeeBudgetDetailsId;
     /**
     * 周期数(顺序递增)
     */
     private  Integer  cycleNumber;
     /**
     * 调整人数
     */
     private  Integer  numberAdjust;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

