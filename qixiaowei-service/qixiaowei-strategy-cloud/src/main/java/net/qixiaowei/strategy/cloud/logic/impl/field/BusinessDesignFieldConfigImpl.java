package net.qixiaowei.strategy.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.strategy.BusinessDesignField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.strategy.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.strategy.cloud.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 业务设计表-字段配置实现类
 *
 * @author Graves
 * @since 2023-02-28
 */
@Service
@Slf4j
public class BusinessDesignFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(BusinessDesignField.PLAN_YEAR.getCode(), FieldConfig.builder().businessType(BusinessType.GAP_ANALYSIS.getCode()).fieldName(BusinessDesignField.PLAN_YEAR.getCode()).fieldLabel(BusinessDesignField.PLAN_YEAR.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(BusinessDesignField.BUSINESS_UNIT_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.GAP_ANALYSIS.getCode()).fieldName(BusinessDesignField.BUSINESS_UNIT_NAME.getCode()).fieldLabel(BusinessDesignField.BUSINESS_UNIT_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(BusinessDesignField.AREA_ID.getCode(), FieldConfig.builder().businessType(BusinessType.GAP_ANALYSIS.getCode()).fieldName(BusinessDesignField.AREA_ID.getCode()).fieldLabel(BusinessDesignField.AREA_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(BusinessDesignField.DEPARTMENT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.GAP_ANALYSIS.getCode()).fieldName(BusinessDesignField.DEPARTMENT_ID.getCode()).fieldLabel(BusinessDesignField.DEPARTMENT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(BusinessDesignField.PRODUCT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.GAP_ANALYSIS.getCode()).fieldName(BusinessDesignField.PRODUCT_ID.getCode()).fieldLabel(BusinessDesignField.PRODUCT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(BusinessDesignField.INDUSTRY_ID.getCode(), FieldConfig.builder().businessType(BusinessType.GAP_ANALYSIS.getCode()).fieldName(BusinessDesignField.INDUSTRY_ID.getCode()).fieldLabel(BusinessDesignField.INDUSTRY_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
    }

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.BUSINESS_DESIGN;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}