package net.qixiaowei.operate.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.EmpSalaryAdjustPlanField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.operate.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.operate.cloud.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description 指标-字段配置实现类
 * @Author hzk
 * @Date 2023-02-09 19:55
 **/
@Service
@Slf4j
public class EmpSalaryAdjustPlanFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(EmpSalaryAdjustPlanField.EMPLOYEE_ID.getCode(), FieldConfig.builder().businessType(BusinessType.EMP_SALARY_ADJUST_PLAN.getCode()).fieldName(EmpSalaryAdjustPlanField.EMPLOYEE_ID.getCode()).fieldLabel(EmpSalaryAdjustPlanField.EMPLOYEE_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.EMPLOYEE_CODE.getCode(), FieldConfig.builder().businessType(BusinessType.EMP_SALARY_ADJUST_PLAN.getCode()).fieldName(EmpSalaryAdjustPlanField.EMPLOYEE_CODE.getCode()).fieldLabel(EmpSalaryAdjustPlanField.EMPLOYEE_CODE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.EMPLOYMENT_DATE.getCode(), FieldConfig.builder().businessType(BusinessType.EMP_SALARY_ADJUST_PLAN.getCode()).fieldName(EmpSalaryAdjustPlanField.EMPLOYMENT_DATE.getCode()).fieldLabel(EmpSalaryAdjustPlanField.EMPLOYMENT_DATE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.SENIORITY.getCode(), FieldConfig.builder().businessType(BusinessType.EMP_SALARY_ADJUST_PLAN.getCode()).fieldName(EmpSalaryAdjustPlanField.SENIORITY.getCode()).fieldLabel(EmpSalaryAdjustPlanField.SENIORITY.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.DEPARTMENT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.EMP_SALARY_ADJUST_PLAN.getCode()).fieldName(EmpSalaryAdjustPlanField.DEPARTMENT_ID.getCode()).fieldLabel(EmpSalaryAdjustPlanField.DEPARTMENT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.DEPARTMENT_LEADER_ID.getCode(), FieldConfig.builder().businessType(BusinessType.EMP_SALARY_ADJUST_PLAN.getCode()).fieldName(EmpSalaryAdjustPlanField.DEPARTMENT_LEADER_ID.getCode()).fieldLabel(EmpSalaryAdjustPlanField.DEPARTMENT_LEADER_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.ADJUST_POST_ID.getCode(), FieldConfig.builder().businessType(BusinessType.EMP_SALARY_ADJUST_PLAN.getCode()).fieldName(EmpSalaryAdjustPlanField.ADJUST_POST_ID.getCode()).fieldLabel(EmpSalaryAdjustPlanField.ADJUST_POST_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.ADJUST_OFFICIAL_RANK_SYSTEM_ID.getCode(), FieldConfig.builder().businessType(BusinessType.EMP_SALARY_ADJUST_PLAN.getCode()).fieldName(EmpSalaryAdjustPlanField.ADJUST_OFFICIAL_RANK_SYSTEM_ID.getCode()).fieldLabel(EmpSalaryAdjustPlanField.ADJUST_OFFICIAL_RANK_SYSTEM_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.ADJUST_OFFICIAL_RANK.getCode(), FieldConfig.builder().businessType(BusinessType.EMP_SALARY_ADJUST_PLAN.getCode()).fieldName(EmpSalaryAdjustPlanField.ADJUST_OFFICIAL_RANK.getCode()).fieldLabel(EmpSalaryAdjustPlanField.ADJUST_OFFICIAL_RANK.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.ADJUST_EMOLUMENT.getCode(), FieldConfig.builder().businessType(BusinessType.EMP_SALARY_ADJUST_PLAN.getCode()).fieldName(EmpSalaryAdjustPlanField.ADJUST_EMOLUMENT.getCode()).fieldLabel(EmpSalaryAdjustPlanField.ADJUST_EMOLUMENT.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.EFFECTIVE_DATE.getCode(), FieldConfig.builder().businessType(BusinessType.EMP_SALARY_ADJUST_PLAN.getCode()).fieldName(EmpSalaryAdjustPlanField.EFFECTIVE_DATE.getCode()).fieldLabel(EmpSalaryAdjustPlanField.EFFECTIVE_DATE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.EMP_SALARY_ADJUST_PLAN;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}