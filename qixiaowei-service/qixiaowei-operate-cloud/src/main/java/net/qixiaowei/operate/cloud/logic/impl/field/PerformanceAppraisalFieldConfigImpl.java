package net.qixiaowei.operate.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.PerformanceAppraisalField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.operate.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.operate.cloud.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description 绩效考核任务-字段配置实现类
 * @Author hzk
 * @Date 2023-02-09 19:55
 **/
@Service
@Slf4j
public class PerformanceAppraisalFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(PerformanceAppraisalField.APPRAISAL_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL.getCode()).fieldName(PerformanceAppraisalField.APPRAISAL_NAME.getCode()).fieldLabel(PerformanceAppraisalField.APPRAISAL_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalField.APPRAISAL_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL.getCode()).fieldName(PerformanceAppraisalField.APPRAISAL_YEAR.getCode()).fieldLabel(PerformanceAppraisalField.APPRAISAL_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalField.APPRAISAL_OBJECT.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL.getCode()).fieldName(PerformanceAppraisalField.APPRAISAL_OBJECT.getCode()).fieldLabel(PerformanceAppraisalField.APPRAISAL_OBJECT.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalField.CYCLE_FLAG.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL.getCode()).fieldName(PerformanceAppraisalField.CYCLE_FLAG.getCode()).fieldLabel(PerformanceAppraisalField.CYCLE_FLAG.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalField.CYCLE_TYPE.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL.getCode()).fieldName(PerformanceAppraisalField.CYCLE_TYPE.getCode()).fieldLabel(PerformanceAppraisalField.CYCLE_TYPE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalField.CYCLE_NUMBER.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL.getCode()).fieldName(PerformanceAppraisalField.CYCLE_NUMBER.getCode()).fieldLabel(PerformanceAppraisalField.CYCLE_NUMBER.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalField.APPRAISAL_START_DATE.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL.getCode()).fieldName(PerformanceAppraisalField.APPRAISAL_START_DATE.getCode()).fieldLabel(PerformanceAppraisalField.APPRAISAL_START_DATE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalField.APPRAISAL_END_DATE.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL.getCode()).fieldName(PerformanceAppraisalField.APPRAISAL_END_DATE.getCode()).fieldLabel(PerformanceAppraisalField.APPRAISAL_END_DATE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalField.PERFORMANCE_RANK_ID.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL.getCode()).fieldName(PerformanceAppraisalField.PERFORMANCE_RANK_ID.getCode()).fieldLabel(PerformanceAppraisalField.PERFORMANCE_RANK_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalField.APPRAISAL_FLOW.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL.getCode()).fieldName(PerformanceAppraisalField.APPRAISAL_FLOW.getCode()).fieldLabel(PerformanceAppraisalField.APPRAISAL_FLOW.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalField.APPRAISAL_STATUS.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL.getCode()).fieldName(PerformanceAppraisalField.APPRAISAL_STATUS.getCode()).fieldLabel(PerformanceAppraisalField.APPRAISAL_STATUS.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.PERFORMANCE_APPRAISAL;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}