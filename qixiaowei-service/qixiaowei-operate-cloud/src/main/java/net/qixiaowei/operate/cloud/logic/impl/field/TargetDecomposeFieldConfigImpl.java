package net.qixiaowei.operate.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.TargetDecomposeField;
import net.qixiaowei.integration.common.enums.field.system.UserField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.operate.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.operate.cloud.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description 用户-字段配置实现类
 * @Author hzk
 * @Date 2023-02-09 19:55
 **/
@Service
@Slf4j
public class TargetDecomposeFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(TargetDecomposeField.TARGET_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.TARGET_DECOMPOSE.getCode()).fieldName(TargetDecomposeField.TARGET_YEAR.getCode()).fieldLabel(TargetDecomposeField.TARGET_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(TargetDecomposeField.INDICATOR_ID.getCode(), FieldConfig.builder().businessType(BusinessType.TARGET_DECOMPOSE.getCode()).fieldName(TargetDecomposeField.INDICATOR_ID.getCode()).fieldLabel(TargetDecomposeField.INDICATOR_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(TargetDecomposeField.DECOMPOSITION_DIMENSION.getCode(), FieldConfig.builder().businessType(BusinessType.TARGET_DECOMPOSE.getCode()).fieldName(TargetDecomposeField.DECOMPOSITION_DIMENSION.getCode()).fieldLabel(TargetDecomposeField.DECOMPOSITION_DIMENSION.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(TargetDecomposeField.TIME_DIMENSION.getCode(), FieldConfig.builder().businessType(BusinessType.TARGET_DECOMPOSE.getCode()).fieldName(TargetDecomposeField.TIME_DIMENSION.getCode()).fieldLabel(TargetDecomposeField.TIME_DIMENSION.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(TargetDecomposeField.TARGET_VALUE.getCode(), FieldConfig.builder().businessType(BusinessType.TARGET_DECOMPOSE.getCode()).fieldName(TargetDecomposeField.TARGET_VALUE.getCode()).fieldLabel(TargetDecomposeField.TARGET_VALUE.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
        INIT_MAP.put(TargetDecomposeField.DECOMPOSE_TARGET.getCode(), FieldConfig.builder().businessType(BusinessType.TARGET_DECOMPOSE.getCode()).fieldName(TargetDecomposeField.DECOMPOSE_TARGET.getCode()).fieldLabel(TargetDecomposeField.DECOMPOSE_TARGET.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.TARGET_DECOMPOSE;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}