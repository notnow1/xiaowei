package net.qixiaowei.system.manage.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.system.IndicatorField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.system.manage.api.domain.field.FieldConfig;
import net.qixiaowei.system.manage.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description 指标-字段配置实现类
 * @Author hzk
 * @Date 2023-02-09 19:55
 **/
@Service
@Slf4j
public class IndicatorFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_INDICATOR = new HashMap<>();

    static {
        INIT_INDICATOR.put(IndicatorField.INDICATOR_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.INDICATOR.getCode()).fieldName(IndicatorField.INDICATOR_NAME.getCode()).fieldLabel(IndicatorField.INDICATOR_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_INDICATOR.put(IndicatorField.INDICATOR_CODE.getCode(), FieldConfig.builder().businessType(BusinessType.INDICATOR.getCode()).fieldName(IndicatorField.INDICATOR_CODE.getCode()).fieldLabel(IndicatorField.INDICATOR_CODE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_INDICATOR.put(IndicatorField.INDICATOR_TYPE.getCode(), FieldConfig.builder().businessType(BusinessType.INDICATOR.getCode()).fieldName(IndicatorField.INDICATOR_TYPE.getCode()).fieldLabel(IndicatorField.INDICATOR_TYPE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_INDICATOR.put(IndicatorField.INDICATOR_VALUE_TYPE.getCode(), FieldConfig.builder().businessType(BusinessType.INDICATOR.getCode()).fieldName(IndicatorField.INDICATOR_VALUE_TYPE.getCode()).fieldLabel(IndicatorField.INDICATOR_VALUE_TYPE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_INDICATOR.put(IndicatorField.CHOICE_FLAG.getCode(), FieldConfig.builder().businessType(BusinessType.INDICATOR.getCode()).fieldName(IndicatorField.CHOICE_FLAG.getCode()).fieldLabel(IndicatorField.CHOICE_FLAG.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_INDICATOR.put(IndicatorField.EXAMINE_DIRECTION.getCode(), FieldConfig.builder().businessType(BusinessType.INDICATOR.getCode()).fieldName(IndicatorField.EXAMINE_DIRECTION.getCode()).fieldLabel(IndicatorField.EXAMINE_DIRECTION.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_INDICATOR.put(IndicatorField.PARENT_INDICATOR_ID.getCode(), FieldConfig.builder().businessType(BusinessType.INDICATOR.getCode()).fieldName(IndicatorField.PARENT_INDICATOR_ID.getCode()).fieldLabel(IndicatorField.PARENT_INDICATOR_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_INDICATOR.put(IndicatorField.LEVEL.getCode(), FieldConfig.builder().businessType(BusinessType.INDICATOR.getCode()).fieldName(IndicatorField.LEVEL.getCode()).fieldLabel(IndicatorField.LEVEL.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_INDICATOR.put(IndicatorField.DRIVING_FACTOR_FLAG.getCode(), FieldConfig.builder().businessType(BusinessType.INDICATOR.getCode()).fieldName(IndicatorField.DRIVING_FACTOR_FLAG.getCode()).fieldLabel(IndicatorField.DRIVING_FACTOR_FLAG.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.INDICATOR;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_INDICATOR.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}