package net.qixiaowei.operate.cloud.logic.impl.field;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.field.operate.BonusPayApplicationField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.operate.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.operate.cloud.logic.IFieldConfigStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description 奖金发放申请-字段配置实现类
 * @Author hzk
 * @Date 2023-02-09 19:55
 **/
@Service
@Slf4j
public class BonusPayApplicationFieldConfigImpl implements IFieldConfigStrategy {

    private static final Map<String, FieldConfig> INIT_MAP = new HashMap<>();

    static {
        INIT_MAP.put(BonusPayApplicationField.AWARD_NAME.getCode(), FieldConfig.builder().businessType(BusinessType.BONUS_PAY_APPLICATION.getCode()).fieldName(BonusPayApplicationField.AWARD_NAME.getCode()).fieldLabel(BonusPayApplicationField.AWARD_NAME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(BonusPayApplicationField.AWARD_CODE.getCode(), FieldConfig.builder().businessType(BusinessType.BONUS_PAY_APPLICATION.getCode()).fieldName(BonusPayApplicationField.AWARD_CODE.getCode()).fieldLabel(BonusPayApplicationField.AWARD_CODE.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(BonusPayApplicationField.SALARY_ITEM_ID.getCode(), FieldConfig.builder().businessType(BusinessType.BONUS_PAY_APPLICATION.getCode()).fieldName(BonusPayApplicationField.SALARY_ITEM_ID.getCode()).fieldLabel(BonusPayApplicationField.SALARY_ITEM_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(BonusPayApplicationField.AWARD_YEAR_MONTH.getCode(), FieldConfig.builder().businessType(BusinessType.BONUS_PAY_APPLICATION.getCode()).fieldName(BonusPayApplicationField.AWARD_YEAR_MONTH.getCode()).fieldLabel(BonusPayApplicationField.AWARD_YEAR_MONTH.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(BonusPayApplicationField.APPLY_DEPARTMENT_ID.getCode(), FieldConfig.builder().businessType(BusinessType.BONUS_PAY_APPLICATION.getCode()).fieldName(BonusPayApplicationField.APPLY_DEPARTMENT_ID.getCode()).fieldLabel(BonusPayApplicationField.APPLY_DEPARTMENT_ID.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(BonusPayApplicationField.BUDGET_DEPARTMENT_LIST.getCode(), FieldConfig.builder().businessType(BusinessType.BONUS_PAY_APPLICATION.getCode()).fieldName(BonusPayApplicationField.BUDGET_DEPARTMENT_LIST.getCode()).fieldLabel(BonusPayApplicationField.BUDGET_DEPARTMENT_LIST.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(BonusPayApplicationField.AWARD_TOTAL_AMOUNT.getCode(), FieldConfig.builder().businessType(BusinessType.BONUS_PAY_APPLICATION.getCode()).fieldName(BonusPayApplicationField.AWARD_TOTAL_AMOUNT.getCode()).fieldLabel(BonusPayApplicationField.AWARD_TOTAL_AMOUNT.getInfo()).fieldType(FieldType.AMOUNT.getCode()).build());
        INIT_MAP.put(BonusPayApplicationField.BONUS_PAY_OBJECT.getCode(), FieldConfig.builder().businessType(BusinessType.BONUS_PAY_APPLICATION.getCode()).fieldName(BonusPayApplicationField.BONUS_PAY_OBJECT.getCode()).fieldLabel(BonusPayApplicationField.BONUS_PAY_OBJECT.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(BonusPayApplicationField.CREATE_TIME.getCode(), FieldConfig.builder().businessType(BusinessType.BONUS_PAY_APPLICATION.getCode()).fieldName(BonusPayApplicationField.CREATE_TIME.getCode()).fieldLabel(BonusPayApplicationField.CREATE_TIME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        INIT_MAP.put(BonusPayApplicationField.CREATE_BY.getCode(), FieldConfig.builder().businessType(BusinessType.BONUS_PAY_APPLICATION.getCode()).fieldName(BonusPayApplicationField.CREATE_BY.getCode()).fieldLabel(BonusPayApplicationField.CREATE_BY.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
    }


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.BONUS_PAY_APPLICATION;
    }


    @Override
    public List<FieldConfig> initFieldConfig() {
        List<FieldConfig> fieldConfigList = new ArrayList<>();
        INIT_MAP.forEach((key, fieldConfig) -> fieldConfigList.add(fieldConfig));
        return fieldConfigList;
    }

}