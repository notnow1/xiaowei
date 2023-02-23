package ${packageName}.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.BonusBudgetField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import ${packageName}.api.domain.field.FieldConfig;
import ${packageName}.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
* ${table.comment!}-字段配置实现类
* @author ${author}
* @since ${date}
*/
@Service
@Slf4j
public class ${entity}FieldConfigImpl implements IFieldConfigStrategy {

private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();




@Override
public BusinessType getBusinessType() {
return BusinessType.枚举值自己加;
}


@Override
public List<FieldConfig> initFieldConfig() {
    List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
        }

        }