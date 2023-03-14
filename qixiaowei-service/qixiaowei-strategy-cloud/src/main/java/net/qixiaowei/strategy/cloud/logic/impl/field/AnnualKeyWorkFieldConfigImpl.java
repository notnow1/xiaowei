package net.qixiaowei.strategy.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.BonusBudgetField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.strategy.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.strategy.cloud.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
* 年度重点工作表-字段配置实现类
* @author Graves
* @since 2023-03-14
*/
@Service
@Slf4j
public class AnnualKeyWorkFieldConfigImpl implements IFieldConfigStrategy {

private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();




@Override
public BusinessType getBusinessType() {
return BusinessType.ANNUAL_KEY_WORK;
}


@Override
public List<FieldConfig> initFieldConfig() {
    List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
        }

        }