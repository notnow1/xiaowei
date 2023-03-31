package ${packageName}.logic.impl.field.list;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.operate.BonusBudgetField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.utils.StringUtils;
import ${packageName}.api.domain.field.FieldListConfig;
import ${packageName}.api.dto.field.FieldConfigDTO;
import ${packageName}.logic.IFieldListConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
* ${table.comment!}-字段列表配置实现类
* @author ${author}
* @since ${date}
*/
@Service
@Slf4j
public class ${entity}FieldListConfigImpl implements IFieldListConfigStrategy {

private static final Map<String, FieldListConfig> INIT_MAP = new HashMap<>();




    @Override
    public BusinessType getBusinessType() {
    return BusinessType.枚举值自己加;
    }

    @Override
    public int getInitSize() {
    return INIT_MAP.size();
    }

@Override
public List<FieldListConfig> initUserFieldListConfig(List<FieldConfigDTO> fieldConfigs) {
        if (StringUtils.isEmpty(fieldConfigs)) {
        return null;
        }
        List<FieldListConfig> fieldListConfigs = new ArrayList<>();
            int sort = INIT_MAP.size() + 1;
            for (FieldConfigDTO fieldConfig : fieldConfigs) {
            Long fieldConfigId = fieldConfig.getFieldConfigId();
            String fieldName = fieldConfig.getFieldName();
            FieldListConfig fieldListConfig;
            if (INIT_MAP.containsKey(fieldName)) {
            fieldListConfig = INIT_MAP.get(fieldName);
            fieldListConfig.setFieldConfigId(fieldConfigId);
            } else {
            fieldListConfig = FieldListConfig.builder().fieldConfigId(fieldConfigId).fieldWidth(200).sort(sort).showFlag(1).fixationFlag(0).showForce(0).fixationForce(0).build();
            sort++;
            }
            fieldListConfigs.add(fieldListConfig);
            }
            return fieldListConfigs;
            }

            }