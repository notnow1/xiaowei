package net.qixiaowei.operate.cloud.api.dto.bonus;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

/**
* 奖金发放申请表
* @author TANGMICHI
* @since 2022-12-08
*/
@Data
@Accessors(chain = true)
public class BonusPayApplicationDTO extends BaseDTO {

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
     * 部门奖金预算详情id
     */
    private Long deptBonusBudgetDetailsId;

    /**
     * 预算部门ID
     */
    private  Long departmentId;
    /**
     * 预算部门名称
     */
    private  String departmentName;
    /**
     * 部门类型 0 申请部门 1 预算部门 2 获奖部门
     */
    private  Integer  departmentType;
    /**
     * 三级项目(奖项类别名称)
     */
    private String salaryItemName;
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
     * 获奖时间-年月
     */
    private  String awardYearMonth;
    /**
    * 申请部门ID
    */
    private  Long applyDepartmentId;
    /**
     * 申请部门ID集合
     */
    private  List<Long> applyDepartmentIds;
    /**
     * 申请部门名称
     */
    private  String applyDepartmentName;


    /**
     * 预算部门ID集合
     */
    private List<Long> budgetDepartmentIds;
    /**
     * 预算部门ID集合(数据库返回为字符串需转化)
     */
    private String budgetDepartmentIdData;
    /**
     * 预算部门名称
     */
    private String budgetDepartmentNames;
    /**
     * 预算部门名称集合
     */
    private List<String> budgetDepartmentList;
    /**
    * 奖项总金额
    */
    private  BigDecimal awardTotalAmount;
    /**
     * 奖金比例
     */
    private  BigDecimal bonusPercentage;
    /**
    * 奖金发放对象:1部门;2员工;3部门+员工
    */
    private  Integer bonusPayObject;
    /**
     * 奖金发放对象名称
     */
    private  String bonusPayObjectName;
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
     * 获奖部门集合
     */
    private List<BonusPayObjectsDTO> bonusPayObjectsDeptDTOs;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 租户ID
    */
    private  Long tenantId;
}

