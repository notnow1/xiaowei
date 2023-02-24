package net.qixiaowei.strategy.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.strategy.StrategyIntentField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.strategy.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.strategy.cloud.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 战略意图表-字段配置实现类
 *
 * @author TANGMICHI
 * @since 2023-02-24
 */
@Service
@Slf4j
public class StrategyIntentFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(StrategyIntentField.PLAN_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.STRATEGY_INTENT.getCode()).fieldName(StrategyIntentField.PLAN_YEAR.getCode()).fieldLabel(StrategyIntentField.PLAN_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(StrategyIntentField.OPERATE_PLAN_PERIOD.getCode(), FieldConfig.builder().businessType(BusinessType.STRATEGY_INTENT.getCode()).fieldName(StrategyIntentField.OPERATE_PLAN_PERIOD.getCode()).fieldLabel(StrategyIntentField.OPERATE_PLAN_PERIOD.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(StrategyIntentField.OPERATE_HISTORY_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.STRATEGY_INTENT.getCode()).fieldName(StrategyIntentField.OPERATE_HISTORY_YEAR.getCode()).fieldLabel(StrategyIntentField.OPERATE_HISTORY_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(StrategyIntentField.CREATE_BY.getCode(), FieldConfig.builder().businessType(BusinessType.STRATEGY_INTENT.getCode()).fieldName(StrategyIntentField.CREATE_BY.getCode()).fieldLabel(StrategyIntentField.CREATE_BY.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
    }
    @Override
    public BusinessType getBusinessType() {
        return BusinessType.STRATEGY_INTENT;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}