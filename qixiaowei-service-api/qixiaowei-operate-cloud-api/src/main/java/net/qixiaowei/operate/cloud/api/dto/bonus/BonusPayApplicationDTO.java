package net.qixiaowei.operate.cloud.api.dto.bonus;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 奖金发放申请表
* @author TANGMICHI
* @since 2022-12-08
*/
@Data
@Accessors(chain = true)
public class BonusPayApplicationDTO {

    //查询检验
    public interface QueryBonusPayApplicationDTO extends Default{

    }
    //新增检验
    public interface AddBonusPayApplicationDTO extends Default{

    }

    //删除检验
    public interface DeleteBonusPayApplicationDTO extends Default{

    }
    //修改检验
    public interface UpdateBonusPayApplicationDTO extends Default{

    }
    /**
    * ID
    */
    private  Long bonusPayApplicationId;
    /**
    * 奖项类别,工资条ID
    */
    private  Long salaryItemId;
    /**
    * 奖项编码
    */
    private  String awardCode;
    /**
    * 奖项名称
    */
    private  String awardName;
    /**
    * 获奖时间-年
    */
    private  Integer awardYear;
    /**
    * 获奖时间-月
    */
    private  Integer awardMonth;
    /**
    * 申请部门ID
    */
    private  Long applyDepartmentId;
    /**
    * 奖项总金额
    */
    private  BigDecimal awardTotalAmount;
    /**
    * 奖金发放对象:1部门;2员工;3部门+员工
    */
    private  Integer bonusPayObject;
    /**
    * 奖项事迹描述
    */
    private  String awardDescription;
    /**
     * 奖金发放预算部门比例集合
     */
    private List<BonusPayBudgetDeptDTO> bonusPayBudgetDeptDTOs;
    /**
     * 获奖员工集合
     */
    private List<BonusPayObjectsDTO> bonusPayObjectsEmployeeDTOs;
    /**
     * 获奖员工集合
     */
    private List<BonusPayObjectsDTO> bonusPayObjectsDeptDTOs;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;
    /**
    * 租户ID
    */
    private  Long tenantId;

}

