package net.qixiaowei.system.manage.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.system.PostField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.system.manage.api.domain.field.FieldConfig;
import net.qixiaowei.system.manage.logic.IFieldConfigStrategy;
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
public class PostFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_POST = new HashMap<>();

    static {
        INIT_POST.put(PostField.POST_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.POST.getCode()).fieldName(PostField.POST_NAME.getCode()).fieldLabel(PostField.POST_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_POST.put(PostField.POST_CODE.getCode(), FieldConfig.builder().businessType(BusinessType.POST.getCode()).fieldName(PostField.POST_CODE.getCode()).fieldLabel(PostField.POST_CODE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_POST.put(PostField.OFFICIAL_RANK_SYSTEM_ID.getCode(), FieldConfig.builder().businessType(BusinessType.POST.getCode()).fieldName(PostField.OFFICIAL_RANK_SYSTEM_ID.getCode()).fieldLabel(PostField.OFFICIAL_RANK_SYSTEM_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_POST.put(PostField.POST_RANK.getCode(), FieldConfig.builder().businessType(BusinessType.POST.getCode()).fieldName(PostField.POST_RANK.getCode()).fieldLabel(PostField.POST_RANK.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_POST.put(PostField.POST_RANK_LOWER.getCode(), FieldConfig.builder().businessType(BusinessType.POST.getCode()).fieldName(PostField.POST_RANK_LOWER.getCode()).fieldLabel(PostField.POST_RANK_LOWER.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_POST.put(PostField.POST_RANK_UPPER.getCode(), FieldConfig.builder().businessType(BusinessType.POST.getCode()).fieldName(PostField.POST_RANK_UPPER.getCode()).fieldLabel(PostField.POST_RANK_UPPER.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_POST.put(PostField.STATUS.getCode(), FieldConfig.builder().businessType(BusinessType.POST.getCode()).fieldName(PostField.STATUS.getCode()).fieldLabel(PostField.STATUS.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.POST;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_POST.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}