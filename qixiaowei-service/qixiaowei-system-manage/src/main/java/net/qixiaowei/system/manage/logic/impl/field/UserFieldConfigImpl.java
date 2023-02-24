package net.qixiaowei.system.manage.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.system.UserField;
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
public class UserFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_USER = new HashMap<>();

    static {
        INIT_USER.put(UserField.USER_ACCOUNT.getCode(), FieldConfig.builder().businessType(BusinessType.USER.getCode()).fieldName(UserField.USER_ACCOUNT.getCode()).fieldLabel(UserField.USER_ACCOUNT.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_USER.put(UserField.EMPLOYEE_ID.getCode(), FieldConfig.builder().businessType(BusinessType.USER.getCode()).fieldName(UserField.EMPLOYEE_ID.getCode()).fieldLabel(UserField.EMPLOYEE_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_USER.put(UserField.MOBILE_PHONE.getCode(), FieldConfig.builder().businessType(BusinessType.USER.getCode()).fieldName(UserField.MOBILE_PHONE.getCode()).fieldLabel(UserField.MOBILE_PHONE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_USER.put(UserField.EMAIL.getCode(), FieldConfig.builder().businessType(BusinessType.USER.getCode()).fieldName(UserField.EMAIL.getCode()).fieldLabel(UserField.EMAIL.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_USER.put(UserField.USER_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.USER.getCode()).fieldName(UserField.USER_NAME.getCode()).fieldLabel(UserField.USER_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_USER.put(UserField.STATUS.getCode(), FieldConfig.builder().businessType(BusinessType.USER.getCode()).fieldName(UserField.STATUS.getCode()).fieldLabel(UserField.STATUS.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.USER;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_USER.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}