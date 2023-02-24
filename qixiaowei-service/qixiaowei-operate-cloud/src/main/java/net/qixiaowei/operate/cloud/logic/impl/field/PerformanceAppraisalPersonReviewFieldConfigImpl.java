package net.qixiaowei.operate.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.PerformanceAppraisalPersonReviewField;
import net.qixiaowei.integration.common.enums.field.system.UserField;
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
public class PerformanceAppraisalPersonReviewFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.APPRAISAL_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_PERSON_REVIEW.getCode()).fieldName(PerformanceAppraisalPersonReviewField.APPRAISAL_YEAR.getCode()).fieldLabel(PerformanceAppraisalPersonReviewField.APPRAISAL_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.APPRAISAL_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_PERSON_REVIEW.getCode()).fieldName(PerformanceAppraisalPersonReviewField.APPRAISAL_NAME.getCode()).fieldLabel(PerformanceAppraisalPersonReviewField.APPRAISAL_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.CYCLE_TYPE.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_PERSON_REVIEW.getCode()).fieldName(PerformanceAppraisalPersonReviewField.CYCLE_TYPE.getCode()).fieldLabel(PerformanceAppraisalPersonReviewField.CYCLE_TYPE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.CYCLE_NUMBER.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_PERSON_REVIEW.getCode()).fieldName(PerformanceAppraisalPersonReviewField.CYCLE_NUMBER.getCode()).fieldLabel(PerformanceAppraisalPersonReviewField.CYCLE_NUMBER.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.APPRAISAL_OBJECT_CODE.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_PERSON_REVIEW.getCode()).fieldName(PerformanceAppraisalPersonReviewField.APPRAISAL_OBJECT_CODE.getCode()).fieldLabel(PerformanceAppraisalPersonReviewField.APPRAISAL_OBJECT_CODE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.APPRAISAL_OBJECT_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_PERSON_REVIEW.getCode()).fieldName(PerformanceAppraisalPersonReviewField.APPRAISAL_OBJECT_NAME.getCode()).fieldLabel(PerformanceAppraisalPersonReviewField.APPRAISAL_OBJECT_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.DEPARTMENT_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_PERSON_REVIEW.getCode()).fieldName(PerformanceAppraisalPersonReviewField.DEPARTMENT_NAME.getCode()).fieldLabel(PerformanceAppraisalPersonReviewField.DEPARTMENT_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.POST_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_PERSON_REVIEW.getCode()).fieldName(PerformanceAppraisalPersonReviewField.POST_NAME.getCode()).fieldLabel(PerformanceAppraisalPersonReviewField.POST_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.APPRAISAL_PRINCIPAL_ID.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_PERSON_REVIEW.getCode()).fieldName(PerformanceAppraisalPersonReviewField.APPRAISAL_PRINCIPAL_ID.getCode()).fieldLabel(PerformanceAppraisalPersonReviewField.APPRAISAL_PRINCIPAL_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.EVALUATION_SCORE.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_PERSON_REVIEW.getCode()).fieldName(PerformanceAppraisalPersonReviewField.EVALUATION_SCORE.getCode()).fieldLabel(PerformanceAppraisalPersonReviewField.EVALUATION_SCORE.getInfo()).fieldType(FieldType.PERCENTAGE.getCode()).build());
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.APPRAISAL_OBJECT_STATUS.getCode(), FieldConfig.builder().businessType(BusinessType.PERFORMANCE_APPRAISAL_PERSON_REVIEW.getCode()).fieldName(PerformanceAppraisalPersonReviewField.APPRAISAL_OBJECT_STATUS.getCode()).fieldLabel(PerformanceAppraisalPersonReviewField.APPRAISAL_OBJECT_STATUS.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.PERFORMANCE_APPRAISAL_PERSON_REVIEW;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}