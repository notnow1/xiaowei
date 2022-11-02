package net.qixiaowei.system.manage.api.factory.basic;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 区域服务降级处理
 */
@Component
public class RemoteIndicatorFallbackFactory implements FallbackFactory<RemoteIndicatorService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteIndicatorFallbackFactory.class);

    @Override
    public RemoteIndicatorService create(Throwable throwable) {
        log.error("指标服务调用失败:{}", throwable.getMessage());
        return new RemoteIndicatorService() {
            @Override
            public R<IndicatorDTO> selectIndicatorByCode(String indicatorCode) {
                return R.fail("获取指标失败:" + throwable.getMessage());
            }
        };
    }
}