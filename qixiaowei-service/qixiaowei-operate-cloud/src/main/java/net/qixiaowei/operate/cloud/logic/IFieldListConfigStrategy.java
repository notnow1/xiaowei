package net.qixiaowei.operate.cloud.logic;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.operate.cloud.api.domain.field.FieldListConfig;
import net.qixiaowei.operate.cloud.api.dto.field.FieldConfigDTO;


import java.util.List;

/**
 * @description 用户列表配置策略
 * @Author hzk
 * @Date 2023-02-09 16:36
 **/
public interface IFieldListConfigStrategy {

    /**
     * 属于哪种业务类型
     *
     * @return {@link BusinessType}
     */
    BusinessType getBusinessType();

    /**
     * 获取初始化大小
     *
     * @return int
     */

    int getInitSize();

    /**
     * 初始化用户列表配置
     *
     * @param fieldConfigs 字段配置
     */
    List<FieldListConfig> initUserFieldListConfig(List<FieldConfigDTO> fieldConfigs);

}
