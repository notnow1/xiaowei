package net.qixiaowei.operate.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.BonusBudgetField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.operate.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.operate.cloud.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description 总奖金包预算-字段配置实现类
 * @Author hzk
 * @Date 2023-02-09 19:55
 **/
@Service
@Slf4j
public class BonusBudgetFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(BonusBudgetField.BUDGET_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.BONUS_BUDGET.getCode()).fieldName(BonusBudgetField.BUDGET_YEAR.getCode()).fieldLabel(BonusBudgetField.BUDGET_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(BonusBudgetField.AMOUNT_BONUS_BUDGET.getCode(), FieldConfig.builder().businessType(BusinessType.BONUS_BUDGET.getCode()).fieldName(BonusBudgetField.AMOUNT_BONUS_BUDGET.getCode()).fieldLabel(BonusBudgetField.AMOUNT_BONUS_BUDGET.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
        INIT_MAP.put(BonusBudgetField.RAISE_SALARY_BONUS_BUDGET.getCode(), FieldConfig.builder().businessType(BusinessType.BONUS_BUDGET.getCode()).fieldName(BonusBudgetField.RAISE_SALARY_BONUS_BUDGET.getCode()).fieldLabel(BonusBudgetField.RAISE_SALARY_BONUS_BUDGET.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
        INIT_MAP.put(BonusBudgetField.PAYMENT_BONUS_BUDGET.getCode(), FieldConfig.builder().businessType(BusinessType.BONUS_BUDGET.getCode()).fieldName(BonusBudgetField.PAYMENT_BONUS_BUDGET.getCode()).fieldLabel(BonusBudgetField.PAYMENT_BONUS_BUDGET.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
        INIT_MAP.put(BonusBudgetField.AMOUNT_WAGE_BUDGET.getCode(), FieldConfig.builder().businessType(BusinessType.BONUS_BUDGET.getCode()).fieldName(BonusBudgetField.AMOUNT_WAGE_BUDGET.getCode()).fieldLabel(BonusBudgetField.AMOUNT_WAGE_BUDGET.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
        INIT_MAP.put(BonusBudgetField.ELASTICITY_BONUS_BUDGET.getCode(), FieldConfig.builder().businessType(BusinessType.BONUS_BUDGET.getCode()).fieldName(BonusBudgetField.ELASTICITY_BONUS_BUDGET.getCode()).fieldLabel(BonusBudgetField.ELASTICITY_BONUS_BUDGET.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.BONUS_BUDGET;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}