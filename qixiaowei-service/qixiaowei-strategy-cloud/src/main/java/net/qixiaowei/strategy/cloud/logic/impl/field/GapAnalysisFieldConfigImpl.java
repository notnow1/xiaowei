package net.qixiaowei.strategy.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.strategy.GapAnalysisField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.strategy.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.strategy.cloud.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 差距分析表-字段配置实现类
 *
 * @author Graves
 * @since 2023-02-24
 */
@Service
@Slf4j
public class GapAnalysisFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(GapAnalysisField.PLAN_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.GAP_ANALYSIS.getCode()).fieldName(GapAnalysisField.PLAN_YEAR.getCode()).fieldLabel(GapAnalysisField.PLAN_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(GapAnalysisField.BUSINESS_UNIT_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.GAP_ANALYSIS.getCode()).fieldName(GapAnalysisField.BUSINESS_UNIT_NAME.getCode()).fieldLabel(GapAnalysisField.BUSINESS_UNIT_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(GapAnalysisField.AREA_ID.getCode(), FieldConfig.builder().businessType(BusinessType.GAP_ANALYSIS.getCode()).fieldName(GapAnalysisField.AREA_ID.getCode()).fieldLabel(GapAnalysisField.AREA_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(GapAnalysisField.DEPARTMENT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.GAP_ANALYSIS.getCode()).fieldName(GapAnalysisField.DEPARTMENT_ID.getCode()).fieldLabel(GapAnalysisField.DEPARTMENT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(GapAnalysisField.PRODUCT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.GAP_ANALYSIS.getCode()).fieldName(GapAnalysisField.PRODUCT_ID.getCode()).fieldLabel(GapAnalysisField.PRODUCT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(GapAnalysisField.INDUSTRY_ID.getCode(), FieldConfig.builder().businessType(BusinessType.GAP_ANALYSIS.getCode()).fieldName(GapAnalysisField.INDUSTRY_ID.getCode()).fieldLabel(GapAnalysisField.INDUSTRY_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
    }

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.GAP_ANALYSIS;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}