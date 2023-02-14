package net.qixiaowei.operate.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.PerformanceAppraisalOrgSettingField;
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
public class PerformanceAppraisalOrgSettingFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(PerformanceAppraisalOrgSettingField.APPRAISAL_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_ORG_SETTING.getCode()).fieldName(PerformanceAppraisalOrgSettingField.APPRAISAL_NAME.getCode()).fieldLabel(PerformanceAppraisalOrgSettingField.APPRAISAL_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalOrgSettingField.APPRAISAL_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_ORG_SETTING.getCode()).fieldName(PerformanceAppraisalOrgSettingField.APPRAISAL_YEAR.getCode()).fieldLabel(PerformanceAppraisalOrgSettingField.APPRAISAL_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalOrgSettingField.CYCLE_TYPE.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_ORG_SETTING.getCode()).fieldName(PerformanceAppraisalOrgSettingField.CYCLE_TYPE.getCode()).fieldLabel(PerformanceAppraisalOrgSettingField.CYCLE_TYPE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalOrgSettingField.CYCLE_NUMBER.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_ORG_SETTING.getCode()).fieldName(PerformanceAppraisalOrgSettingField.CYCLE_NUMBER.getCode()).fieldLabel(PerformanceAppraisalOrgSettingField.CYCLE_NUMBER.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalOrgSettingField.APPRAISAL_OBJECT_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_ORG_SETTING.getCode()).fieldName(PerformanceAppraisalOrgSettingField.APPRAISAL_OBJECT_NAME.getCode()).fieldLabel(PerformanceAppraisalOrgSettingField.APPRAISAL_OBJECT_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalOrgSettingField.APPRAISAL_PRINCIPAL_ID.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_ORG_SETTING.getCode()).fieldName(PerformanceAppraisalOrgSettingField.APPRAISAL_PRINCIPAL_ID.getCode()).fieldLabel(PerformanceAppraisalOrgSettingField.APPRAISAL_PRINCIPAL_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalOrgSettingField.APPRAISAL_OBJECT_STATUS.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_ORG_SETTING.getCode()).fieldName(PerformanceAppraisalOrgSettingField.APPRAISAL_OBJECT_STATUS.getCode()).fieldLabel(PerformanceAppraisalOrgSettingField.APPRAISAL_OBJECT_STATUS.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.PERFORMANCE_APPRAISAL_ORG_SETTING;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}