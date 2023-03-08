package net.qixiaowei.strategy.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.strategy.StrategyMeasureField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.strategy.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.strategy.cloud.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 战略举措清单表-字段配置实现类
 *
 * @author Graves
 * @since 2023-03-07
 */
@Service
@Slf4j
public class StrategyMeasureFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(StrategyMeasureField.PLAN_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.STRATEGY_MEASURE.getCode()).fieldName(StrategyMeasureField.PLAN_YEAR.getCode()).fieldLabel(StrategyMeasureField.PLAN_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(StrategyMeasureField.BUSINESS_UNIT_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.STRATEGY_MEASURE.getCode()).fieldName(StrategyMeasureField.BUSINESS_UNIT_NAME.getCode()).fieldLabel(StrategyMeasureField.BUSINESS_UNIT_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(StrategyMeasureField.AREA_ID.getCode(), FieldConfig.builder().businessType(BusinessType.STRATEGY_MEASURE.getCode()).fieldName(StrategyMeasureField.AREA_ID.getCode()).fieldLabel(StrategyMeasureField.AREA_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(StrategyMeasureField.DEPARTMENT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.STRATEGY_MEASURE.getCode()).fieldName(StrategyMeasureField.DEPARTMENT_ID.getCode()).fieldLabel(StrategyMeasureField.DEPARTMENT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(StrategyMeasureField.PRODUCT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.STRATEGY_MEASURE.getCode()).fieldName(StrategyMeasureField.PRODUCT_ID.getCode()).fieldLabel(StrategyMeasureField.PRODUCT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(StrategyMeasureField.INDUSTRY_ID.getCode(), FieldConfig.builder().businessType(BusinessType.STRATEGY_MEASURE.getCode()).fieldName(StrategyMeasureField.INDUSTRY_ID.getCode()).fieldLabel(StrategyMeasureField.INDUSTRY_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
    }

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.STRATEGY_MEASURE;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}