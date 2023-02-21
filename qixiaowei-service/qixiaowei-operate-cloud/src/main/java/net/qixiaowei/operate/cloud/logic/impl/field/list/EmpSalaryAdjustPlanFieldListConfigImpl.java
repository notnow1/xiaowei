package net.qixiaowei.operate.cloud.logic.impl.field.list;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.operate.EmpSalaryAdjustPlanField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.operate.cloud.api.domain.field.FieldListConfig;
import net.qixiaowei.operate.cloud.api.dto.field.FieldConfigDTO;
import net.qixiaowei.operate.cloud.logic.IFieldListConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description 个人调薪计划-字段列表配置实现类
 * @Author hzk
 * @Date 2023-02-09 19:55
 **/
@Service
@Slf4j
public class EmpSalaryAdjustPlanFieldListConfigImpl implements IFieldListConfigStrategy {

    private static final Map<String, FieldListConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(EmpSalaryAdjustPlanField.EMPLOYEE_ID.getCode(), FieldListConfig.builder().fieldWidth(120).sort(1).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.EMPLOYEE_CODE.getCode(), FieldListConfig.builder().fieldWidth(120).sort(2).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.EMPLOYMENT_DATE.getCode(), FieldListConfig.builder().fieldWidth(120).sort(3).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.SENIORITY.getCode(), FieldListConfig.builder().fieldWidth(120).sort(4).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.DEPARTMENT_ID.getCode(), FieldListConfig.builder().fieldWidth(120).sort(5).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.ADJUST_DEPARTMENT_ID.getCode(), FieldListConfig.builder().fieldWidth(130).sort(6).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
//        INIT_MAP.put(EmpSalaryAdjustPlanField.DEPARTMENT_LEADER_ID.getCode(), FieldListConfig.builder().fieldWidth(120).sort(6).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.POST_ID.getCode(), FieldListConfig.builder().fieldWidth(120).sort(7).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.ADJUST_POST_ID.getCode(), FieldListConfig.builder().fieldWidth(130).sort(8).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.OFFICIAL_RANK_SYSTEM_ID.getCode(), FieldListConfig.builder().fieldWidth(130).sort(9).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.ADJUST_OFFICIAL_RANK_SYSTEM_ID.getCode(), FieldListConfig.builder().fieldWidth(150).sort(10).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.OFFICIAL_RANK.getCode(), FieldListConfig.builder().fieldWidth(120).sort(11).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.ADJUST_OFFICIAL_RANK.getCode(), FieldListConfig.builder().fieldWidth(120).sort(12).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.BASIC_WAGE.getCode(), FieldListConfig.builder().fieldWidth(130).sort(13).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.ADJUST_EMOLUMENT.getCode(), FieldListConfig.builder().fieldWidth(150).sort(14).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(EmpSalaryAdjustPlanField.EFFECTIVE_DATE.getCode(), FieldListConfig.builder().fieldWidth(120).sort(15).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.EMP_SALARY_ADJUST_PLAN;
    }

    @Override
    public int getInitSize() {
        return INIT_MAP.size();
    }

    @Override
    public List<FieldListConfig> initUserFieldListConfig(List<FieldConfigDTO> fieldConfigs) {
        if (StringUtils.isEmpty(fieldConfigs)) {
            return null;
        }
        List<FieldListConfig> fieldListConfigs = new ArrayList<>();
        int sort = INIT_MAP.size() + 1;
        for (FieldConfigDTO fieldConfig : fieldConfigs) {
            Long fieldConfigId = fieldConfig.getFieldConfigId();
            String fieldName = fieldConfig.getFieldName();
            FieldListConfig fieldListConfig;
            if (INIT_MAP.containsKey(fieldName)) {
                fieldListConfig = INIT_MAP.get(fieldName);
                fieldListConfig.setFieldConfigId(fieldConfigId);
            } else {
                fieldListConfig = FieldListConfig.builder().fieldConfigId(fieldConfigId).fieldWidth(200).sort(sort).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build();
                sort++;
            }
            fieldListConfigs.add(fieldListConfig);
        }
        return fieldListConfigs;
    }

}