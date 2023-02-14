package net.qixiaowei.operate.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.ProductField;
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
public class ProductFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(ProductField.PRODUCT_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.PRODUCT.getCode()).fieldName(ProductField.PRODUCT_NAME.getCode()).fieldLabel(ProductField.PRODUCT_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(ProductField.PRODUCT_CODE.getCode(), FieldConfig.builder().businessType(BusinessType.PRODUCT.getCode()).fieldName(ProductField.PRODUCT_CODE.getCode()).fieldLabel(ProductField.PRODUCT_CODE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(ProductField.PARENT_PRODUCT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.PRODUCT.getCode()).fieldName(ProductField.PARENT_PRODUCT_ID.getCode()).fieldLabel(ProductField.PARENT_PRODUCT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(ProductField.LEVEL.getCode(), FieldConfig.builder().businessType(BusinessType.PRODUCT.getCode()).fieldName(ProductField.LEVEL.getCode()).fieldLabel(ProductField.LEVEL.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(ProductField.PRODUCT_UNIT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.PRODUCT.getCode()).fieldName(ProductField.PRODUCT_UNIT_ID.getCode()).fieldLabel(ProductField.PRODUCT_UNIT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(ProductField.PRODUCT_CATEGORY.getCode(), FieldConfig.builder().businessType(BusinessType.PRODUCT.getCode()).fieldName(ProductField.PRODUCT_CATEGORY.getCode()).fieldLabel(ProductField.PRODUCT_CATEGORY.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(ProductField.LISTING_FLAG.getCode(), FieldConfig.builder().businessType(BusinessType.PRODUCT.getCode()).fieldName(ProductField.LISTING_FLAG.getCode()).fieldLabel(ProductField.LISTING_FLAG.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.PRODUCT;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}