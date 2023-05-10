package net.qixiaowei.operate.cloud.api.dto.salary;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

/**
 * 工资项
 *
 * @author Graves
 * @since 2022-10-05
 */
@Data
@Accessors(chain = true)
public class SalaryItemDTO extends BaseDTO {

    //查询检验
    public interface QuerySalaryItemDTO extends Default {

    }

    //新增检验
    public interface AddSalaryItemDTO extends Default {

    }

    //新增检验
    public interface DeleteSalaryItemDTO extends Default {

    }

    //修改检验
    public interface UpdateSalaryItemDTO extends Default {

    }

    /**
     * ID
     */
    private Long salaryItemId;
    /**
     * 一级项目:1总工资包;2总奖金包;3总扣减项
     */
    private Integer firstLevelItem;
    /**
     * 一级项目:1总工资包;2总奖金包;3总扣减项
     */
    private String firstLevelItemValue;
    /**
     * 二级项目:1工资;2津贴;3福利;4奖金;5代扣代缴;6其他扣款
     */
    private Integer secondLevelItem;
    /**
     * 二级项目:1工资;2津贴;3福利;4奖金;5代扣代缴;6其他扣款
     */
    private String secondLevelItemValue;
    /**
     * 三级项目
     */
    private String thirdLevelItem;
    /**
     * 作用范围：1部门;2公司
     */
    private Integer scope;
    /**
     * 作用范围：1部门;2公司
     */
    private String scopeName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 是否被选中(1-选中，0-未选中)
     */
    private Integer isSelect;
    /**
     * 选中的工资条列表
     */
    private List<Long> selectSalaryItem;
}

