package net.qixiaowei.operate.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.SalaryPayField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.operate.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.operate.cloud.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description 用户-字段配置实现类
 * @Author hzk
 * @Date 2023-02-09 19:55
 **/
@Service
@Slf4j
public class SalaryPayFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(SalaryPayField.PAY_YEAR_MONTH.getCode(), FieldConfig.builder().businessType(BusinessType.SALARY_PAY.getCode()).fieldName(SalaryPayField.PAY_YEAR_MONTH.getCode()).fieldLabel(SalaryPayField.PAY_YEAR_MONTH.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(SalaryPayField.EMPLOYEE_CODE.getCode(), FieldConfig.builder().businessType(BusinessType.SALARY_PAY.getCode()).fieldName(SalaryPayField.EMPLOYEE_CODE.getCode()).fieldLabel(SalaryPayField.EMPLOYEE_CODE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(SalaryPayField.EMPLOYEE_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.SALARY_PAY.getCode()).fieldName(SalaryPayField.EMPLOYEE_NAME.getCode()).fieldLabel(SalaryPayField.EMPLOYEE_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(SalaryPayField.TOP_LEVEL_DEPARTMENT_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.SALARY_PAY.getCode()).fieldName(SalaryPayField.TOP_LEVEL_DEPARTMENT_NAME.getCode()).fieldLabel(SalaryPayField.TOP_LEVEL_DEPARTMENT_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(SalaryPayField.EMPLOYEE_DEPARTMENT_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.SALARY_PAY.getCode()).fieldName(SalaryPayField.EMPLOYEE_DEPARTMENT_NAME.getCode()).fieldLabel(SalaryPayField.EMPLOYEE_DEPARTMENT_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(SalaryPayField.EMPLOYEE_POST_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.SALARY_PAY.getCode()).fieldName(SalaryPayField.EMPLOYEE_POST_NAME.getCode()).fieldLabel(SalaryPayField.EMPLOYEE_POST_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(SalaryPayField.EMPLOYEE_RANK_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.SALARY_PAY.getCode()).fieldName(SalaryPayField.EMPLOYEE_RANK_NAME.getCode()).fieldLabel(SalaryPayField.EMPLOYEE_RANK_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(SalaryPayField.TOTAL_WAGES.getCode(), FieldConfig.builder().businessType(BusinessType.SALARY_PAY.getCode()).fieldName(SalaryPayField.TOTAL_WAGES.getCode()).fieldLabel(SalaryPayField.TOTAL_WAGES.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
        INIT_MAP.put(SalaryPayField.BONUS_AMOUNT.getCode(), FieldConfig.builder().businessType(BusinessType.SALARY_PAY.getCode()).fieldName(SalaryPayField.BONUS_AMOUNT.getCode()).fieldLabel(SalaryPayField.BONUS_AMOUNT.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
        INIT_MAP.put(SalaryPayField.TOTAL_AMOUNT.getCode(), FieldConfig.builder().businessType(BusinessType.SALARY_PAY.getCode()).fieldName(SalaryPayField.TOTAL_AMOUNT.getCode()).fieldLabel(SalaryPayField.TOTAL_AMOUNT.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
        INIT_MAP.put(SalaryPayField.TOTAL_DEDUCTIONS.getCode(), FieldConfig.builder().businessType(BusinessType.SALARY_PAY.getCode()).fieldName(SalaryPayField.TOTAL_DEDUCTIONS.getCode()).fieldLabel(SalaryPayField.TOTAL_DEDUCTIONS.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
        INIT_MAP.put(SalaryPayField.PAY_AMOUNT.getCode(), FieldConfig.builder().businessType(BusinessType.SALARY_PAY.getCode()).fieldName(SalaryPayField.PAY_AMOUNT.getCode()).fieldLabel(SalaryPayField.PAY_AMOUNT.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.SALARY_PAY;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}