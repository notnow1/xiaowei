package net.qixiaowei.system.manage.logic.impl.field.list;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.system.IndicatorField;
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
 * @description 指标-字段列表配置实现类
 * @Author hzk
 * @Date 2023-02-09 19:55
 **/
@Service
@Slf4j
public class IndicatorFieldListConfigImpl implements IFieldListConfigStrategy {

    private static final Map<String, FieldListConfig> INIT_INDICATOR = new HashMap<>();

    static {
        INIT_INDICATOR.put(IndicatorField.INDICATOR_NAME.getCode(), FieldListConfig.builder().fieldWidth(120).sort(1).showFlag(1).fixationFlag(1).showForce(1).fixationForce(1).build());
        INIT_INDICATOR.put(IndicatorField.INDICATOR_CODE.getCode(), FieldListConfig.builder().fieldWidth(120).sort(2).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_INDICATOR.put(IndicatorField.INDICATOR_TYPE.getCode(), FieldListConfig.builder().fieldWidth(120).sort(3).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_INDICATOR.put(IndicatorField.INDICATOR_VALUE_TYPE.getCode(), FieldListConfig.builder().fieldWidth(130).sort(4).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_INDICATOR.put(IndicatorField.CHOICE_FLAG.getCode(), FieldListConfig.builder().fieldWidth(120).sort(5).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_INDICATOR.put(IndicatorField.EXAMINE_DIRECTION.getCode(), FieldListConfig.builder().fieldWidth(120).sort(6).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_INDICATOR.put(IndicatorField.PARENT_INDICATOR_ID.getCode(), FieldListConfig.builder().fieldWidth(120).sort(7).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_INDICATOR.put(IndicatorField.LEVEL.getCode(), FieldListConfig.builder().fieldWidth(120).sort(8).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
        INIT_INDICATOR.put(IndicatorField.DRIVING_FACTOR_FLAG.getCode(), FieldListConfig.builder().fieldWidth(130).sort(9).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.INDICATOR;
    }

    @Override
    public int getInitSize() {
        return INIT_INDICATOR.size();
    }


    @Override
    public List<FieldListConfig> initUserFieldListConfig(List<FieldConfigDTO> fieldConfigs) {
        if (StringUtils.isEmpty(fieldConfigs)) {
            return null;
        }
        List<FieldListConfig> fieldListConfigs = new ArrayList<>();
        int sort = INIT_INDICATOR.size() + 1;
        for (FieldConfigDTO fieldConfig : fieldConfigs) {
            Long fieldConfigId = fieldConfig.getFieldConfigId();
            String fieldName = fieldConfig.getFieldName();
            FieldListConfig fieldListConfig;
            if (INIT_INDICATOR.containsKey(fieldName)) {
                fieldListConfig = INIT_INDICATOR.get(fieldName);
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