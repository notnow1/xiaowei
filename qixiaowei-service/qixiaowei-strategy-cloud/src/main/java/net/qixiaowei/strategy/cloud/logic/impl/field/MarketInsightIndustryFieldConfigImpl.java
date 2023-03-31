package net.qixiaowei.strategy.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.BonusBudgetField;
import net.qixiaowei.integration.common.enums.field.strategy.MarketInsightIndustryField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.strategy.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.strategy.cloud.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 市场洞察行业表-字段配置实现类
 *
 * @author TANGMICHI
 * @since 2023-03-03
 */
@Service
@Slf4j
public class MarketInsightIndustryFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(MarketInsightIndustryField.PLAN_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_INDUSTRY.getCode()).fieldName(MarketInsightIndustryField.PLAN_YEAR.getCode()).fieldLabel(MarketInsightIndustryField.PLAN_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightIndustryField.PLAN_BUSINESS_UNIT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_INDUSTRY.getCode()).fieldName(MarketInsightIndustryField.PLAN_BUSINESS_UNIT_ID.getCode()).fieldLabel(MarketInsightIndustryField.PLAN_BUSINESS_UNIT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightIndustryField.AREA_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_INDUSTRY.getCode()).fieldName(MarketInsightIndustryField.AREA_ID.getCode()).fieldLabel(MarketInsightIndustryField.AREA_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightIndustryField.DEPARTMENT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_INDUSTRY.getCode()).fieldName(MarketInsightIndustryField.DEPARTMENT_ID.getCode()).fieldLabel(MarketInsightIndustryField.DEPARTMENT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightIndustryField.PRODUCT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_INDUSTRY.getCode()).fieldName(MarketInsightIndustryField.PRODUCT_ID.getCode()).fieldLabel(MarketInsightIndustryField.PRODUCT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightIndustryField.INDUSTRY_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_INDUSTRY.getCode()).fieldName(MarketInsightIndustryField.INDUSTRY_ID.getCode()).fieldLabel(MarketInsightIndustryField.INDUSTRY_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());

    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.MARKET_INSIGHT_INDUSTRY;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}