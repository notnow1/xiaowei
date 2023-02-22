package net.qixiaowei.strategy.cloud.logic.manager;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.strategy.cloud.api.domain.field.FieldListConfig;
import net.qixiaowei.strategy.cloud.api.dto.field.FieldConfigDTO;
import net.qixiaowei.strategy.cloud.logic.IFieldListConfigStrategy;
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
public class FieldListConfigManager implements ApplicationContextAware {

    private final Map<BusinessType, IFieldListConfigStrategy> fieldListConfigStrategyMap = new ConcurrentHashMap<>();

    /**
     * 获取初始化大小
     *
     * @return int
     */
    public Integer getInitSize(Integer businessType) {
        if (StringUtils.isNull(businessType)) {
            throw new ServiceException("业务类型不能为空");
        }
        BusinessType businessTypeEnum = BusinessType.getBusinessType(businessType);
        if (StringUtils.isNull(businessTypeEnum)) {
            throw new ServiceException("业务类型错误");
        }
        IFieldListConfigStrategy fieldListConfigStrategy = fieldListConfigStrategyMap.get(businessTypeEnum);
        if (StringUtils.isNull(fieldListConfigStrategy)) {
            throw new ServiceException("业务类型错误");
        }
        return fieldListConfigStrategy.getInitSize();
    }

    /**
     * 初始化用户字段列表配置
     *
     * @param businessType 业务类型
     * @param fieldConfigs 字段配置
     */
    public List<FieldListConfig> initUserFieldListConfig(Integer businessType, List<FieldConfigDTO> fieldConfigs) {
        if (StringUtils.isNull(businessType)) {
            throw new ServiceException("业务类型不能为空");
        }
        BusinessType businessTypeEnum = BusinessType.getBusinessType(businessType);
        if (StringUtils.isNull(businessTypeEnum)) {
            throw new ServiceException("业务类型错误");
        }
        IFieldListConfigStrategy fieldListConfigStrategy = fieldListConfigStrategyMap.get(businessTypeEnum);
        return fieldListConfigStrategy.initUserFieldListConfig(fieldConfigs);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, IFieldListConfigStrategy> strategyMap = applicationContext.getBeansOfType(IFieldListConfigStrategy.class);
        strategyMap.values().forEach(strategyService -> {
            fieldListConfigStrategyMap.put(strategyService.getBusinessType(), strategyService);
        });
    }
}
