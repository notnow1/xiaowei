package net.qixiaowei.system.manage.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.system.DepartmentField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.system.manage.api.domain.field.FieldConfig;
import net.qixiaowei.system.manage.logic.IFieldConfigStrategy;
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
public class DepartmentFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_DEPARTMENT = new HashMap<>();

    static {
        INIT_DEPARTMENT.put(DepartmentField.DEPARTMENT_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.DEPARTMENT.getCode()).fieldName(DepartmentField.DEPARTMENT_NAME.getCode()).fieldLabel(DepartmentField.DEPARTMENT_NAME.getInfo()).build());
        INIT_DEPARTMENT.put(DepartmentField.DEPARTMENT_CODE.getCode(), FieldConfig.builder().businessType(BusinessType.DEPARTMENT.getCode()).fieldName(DepartmentField.DEPARTMENT_CODE.getCode()).fieldLabel(DepartmentField.DEPARTMENT_CODE.getInfo()).build());
        INIT_DEPARTMENT.put(DepartmentField.PARENT_DEPARTMENT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.DEPARTMENT.getCode()).fieldName(DepartmentField.PARENT_DEPARTMENT_ID.getCode()).fieldLabel(DepartmentField.PARENT_DEPARTMENT_ID.getInfo()).build());
        INIT_DEPARTMENT.put(DepartmentField.LEVEL.getCode(), FieldConfig.builder().businessType(BusinessType.DEPARTMENT.getCode()).fieldName(DepartmentField.LEVEL.getCode()).fieldLabel(DepartmentField.LEVEL.getInfo()).build());
        INIT_DEPARTMENT.put(DepartmentField.DEPARTMENT_LEADER_ID.getCode(), FieldConfig.builder().businessType(BusinessType.DEPARTMENT.getCode()).fieldName(DepartmentField.DEPARTMENT_LEADER_ID.getCode()).fieldLabel(DepartmentField.DEPARTMENT_LEADER_ID.getInfo()).build());
        INIT_DEPARTMENT.put(DepartmentField.DEPARTMENT_LEADER_POST_ID.getCode(), FieldConfig.builder().businessType(BusinessType.DEPARTMENT.getCode()).fieldName(DepartmentField.DEPARTMENT_LEADER_POST_ID.getCode()).fieldLabel(DepartmentField.DEPARTMENT_LEADER_POST_ID.getInfo()).build());
        INIT_DEPARTMENT.put(DepartmentField.EXAMINATION_LEADER_ID.getCode(), FieldConfig.builder().businessType(BusinessType.DEPARTMENT.getCode()).fieldName(DepartmentField.EXAMINATION_LEADER_ID.getCode()).fieldLabel(DepartmentField.EXAMINATION_LEADER_ID.getInfo()).build());
        INIT_DEPARTMENT.put(DepartmentField.DEPARTMENT_IMPORTANCE_FACTOR.getCode(), FieldConfig.builder().businessType(BusinessType.DEPARTMENT.getCode()).fieldName(DepartmentField.DEPARTMENT_IMPORTANCE_FACTOR.getCode()).fieldLabel(DepartmentField.DEPARTMENT_IMPORTANCE_FACTOR.getInfo()).build());
        INIT_DEPARTMENT.put(DepartmentField.STATUS.getCode(), FieldConfig.builder().businessType(BusinessType.DEPARTMENT.getCode()).fieldName(DepartmentField.STATUS.getCode()).fieldLabel(DepartmentField.STATUS.getInfo()).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.DEPARTMENT;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_DEPARTMENT.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}