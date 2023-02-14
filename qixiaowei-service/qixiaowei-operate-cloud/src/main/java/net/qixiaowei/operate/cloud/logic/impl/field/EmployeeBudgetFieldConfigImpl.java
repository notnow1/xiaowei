package net.qixiaowei.operate.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.EmployeeBudgetField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.operate.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.operate.cloud.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description 人力预算调控-字段配置实现类
 * @Author hzk
 * @Date 2023-02-09 19:55
 **/
@Service
@Slf4j
public class EmployeeBudgetFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(EmployeeBudgetField.DEPARTMENT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE_BUDGET.getCode()).fieldName(EmployeeBudgetField.DEPARTMENT_ID.getCode()).fieldLabel(EmployeeBudgetField.DEPARTMENT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmployeeBudgetField.BUDGET_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE_BUDGET.getCode()).fieldName(EmployeeBudgetField.BUDGET_YEAR.getCode()).fieldLabel(EmployeeBudgetField.BUDGET_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmployeeBudgetField.OFFICIAL_RANK_SYSTEM_ID.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE_BUDGET.getCode()).fieldName(EmployeeBudgetField.OFFICIAL_RANK_SYSTEM_ID.getCode()).fieldLabel(EmployeeBudgetField.OFFICIAL_RANK_SYSTEM_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmployeeBudgetField.AMOUNT_LAST_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE_BUDGET.getCode()).fieldName(EmployeeBudgetField.AMOUNT_LAST_YEAR.getCode()).fieldLabel(EmployeeBudgetField.AMOUNT_LAST_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmployeeBudgetField.AMOUNT_ADJUST.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE_BUDGET.getCode()).fieldName(EmployeeBudgetField.AMOUNT_ADJUST.getCode()).fieldLabel(EmployeeBudgetField.AMOUNT_ADJUST.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmployeeBudgetField.AMOUNT_AVERAGE_ADJUST.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE_BUDGET.getCode()).fieldName(EmployeeBudgetField.AMOUNT_AVERAGE_ADJUST.getCode()).fieldLabel(EmployeeBudgetField.AMOUNT_AVERAGE_ADJUST.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmployeeBudgetField.ANNUAL_AVERAGE_NUM.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE_BUDGET.getCode()).fieldName(EmployeeBudgetField.ANNUAL_AVERAGE_NUM.getCode()).fieldLabel(EmployeeBudgetField.ANNUAL_AVERAGE_NUM.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmployeeBudgetField.BUDGET_CYCLE.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE_BUDGET.getCode()).fieldName(EmployeeBudgetField.BUDGET_CYCLE.getCode()).fieldLabel(EmployeeBudgetField.BUDGET_CYCLE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.EMPLOYEE_BUDGET;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}