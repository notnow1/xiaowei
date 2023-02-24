package net.qixiaowei.operate.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.EmployeeAnnualBonusField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.operate.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.operate.cloud.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description 个人年终奖-字段配置实现类
 * @Author hzk
 * @Date 2023-02-09 19:55
 **/
@Service
@Slf4j
public class EmployeeAnnualBonusFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(EmployeeAnnualBonusField.ANNUAL_BONUS_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE_ANNUAL_BONUS.getCode()).fieldName(EmployeeAnnualBonusField.ANNUAL_BONUS_YEAR.getCode()).fieldLabel(EmployeeAnnualBonusField.ANNUAL_BONUS_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmployeeAnnualBonusField.DEPARTMENT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE_ANNUAL_BONUS.getCode()).fieldName(EmployeeAnnualBonusField.DEPARTMENT_ID.getCode()).fieldLabel(EmployeeAnnualBonusField.DEPARTMENT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmployeeAnnualBonusField.DISTRIBUTE_BONUS_AMOUNT.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE_ANNUAL_BONUS.getCode()).fieldName(EmployeeAnnualBonusField.DISTRIBUTE_BONUS_AMOUNT.getCode()).fieldLabel(EmployeeAnnualBonusField.DISTRIBUTE_BONUS_AMOUNT.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
        INIT_MAP.put(EmployeeAnnualBonusField.APPLY_DEPARTMENT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE_ANNUAL_BONUS.getCode()).fieldName(EmployeeAnnualBonusField.APPLY_DEPARTMENT_ID.getCode()).fieldLabel(EmployeeAnnualBonusField.APPLY_DEPARTMENT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmployeeAnnualBonusField.APPLY_EMPLOYEE_ID.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE_ANNUAL_BONUS.getCode()).fieldName(EmployeeAnnualBonusField.APPLY_EMPLOYEE_ID.getCode()).fieldLabel(EmployeeAnnualBonusField.APPLY_EMPLOYEE_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmployeeAnnualBonusField.APPLY_BONUS_AMOUNT.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE_ANNUAL_BONUS.getCode()).fieldName(EmployeeAnnualBonusField.APPLY_BONUS_AMOUNT.getCode()).fieldLabel(EmployeeAnnualBonusField.APPLY_BONUS_AMOUNT.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
        INIT_MAP.put(EmployeeAnnualBonusField.COMMENT_FLAG.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE_ANNUAL_BONUS.getCode()).fieldName(EmployeeAnnualBonusField.COMMENT_FLAG.getCode()).fieldLabel(EmployeeAnnualBonusField.COMMENT_FLAG.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmployeeAnnualBonusField.STATUS.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE_ANNUAL_BONUS.getCode()).fieldName(EmployeeAnnualBonusField.STATUS.getCode()).fieldLabel(EmployeeAnnualBonusField.STATUS.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmployeeAnnualBonusField.COMMENT_DATE.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE_ANNUAL_BONUS.getCode()).fieldName(EmployeeAnnualBonusField.COMMENT_DATE.getCode()).fieldLabel(EmployeeAnnualBonusField.COMMENT_DATE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.EMPLOYEE_ANNUAL_BONUS;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}