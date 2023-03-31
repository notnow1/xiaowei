package net.qixiaowei.strategy.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.BonusBudgetField;
import net.qixiaowei.integration.common.enums.field.strategy.MarketInsightOpponentField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.strategy.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.strategy.cloud.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 市场洞察对手表-字段配置实现类
 *
 * @author TANGMICHI
 * @since 2023-03-12
 */
@Service
@Slf4j
public class MarketInsightOpponentFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.MARKET_INSIGHT_OPPONENT;
    }

    static {
        INIT_MAP.put(MarketInsightOpponentField.PLAN_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_OPPONENT.getCode()).fieldName(MarketInsightOpponentField.PLAN_YEAR.getCode()).fieldLabel(MarketInsightOpponentField.PLAN_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightOpponentField.PLAN_BUSINESS_UNIT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_OPPONENT.getCode()).fieldName(MarketInsightOpponentField.PLAN_BUSINESS_UNIT_ID.getCode()).fieldLabel(MarketInsightOpponentField.PLAN_BUSINESS_UNIT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightOpponentField.AREA_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_OPPONENT.getCode()).fieldName(MarketInsightOpponentField.AREA_ID.getCode()).fieldLabel(MarketInsightOpponentField.AREA_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightOpponentField.DEPARTMENT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_OPPONENT.getCode()).fieldName(MarketInsightOpponentField.DEPARTMENT_ID.getCode()).fieldLabel(MarketInsightOpponentField.DEPARTMENT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightOpponentField.PRODUCT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_OPPONENT.getCode()).fieldName(MarketInsightOpponentField.PRODUCT_ID.getCode()).fieldLabel(MarketInsightOpponentField.PRODUCT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(MarketInsightOpponentField.INDUSTRY_ID.getCode(), FieldConfig.builder().businessType(BusinessType.MARKET_INSIGHT_OPPONENT.getCode()).fieldName(MarketInsightOpponentField.INDUSTRY_ID.getCode()).fieldLabel(MarketInsightOpponentField.INDUSTRY_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());

    }

    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}