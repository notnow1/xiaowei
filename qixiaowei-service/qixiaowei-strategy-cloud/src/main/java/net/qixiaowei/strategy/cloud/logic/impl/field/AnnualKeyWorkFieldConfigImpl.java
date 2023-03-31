package net.qixiaowei.strategy.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.strategy.AnnualKeyWorkField;
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
 *
 * @author Graves
 * @since 2023-03-14
 */
@Service
@Slf4j
public class AnnualKeyWorkFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(AnnualKeyWorkField.PLAN_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.ANNUAL_KEY_WORK.getCode()).fieldName(AnnualKeyWorkField.PLAN_YEAR.getCode()).fieldLabel(AnnualKeyWorkField.PLAN_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(AnnualKeyWorkField.BUSINESS_UNIT_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.ANNUAL_KEY_WORK.getCode()).fieldName(AnnualKeyWorkField.BUSINESS_UNIT_NAME.getCode()).fieldLabel(AnnualKeyWorkField.BUSINESS_UNIT_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(AnnualKeyWorkField.AREA_ID.getCode(), FieldConfig.builder().businessType(BusinessType.ANNUAL_KEY_WORK.getCode()).fieldName(AnnualKeyWorkField.AREA_ID.getCode()).fieldLabel(AnnualKeyWorkField.AREA_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(AnnualKeyWorkField.DEPARTMENT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.ANNUAL_KEY_WORK.getCode()).fieldName(AnnualKeyWorkField.DEPARTMENT_ID.getCode()).fieldLabel(AnnualKeyWorkField.DEPARTMENT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(AnnualKeyWorkField.PRODUCT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.ANNUAL_KEY_WORK.getCode()).fieldName(AnnualKeyWorkField.PRODUCT_ID.getCode()).fieldLabel(AnnualKeyWorkField.PRODUCT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(AnnualKeyWorkField.INDUSTRY_ID.getCode(), FieldConfig.builder().businessType(BusinessType.ANNUAL_KEY_WORK.getCode()).fieldName(AnnualKeyWorkField.INDUSTRY_ID.getCode()).fieldLabel(AnnualKeyWorkField.INDUSTRY_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
    }

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