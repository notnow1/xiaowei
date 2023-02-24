package net.qixiaowei.operate.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.DeptBonusBudgetField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.operate.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.operate.cloud.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description 部门奖金包预算-字段配置实现类
 * @Author hzk
 * @Date 2023-02-09 19:55
 **/
@Service
@Slf4j
public class DeptBonusBudgetFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(DeptBonusBudgetField.BUDGET_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.DEPT_BONUS_BUDGET.getCode()).fieldName(DeptBonusBudgetField.BUDGET_YEAR.getCode()).fieldLabel(DeptBonusBudgetField.BUDGET_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(DeptBonusBudgetField.AMOUNT_BONUS_BUDGET.getCode(), FieldConfig.builder().businessType(BusinessType.DEPT_BONUS_BUDGET.getCode()).fieldName(DeptBonusBudgetField.AMOUNT_BONUS_BUDGET.getCode()).fieldLabel(DeptBonusBudgetField.AMOUNT_BONUS_BUDGET.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
        INIT_MAP.put(DeptBonusBudgetField.STRATEGY_AWARD_AMOUNT.getCode(), FieldConfig.builder().businessType(BusinessType.DEPT_BONUS_BUDGET.getCode()).fieldName(DeptBonusBudgetField.STRATEGY_AWARD_AMOUNT.getCode()).fieldLabel(DeptBonusBudgetField.STRATEGY_AWARD_AMOUNT.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
        INIT_MAP.put(DeptBonusBudgetField.STRATEGY_AWARD_PERCENTAGE.getCode(), FieldConfig.builder().businessType(BusinessType.DEPT_BONUS_BUDGET.getCode()).fieldName(DeptBonusBudgetField.STRATEGY_AWARD_PERCENTAGE.getCode()).fieldLabel(DeptBonusBudgetField.STRATEGY_AWARD_PERCENTAGE.getInfo()).fieldType(FieldType.PERCENTAGE.getCode()).build());
        INIT_MAP.put(DeptBonusBudgetField.DEPT_AMOUNT_BONUS.getCode(), FieldConfig.builder().businessType(BusinessType.DEPT_BONUS_BUDGET.getCode()).fieldName(DeptBonusBudgetField.DEPT_AMOUNT_BONUS.getCode()).fieldLabel(DeptBonusBudgetField.DEPT_AMOUNT_BONUS.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.DEPT_BONUS_BUDGET;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}