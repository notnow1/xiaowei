package net.qixiaowei.strategy.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.strategy.StrategyMetricsField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.strategy.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.strategy.cloud.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 战略衡量指标表-字段配置实现类
 *
 * @author Graves
 * @since 2023-03-07
 */
@Service
@Slf4j
public class StrategyMetricsFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(StrategyMetricsField.PLAN_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.STRATEGY_METRICS.getCode()).fieldName(StrategyMetricsField.PLAN_YEAR.getCode()).fieldLabel(StrategyMetricsField.PLAN_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(StrategyMetricsField.BUSINESS_UNIT_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.STRATEGY_METRICS.getCode()).fieldName(StrategyMetricsField.BUSINESS_UNIT_NAME.getCode()).fieldLabel(StrategyMetricsField.BUSINESS_UNIT_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(StrategyMetricsField.AREA_ID.getCode(), FieldConfig.builder().businessType(BusinessType.STRATEGY_METRICS.getCode()).fieldName(StrategyMetricsField.AREA_ID.getCode()).fieldLabel(StrategyMetricsField.AREA_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(StrategyMetricsField.DEPARTMENT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.STRATEGY_METRICS.getCode()).fieldName(StrategyMetricsField.DEPARTMENT_ID.getCode()).fieldLabel(StrategyMetricsField.DEPARTMENT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(StrategyMetricsField.PRODUCT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.STRATEGY_METRICS.getCode()).fieldName(StrategyMetricsField.PRODUCT_ID.getCode()).fieldLabel(StrategyMetricsField.PRODUCT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(StrategyMetricsField.INDUSTRY_ID.getCode(), FieldConfig.builder().businessType(BusinessType.STRATEGY_METRICS.getCode()).fieldName(StrategyMetricsField.INDUSTRY_ID.getCode()).fieldLabel(StrategyMetricsField.INDUSTRY_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
    }

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.STRATEGY_METRICS;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}