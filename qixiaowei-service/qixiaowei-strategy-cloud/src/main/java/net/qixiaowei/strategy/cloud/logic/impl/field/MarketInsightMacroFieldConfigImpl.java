package net.qixiaowei.strategy.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.strategy.MarketInsightMacroField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.strategy.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.strategy.cloud.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 市场洞察宏观表-字段配置实现类
 *
 * @author TANGMICHI
 * @since 2023-02-28
 */
@Service
@Slf4j
public class MarketInsightMacroFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(MarketInsightMacroField.PLAN_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_MACRO.getCode()).fieldName(MarketInsightMacroField.PLAN_YEAR.getCode()).fieldLabel(MarketInsightMacroField.PLAN_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightMacroField.PLAN_BUSINESS_UNIT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_MACRO.getCode()).fieldName(MarketInsightMacroField.PLAN_BUSINESS_UNIT_ID.getCode()).fieldLabel(MarketInsightMacroField.PLAN_BUSINESS_UNIT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightMacroField.AREA_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_MACRO.getCode()).fieldName(MarketInsightMacroField.AREA_ID.getCode()).fieldLabel(MarketInsightMacroField.AREA_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightMacroField.DEPARTMENT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_MACRO.getCode()).fieldName(MarketInsightMacroField.DEPARTMENT_ID.getCode()).fieldLabel(MarketInsightMacroField.DEPARTMENT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightMacroField.PRODUCT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_MACRO.getCode()).fieldName(MarketInsightMacroField.PRODUCT_ID.getCode()).fieldLabel(MarketInsightMacroField.PRODUCT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightMacroField.INDUSTRY_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_MACRO.getCode()).fieldName(MarketInsightMacroField.INDUSTRY_ID.getCode()).fieldLabel(MarketInsightMacroField.INDUSTRY_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());

    }
    @Override
    public BusinessType getBusinessType() {
        return BusinessType.MARKET_INSIGHT_MACRO;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}