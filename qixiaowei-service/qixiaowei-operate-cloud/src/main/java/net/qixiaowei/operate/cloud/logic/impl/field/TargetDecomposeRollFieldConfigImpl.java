package net.qixiaowei.operate.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.TargetDecomposeRollField;
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
public class TargetDecomposeRollFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(TargetDecomposeRollField.TARGET_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.TARGET_DECOMPOSE_ROLL.getCode()).fieldName(TargetDecomposeRollField.TARGET_YEAR.getCode()).fieldLabel(TargetDecomposeRollField.TARGET_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(TargetDecomposeRollField.INDICATOR_ID.getCode(), FieldConfig.builder().businessType(BusinessType.TARGET_DECOMPOSE_ROLL.getCode()).fieldName(TargetDecomposeRollField.INDICATOR_ID.getCode()).fieldLabel(TargetDecomposeRollField.INDICATOR_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(TargetDecomposeRollField.DECOMPOSITION_DIMENSION.getCode(), FieldConfig.builder().businessType(BusinessType.TARGET_DECOMPOSE_ROLL.getCode()).fieldName(TargetDecomposeRollField.DECOMPOSITION_DIMENSION.getCode()).fieldLabel(TargetDecomposeRollField.DECOMPOSITION_DIMENSION.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(TargetDecomposeRollField.TIME_DIMENSION.getCode(), FieldConfig.builder().businessType(BusinessType.TARGET_DECOMPOSE_ROLL.getCode()).fieldName(TargetDecomposeRollField.TIME_DIMENSION.getCode()).fieldLabel(TargetDecomposeRollField.TIME_DIMENSION.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(TargetDecomposeRollField.FORECAST_CYCLE.getCode(), FieldConfig.builder().businessType(BusinessType.TARGET_DECOMPOSE_ROLL.getCode()).fieldName(TargetDecomposeRollField.FORECAST_CYCLE.getCode()).fieldLabel(TargetDecomposeRollField.FORECAST_CYCLE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(TargetDecomposeRollField.TARGET_VALUE.getCode(), FieldConfig.builder().businessType(BusinessType.TARGET_DECOMPOSE_ROLL.getCode()).fieldName(TargetDecomposeRollField.TARGET_VALUE.getCode()).fieldLabel(TargetDecomposeRollField.TARGET_VALUE.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
        INIT_MAP.put(TargetDecomposeRollField.DECOMPOSE_TARGET.getCode(), FieldConfig.builder().businessType(BusinessType.TARGET_DECOMPOSE_ROLL.getCode()).fieldName(TargetDecomposeRollField.DECOMPOSE_TARGET.getCode()).fieldLabel(TargetDecomposeRollField.DECOMPOSE_TARGET.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
        INIT_MAP.put(TargetDecomposeRollField.ACTUAL_TOTAL.getCode(), FieldConfig.builder().businessType(BusinessType.TARGET_DECOMPOSE_ROLL.getCode()).fieldName(TargetDecomposeRollField.ACTUAL_TOTAL.getCode()).fieldLabel(TargetDecomposeRollField.ACTUAL_TOTAL.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
        INIT_MAP.put(TargetDecomposeRollField.STATUS.getCode(), FieldConfig.builder().businessType(BusinessType.TARGET_DECOMPOSE_ROLL.getCode()).fieldName(TargetDecomposeRollField.STATUS.getCode()).fieldLabel(TargetDecomposeRollField.STATUS.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.TARGET_DECOMPOSE_ROLL;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}