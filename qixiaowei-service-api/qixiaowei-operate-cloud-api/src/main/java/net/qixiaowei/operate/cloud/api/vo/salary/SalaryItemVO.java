package net.qixiaowei.operate.cloud.api.vo.salary;

import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryItemDTO;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.List;

/**
 * 工资项
 *
 * @author Graves
 * @since 2022-10-05
 */
@Data
public class SalaryItemVO extends BaseDTO {
    /**
     * 工资项大列名
     */
    private String salaryItemName;
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
     * 状态:0失效;1生效
     */
    private Integer status;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 工资条列表
     */
    private List<SalaryItemDTO> salaryItemDTOS;
}

