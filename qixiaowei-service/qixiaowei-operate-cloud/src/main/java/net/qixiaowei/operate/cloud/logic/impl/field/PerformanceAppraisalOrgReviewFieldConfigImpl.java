package net.qixiaowei.operate.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.PerformanceAppraisalOrgReviewField;
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
public class PerformanceAppraisalOrgReviewFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(PerformanceAppraisalOrgReviewField.APPRAISAL_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_ORG_REVIEW.getCode()).fieldName(PerformanceAppraisalOrgReviewField.APPRAISAL_YEAR.getCode()).fieldLabel(PerformanceAppraisalOrgReviewField.APPRAISAL_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalOrgReviewField.APPRAISAL_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_ORG_REVIEW.getCode()).fieldName(PerformanceAppraisalOrgReviewField.APPRAISAL_NAME.getCode()).fieldLabel(PerformanceAppraisalOrgReviewField.APPRAISAL_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalOrgReviewField.CYCLE_TYPE.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_ORG_REVIEW.getCode()).fieldName(PerformanceAppraisalOrgReviewField.CYCLE_TYPE.getCode()).fieldLabel(PerformanceAppraisalOrgReviewField.CYCLE_TYPE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalOrgReviewField.CYCLE_NUMBER.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_ORG_REVIEW.getCode()).fieldName(PerformanceAppraisalOrgReviewField.CYCLE_NUMBER.getCode()).fieldLabel(PerformanceAppraisalOrgReviewField.CYCLE_NUMBER.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalOrgReviewField.EVALUATION_TYPE.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_ORG_REVIEW.getCode()).fieldName(PerformanceAppraisalOrgReviewField.EVALUATION_TYPE.getCode()).fieldLabel(PerformanceAppraisalOrgReviewField.EVALUATION_TYPE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalOrgReviewField.APPRAISAL_OBJECT_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_ORG_REVIEW.getCode()).fieldName(PerformanceAppraisalOrgReviewField.APPRAISAL_OBJECT_NAME.getCode()).fieldLabel(PerformanceAppraisalOrgReviewField.APPRAISAL_OBJECT_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalOrgReviewField.APPRAISAL_PRINCIPAL_ID.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_ORG_REVIEW.getCode()).fieldName(PerformanceAppraisalOrgReviewField.APPRAISAL_PRINCIPAL_ID.getCode()).fieldLabel(PerformanceAppraisalOrgReviewField.APPRAISAL_PRINCIPAL_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalOrgReviewField.EVALUATION_SCORE.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_ORG_REVIEW.getCode()).fieldName(PerformanceAppraisalOrgReviewField.EVALUATION_SCORE.getCode()).fieldLabel(PerformanceAppraisalOrgReviewField.EVALUATION_SCORE.getInfo()).fieldType(FieldType.PERCENTAGE.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalOrgReviewField.APPRAISAL_OBJECT_STATUS.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_ORG_REVIEW.getCode()).fieldName(PerformanceAppraisalOrgReviewField.APPRAISAL_OBJECT_STATUS.getCode()).fieldLabel(PerformanceAppraisalOrgReviewField.APPRAISAL_OBJECT_STATUS.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.PERFORMANCE_APPRAISAL_ORG_REVIEW;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}