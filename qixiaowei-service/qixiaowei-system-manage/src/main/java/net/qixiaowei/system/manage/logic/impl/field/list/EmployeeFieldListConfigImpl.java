package net.qixiaowei.system.manage.logic.impl.field.list;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.BaseField;
import net.qixiaowei.integration.common.enums.field.system.EmployeeField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.system.manage.api.domain.field.FieldListConfig;
import net.qixiaowei.system.manage.api.dto.field.FieldConfigDTO;
import net.qixiaowei.system.manage.logic.IFieldListConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description 人员-字段列表配置实现类
 * @Author hzk
 * @Date 2023-02-09 19:55
 **/
@Service
@Slf4j
public class EmployeeFieldListConfigImpl implements IFieldListConfigStrategy {

    private static final Map<String, FieldListConfig> INIT_EMPLOYEE = new HashMap<>();

    static {
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYEE_CODE.getCode(), FieldListConfig.builder().fieldWidth(120).sort(1).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYEE_NAME.getCode(), FieldListConfig.builder().fieldWidth(120).sort(2).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYEE_GENDER.getCode(), FieldListConfig.builder().fieldWidth(100).sort(3).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYEE_DEPARTMENT_ID.getCode(), FieldListConfig.builder().fieldWidth(120).sort(4).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYEE_POST_ID.getCode(), FieldListConfig.builder().fieldWidth(120).sort(5).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYMENT_STATUS.getCode(), FieldListConfig.builder().fieldWidth(130).sort(6).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYMENT_DATE.getCode(), FieldListConfig.builder().fieldWidth(120).sort(7).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_EMPLOYEE.put(EmployeeField.DEPARTURE_DATE.getCode(), FieldListConfig.builder().fieldWidth(120).sort(8).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_EMPLOYEE.put(EmployeeField.OFFICIAL_RANK_SYSTEM_ID.getCode(), FieldListConfig.builder().fieldWidth(9).sort(14).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_EMPLOYEE.put(EmployeeField.POST_RANK_NAME.getCode(), FieldListConfig.builder().fieldWidth(120).sort(10).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYEE_RANK.getCode(), FieldListConfig.builder().fieldWidth(120).sort(11).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYEE_BASIC_WAGE.getCode(), FieldListConfig.builder().fieldWidth(120).sort(12).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_EMPLOYEE.put(EmployeeField.NATIONALITY.getCode(), FieldListConfig.builder().fieldWidth(120).sort(13).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_EMPLOYEE.put(EmployeeField.NATION.getCode(), FieldListConfig.builder().fieldWidth(120).sort(14).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_EMPLOYEE.put(EmployeeField.IDENTITY_CARD.getCode(), FieldListConfig.builder().fieldWidth(185).sort(15).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYEE_BIRTHDAY.getCode(), FieldListConfig.builder().fieldWidth(120).sort(16).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_EMPLOYEE.put(EmployeeField.MARITAL_STATUS.getCode(), FieldListConfig.builder().fieldWidth(120).sort(17).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYEE_MOBILE.getCode(), FieldListConfig.builder().fieldWidth(125).sort(18).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_EMPLOYEE.put(EmployeeField.EMPLOYEE_EMAIL.getCode(), FieldListConfig.builder().fieldWidth(150).sort(19).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_EMPLOYEE.put(EmployeeField.WECHAT_CODE.getCode(), FieldListConfig.builder().fieldWidth(125).sort(20).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_EMPLOYEE.put(EmployeeField.STATUS.getCode(), FieldListConfig.builder().fieldWidth(120).sort(21).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.EMPLOYEE;
    }

    @Override
    public int getInitSize() {
        return INIT_EMPLOYEE.size();
    }


    @Override
    public List<FieldListConfig> initUserFieldListConfig(List<FieldConfigDTO> fieldConfigs) {
        if (StringUtils.isEmpty(fieldConfigs)) {
            return null;
        }
        List<FieldListConfig> fieldListConfigs = new ArrayList<>();
        int sort = INIT_EMPLOYEE.size() + 1;
        for (FieldConfigDTO fieldConfig : fieldConfigs) {
            Long fieldConfigId = fieldConfig.getFieldConfigId();
            String fieldName = fieldConfig.getFieldName();
            FieldListConfig fieldListConfig;
            if (INIT_EMPLOYEE.containsKey(fieldName)) {
                fieldListConfig = INIT_EMPLOYEE.get(fieldName);
                fieldListConfig.setFieldConfigId(fieldConfigId);
            } else {
                fieldListConfig = FieldListConfig.builder().fieldConfigId(fieldConfigId).fieldWidth(120).sort(sort).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build();
                if (BaseField.CREATE_TIME.getCode().equals(fieldName)) {
                    fieldListConfig.setFieldWidth(180);
                }
                sort++;
            }
            fieldListConfigs.add(fieldListConfig);
        }
        return fieldListConfigs;
    }

}