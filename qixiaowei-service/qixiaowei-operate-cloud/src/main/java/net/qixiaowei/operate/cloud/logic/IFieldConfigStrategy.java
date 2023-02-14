package net.qixiaowei.operate.cloud.logic;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.operate.cloud.api.domain.field.FieldConfig;

import java.util.List;

/**
 * @description 字段配置策略
 * @Author hzk
 * @Date 2023-02-09 16:36
 **/
public interface IFieldConfigStrategy {

    /**
     * 属于哪种业务类型
     *
     * @return {@link BusinessType}
     */
    BusinessType getBusinessType();

    /**
     * 初始化字段配置
     *
     */
    List<FieldConfig> initFieldConfig();
}
