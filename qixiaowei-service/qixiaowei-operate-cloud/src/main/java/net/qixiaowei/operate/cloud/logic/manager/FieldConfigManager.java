package net.qixiaowei.operate.cloud.logic.manager;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.field.BaseField;
import net.qixiaowei.integration.common.enums.field.FieldType;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.operate.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.operate.cloud.logic.IFieldConfigStrategy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description 字段列表配置管理
 * @Author hzk
 * @Date 2023-02-09 16:41
 **/
@Component
@Slf4j
public class FieldConfigManager implements ApplicationContextAware {

    private final Map<BusinessType, IFieldConfigStrategy> fieldConfigStrategyMap = new ConcurrentHashMap<>();

    /**
     * 初始化用户字段列表配置
     *
     * @param businessType 业务类型
     */
    public List<FieldConfig> initFieldConfig(Integer businessType) {
        if (StringUtils.isNull(businessType)) {
            throw new ServiceException("业务类型不能为空");
        }
        BusinessType businessTypeEnum = BusinessType.getBusinessType(businessType);
        if (StringUtils.isNull(businessTypeEnum)) {
            throw new ServiceException("业务类型错误");
        }
        IFieldConfigStrategy fieldConfigStrategy = fieldConfigStrategyMap.get(businessTypeEnum);
        if (StringUtils.isNull(fieldConfigStrategy)) {
            throw new ServiceException("业务类型错误");
        }
        List<FieldConfig> fieldConfigList = fieldConfigStrategy.initFieldConfig();
        if (StringUtils.isEmpty(fieldConfigList)) {
            fieldConfigList = new ArrayList<>();
        }
        fieldConfigList.add(FieldConfig.builder().businessType(businessType).fieldName(BaseField.CREATE_TIME.getCode()).fieldLabel(BaseField.CREATE_TIME.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        fieldConfigList.add(FieldConfig.builder().businessType(businessType).fieldName(BaseField.CREATE_BY.getCode()).fieldLabel(BaseField.CREATE_BY.getInfo()).fieldType(FieldType.TEXT.getCode()).build());
        return fieldConfigList;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, IFieldConfigStrategy> strategyMap = applicationContext.getBeansOfType(IFieldConfigStrategy.class);
        strategyMap.values().forEach(strategyService -> {
            fieldConfigStrategyMap.put(strategyService.getBusinessType(), strategyService);
        });
    }
}
