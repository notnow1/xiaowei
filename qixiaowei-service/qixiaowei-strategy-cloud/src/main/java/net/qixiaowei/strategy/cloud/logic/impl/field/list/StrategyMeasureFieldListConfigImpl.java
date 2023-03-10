package net.qixiaowei.strategy.cloud.logic.impl.field.list;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.strategy.GapAnalysisField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.strategy.cloud.api.domain.field.FieldListConfig;
import net.qixiaowei.strategy.cloud.api.dto.field.FieldConfigDTO;
import net.qixiaowei.strategy.cloud.logic.IFieldListConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description 总奖金包预算-字段列表配置实现类
 * @Author hzk
 * @Date 2023-02-09 19:55
 **/
@Service
@Slf4j
public class StrategyMeasureFieldListConfigImpl implements IFieldListConfigStrategy {

    private static final Map<String, FieldListConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(GapAnalysisField.PLAN_YEAR.getCode(), FieldListConfig.builder().fieldWidth(120).sort(1).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(GapAnalysisField.BUSINESS_UNIT_NAME.getCode(), FieldListConfig.builder().fieldWidth(160).sort(2).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(GapAnalysisField.INDUSTRY_ID.getCode(), FieldListConfig.builder().fieldWidth(120).sort(3).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(GapAnalysisField.AREA_ID.getCode(), FieldListConfig.builder().fieldWidth(120).sort(4).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(GapAnalysisField.DEPARTMENT_ID.getCode(), FieldListConfig.builder().fieldWidth(120).sort(5).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_MAP.put(GapAnalysisField.PRODUCT_ID.getCode(), FieldListConfig.builder().fieldWidth(120).sort(6).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
    }

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.STRATEGY_MEASURE;
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