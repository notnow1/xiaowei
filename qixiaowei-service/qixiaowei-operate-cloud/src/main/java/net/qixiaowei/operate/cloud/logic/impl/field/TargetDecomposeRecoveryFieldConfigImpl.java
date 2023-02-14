package net.qixiaowei.operate.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.TargetDecomposeRecoveryField;
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
public class TargetDecomposeRecoveryFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(TargetDecomposeRecoveryField.TARGET_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.TARGET_DECOMPOSE_RECOVERY.getCode()).fieldName(TargetDecomposeRecoveryField.TARGET_YEAR.getCode()).fieldLabel(TargetDecomposeRecoveryField.TARGET_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(TargetDecomposeRecoveryField.DECOMPOSITION_DIMENSION.getCode(), FieldConfig.builder().businessType(BusinessType.TARGET_DECOMPOSE_RECOVERY.getCode()).fieldName(TargetDecomposeRecoveryField.DECOMPOSITION_DIMENSION.getCode()).fieldLabel(TargetDecomposeRecoveryField.DECOMPOSITION_DIMENSION.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(TargetDecomposeRecoveryField.TIME_DIMENSION.getCode(), FieldConfig.builder().businessType(BusinessType.TARGET_DECOMPOSE_RECOVERY.getCode()).fieldName(TargetDecomposeRecoveryField.TIME_DIMENSION.getCode()).fieldLabel(TargetDecomposeRecoveryField.TIME_DIMENSION.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(TargetDecomposeRecoveryField.TARGET_VALUE.getCode(), FieldConfig.builder().businessType(BusinessType.TARGET_DECOMPOSE_RECOVERY.getCode()).fieldName(TargetDecomposeRecoveryField.TARGET_VALUE.getCode()).fieldLabel(TargetDecomposeRecoveryField.TARGET_VALUE.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
        INIT_MAP.put(TargetDecomposeRecoveryField.DECOMPOSE_TARGET.getCode(), FieldConfig.builder().businessType(BusinessType.TARGET_DECOMPOSE_RECOVERY.getCode()).fieldName(TargetDecomposeRecoveryField.DECOMPOSE_TARGET.getCode()).fieldLabel(TargetDecomposeRecoveryField.DECOMPOSE_TARGET.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.TARGET_DECOMPOSE_RECOVERY;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}