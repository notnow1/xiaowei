package net.qixiaowei.strategy.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.BonusBudgetField;
import net.qixiaowei.integration.common.enums.field.strategy.MarketInsightSelfField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.strategy.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.strategy.cloud.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 市场洞察自身表-字段配置实现类
 *
 * @author TANGMICHI
 * @since 2023-03-13
 */
@Service
@Slf4j
public class MarketInsightSelfFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(MarketInsightSelfField.PLAN_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_SELF.getCode()).fieldName(MarketInsightSelfField.PLAN_YEAR.getCode()).fieldLabel(MarketInsightSelfField.PLAN_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightSelfField.PLAN_BUSINESS_UNIT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_SELF.getCode()).fieldName(MarketInsightSelfField.PLAN_BUSINESS_UNIT_ID.getCode()).fieldLabel(MarketInsightSelfField.PLAN_BUSINESS_UNIT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightSelfField.AREA_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_SELF.getCode()).fieldName(MarketInsightSelfField.AREA_ID.getCode()).fieldLabel(MarketInsightSelfField.AREA_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightSelfField.DEPARTMENT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_SELF.getCode()).fieldName(MarketInsightSelfField.DEPARTMENT_ID.getCode()).fieldLabel(MarketInsightSelfField.DEPARTMENT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightSelfField.PRODUCT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_SELF.getCode()).fieldName(MarketInsightSelfField.PRODUCT_ID.getCode()).fieldLabel(MarketInsightSelfField.PRODUCT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightSelfField.INDUSTRY_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_SELF.getCode()).fieldName(MarketInsightSelfField.INDUSTRY_ID.getCode()).fieldLabel(MarketInsightSelfField.INDUSTRY_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());

    }
    @Override
    public BusinessType getBusinessType() {
        return BusinessType.MARKET_INSIGHT_SELF;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}