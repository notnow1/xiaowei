package net.qixiaowei.system.manage.logic.impl.field.list;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.BaseField;
import net.qixiaowei.integration.common.enums.field.system.DepartmentField;
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
 * @description 部门-字段列表配置实现类
 * @Author hzk
 * @Date 2023-02-09 19:55
 **/
@Service
@Slf4j
public class DepartmentFieldListConfigImpl implements IFieldListConfigStrategy {

    private static final Map<String, FieldListConfig> INIT_DEPARTMENT = new HashMap<>();

    static {
        INIT_DEPARTMENT.put(DepartmentField.DEPARTMENT_NAME.getCode(), FieldListConfig.builder().fieldWidth(200).sort(1).showFlag(1).fixationFlag(1).showForce(1).fixationForce(1).build());
        INIT_DEPARTMENT.put(DepartmentField.DEPARTMENT_CODE.getCode(), FieldListConfig.builder().fieldWidth(120).sort(2).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_DEPARTMENT.put(DepartmentField.DEPARTMENT_IMPORTANCE_FACTOR.getCode(), FieldListConfig.builder().fieldWidth(150).sort(3).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_DEPARTMENT.put(DepartmentField.LEVEL.getCode(), FieldListConfig.builder().fieldWidth(120).sort(4).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_DEPARTMENT.put(DepartmentField.PARENT_DEPARTMENT_ID.getCode(), FieldListConfig.builder().fieldWidth(120).sort(5).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_DEPARTMENT.put(DepartmentField.DEPARTMENT_LEADER_ID.getCode(), FieldListConfig.builder().fieldWidth(130).sort(6).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_DEPARTMENT.put(DepartmentField.DEPARTMENT_LEADER_POST_ID.getCode(), FieldListConfig.builder().fieldWidth(140).sort(7).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_DEPARTMENT.put(DepartmentField.EXAMINATION_LEADER_ID.getCode(), FieldListConfig.builder().fieldWidth(130).sort(8).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_DEPARTMENT.put(DepartmentField.STATUS.getCode(), FieldListConfig.builder().fieldWidth(120).sort(9).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.DEPARTMENT;
    }

    @Override
    public int getInitSize() {
        return INIT_DEPARTMENT.size();
    }

    @Override
    public List<FieldListConfig> initUserFieldListConfig(List<FieldConfigDTO> fieldConfigs) {
        if (StringUtils.isEmpty(fieldConfigs)) {
            return null;
        }
        List<FieldListConfig> fieldListConfigs = new ArrayList<>();
        int sort = INIT_DEPARTMENT.size() + 1;
        for (FieldConfigDTO fieldConfig : fieldConfigs) {
            Long fieldConfigId = fieldConfig.getFieldConfigId();
            String fieldName = fieldConfig.getFieldName();
            FieldListConfig fieldListConfig;
            if (INIT_DEPARTMENT.containsKey(fieldName)) {
                fieldListConfig = INIT_DEPARTMENT.get(fieldName);
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