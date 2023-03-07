package net.qixiaowei.strategy.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.BonusBudgetField;
import net.qixiaowei.integration.common.enums.field.strategy.MarketInsightCustomerField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.strategy.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.strategy.cloud.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 市场洞察客户表-字段配置实现类
 *
 * @author TANGMICHI
 * @since 2023-03-07
 */
@Service
@Slf4j
public class MarketInsightCustomerFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(MarketInsightCustomerField.PLAN_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_MACRO.getCode()).fieldName(MarketInsightCustomerField.PLAN_YEAR.getCode()).fieldLabel(MarketInsightCustomerField.PLAN_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightCustomerField.PLAN_BUSINESS_UNIT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_MACRO.getCode()).fieldName(MarketInsightCustomerField.PLAN_BUSINESS_UNIT_ID.getCode()).fieldLabel(MarketInsightCustomerField.PLAN_BUSINESS_UNIT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightCustomerField.AREA_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_MACRO.getCode()).fieldName(MarketInsightCustomerField.AREA_ID.getCode()).fieldLabel(MarketInsightCustomerField.AREA_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightCustomerField.DEPARTMENT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_MACRO.getCode()).fieldName(MarketInsightCustomerField.DEPARTMENT_ID.getCode()).fieldLabel(MarketInsightCustomerField.DEPARTMENT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightCustomerField.PRODUCT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_MACRO.getCode()).fieldName(MarketInsightCustomerField.PRODUCT_ID.getCode()).fieldLabel(MarketInsightCustomerField.PRODUCT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightCustomerField.INDUSTRY_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_MACRO.getCode()).fieldName(MarketInsightCustomerField.INDUSTRY_ID.getCode()).fieldLabel(MarketInsightCustomerField.INDUSTRY_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());

    }

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.MARKET_INSIGHT_CUSTOMER;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}