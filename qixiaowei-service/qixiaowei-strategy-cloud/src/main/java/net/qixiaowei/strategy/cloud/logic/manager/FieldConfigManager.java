package net.qixiaowei.strategy.cloud.logic.manager;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.strategy.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.strategy.cloud.logic.IFieldConfigStrategy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

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
        return fieldConfigStrategy.initFieldConfig();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, IFieldConfigStrategy> strategyMap = applicationContext.getBeansOfType(IFieldConfigStrategy.class);
        strategyMap.values().forEach(strategyService -> {
            fieldConfigStrategyMap.put(strategyService.getBusinessType(), strategyService);
        });
    }
}
