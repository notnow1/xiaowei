package net.qixiaowei.operate.cloud.logic.impl.field.list;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.BaseField;
import net.qixiaowei.integration.common.enums.field.operate.PerformanceAppraisalPersonReviewField;
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
 * @description 个人绩效评议-字段列表配置实现类
 * @Author hzk
 * @Date 2023-02-09 19:55
 **/
@Service
@Slf4j
public class PerformanceAppraisalPersonReviewFieldListConfigImpl implements IFieldListConfigStrategy {

    private static final Map<String, FieldListConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.APPRAISAL_YEAR.getCode(), FieldListConfig.builder().fieldWidth(120).sort(1).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.APPRAISAL_NAME.getCode(), FieldListConfig.builder().fieldWidth(140).sort(2).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.CYCLE_TYPE.getCode(), FieldListConfig.builder().fieldWidth(120).sort(3).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.CYCLE_NUMBER.getCode(), FieldListConfig.builder().fieldWidth(120).sort(4).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.APPRAISAL_OBJECT_CODE.getCode(), FieldListConfig.builder().fieldWidth(140).sort(5).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.APPRAISAL_OBJECT_NAME.getCode(), FieldListConfig.builder().fieldWidth(140).sort(6).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.DEPARTMENT_NAME.getCode(), FieldListConfig.builder().fieldWidth(140).sort(7).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.POST_NAME.getCode(), FieldListConfig.builder().fieldWidth(140).sort(8).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.APPRAISAL_PRINCIPAL_ID.getCode(), FieldListConfig.builder().fieldWidth(130).sort(9).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.EVALUATION_SCORE.getCode(), FieldListConfig.builder().fieldWidth(120).sort(10).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(PerformanceAppraisalPersonReviewField.APPRAISAL_OBJECT_STATUS.getCode(), FieldListConfig.builder().fieldWidth(120).sort(11).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.PERFORMANCE_APPRAISAL_PERSON_REVIEW;
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