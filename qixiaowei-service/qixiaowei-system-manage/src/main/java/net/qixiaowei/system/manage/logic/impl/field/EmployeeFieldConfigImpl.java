package net.qixiaowei.system.manage.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.system.EmployeeField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.system.manage.api.domain.field.FieldConfig;
import net.qixiaowei.system.manage.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description 人员-字段配置实现类
 * @Author hzk
 * @Date 2023-02-09 19:55
 **/
@Service
@Slf4j
public class EmployeeFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_EMPLOYEE = new HashMap<>();

    static {
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYEE_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE.getCode()).fieldName(EmployeeField.EMPLOYEE_NAME.getCode()).fieldLabel(EmployeeField.EMPLOYEE_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYEE_CODE.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE.getCode()).fieldName(EmployeeField.EMPLOYEE_CODE.getCode()).fieldLabel(EmployeeField.EMPLOYEE_CODE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYMENT_STATUS.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE.getCode()).fieldName(EmployeeField.EMPLOYMENT_STATUS.getCode()).fieldLabel(EmployeeField.EMPLOYMENT_STATUS.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYEE_GENDER.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE.getCode()).fieldName(EmployeeField.EMPLOYEE_GENDER.getCode()).fieldLabel(EmployeeField.EMPLOYEE_GENDER.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_EMPLOYEE.put(EmployeeField.IDENTITY_CARD.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE.getCode()).fieldName(EmployeeField.IDENTITY_CARD.getCode()).fieldLabel(EmployeeField.IDENTITY_CARD.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYEE_BIRTHDAY.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE.getCode()).fieldName(EmployeeField.EMPLOYEE_BIRTHDAY.getCode()).fieldLabel(EmployeeField.EMPLOYEE_BIRTHDAY.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_EMPLOYEE.put(EmployeeField.MARITAL_STATUS.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE.getCode()).fieldName(EmployeeField.MARITAL_STATUS.getCode()).fieldLabel(EmployeeField.MARITAL_STATUS.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_EMPLOYEE.put(EmployeeField.NATIONALITY.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE.getCode()).fieldName(EmployeeField.NATIONALITY.getCode()).fieldLabel(EmployeeField.NATIONALITY.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_EMPLOYEE.put(EmployeeField.NATION.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE.getCode()).fieldName(EmployeeField.NATION.getCode()).fieldLabel(EmployeeField.NATION.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYMENT_DATE.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE.getCode()).fieldName(EmployeeField.EMPLOYMENT_DATE.getCode()).fieldLabel(EmployeeField.EMPLOYMENT_DATE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_EMPLOYEE.put(EmployeeField.STATUS.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE.getCode()).fieldName(EmployeeField.STATUS.getCode()).fieldLabel(EmployeeField.STATUS.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYEE_DEPARTMENT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE.getCode()).fieldName(EmployeeField.EMPLOYEE_DEPARTMENT_ID.getCode()).fieldLabel(EmployeeField.EMPLOYEE_DEPARTMENT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYEE_POST_ID.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE.getCode()).fieldName(EmployeeField.EMPLOYEE_POST_ID.getCode()).fieldLabel(EmployeeField.EMPLOYEE_POST_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_EMPLOYEE.put(EmployeeField.OFFICIAL_RANK_SYSTEM_ID.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE.getCode()).fieldName(EmployeeField.OFFICIAL_RANK_SYSTEM_ID.getCode()).fieldLabel(EmployeeField.OFFICIAL_RANK_SYSTEM_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_EMPLOYEE.put(EmployeeField.POST_RANK_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE.getCode()).fieldName(EmployeeField.POST_RANK_NAME.getCode()).fieldLabel(EmployeeField.POST_RANK_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYEE_RANK.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE.getCode()).fieldName(EmployeeField.EMPLOYEE_RANK.getCode()).fieldLabel(EmployeeField.EMPLOYEE_RANK.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYEE_BASIC_WAGE.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE.getCode()).fieldName(EmployeeField.EMPLOYEE_BASIC_WAGE.getCode()).fieldLabel(EmployeeField.EMPLOYEE_BASIC_WAGE.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYEE_MOBILE.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE.getCode()).fieldName(EmployeeField.EMPLOYEE_MOBILE.getCode()).fieldLabel(EmployeeField.EMPLOYEE_MOBILE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYEE_EMAIL.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE.getCode()).fieldName(EmployeeField.EMPLOYEE_EMAIL.getCode()).fieldLabel(EmployeeField.EMPLOYEE_EMAIL.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_EMPLOYEE.put(EmployeeField.WECHAT_CODE.getCode(), FieldConfig.builder().businessType(BusinessType.EMPLOYEE.getCode()).fieldName(EmployeeField.WECHAT_CODE.getCode()).fieldLabel(EmployeeField.WECHAT_CODE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
    }

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.EMPLOYEE;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_EMPLOYEE.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}