package net.qixiaowei.strategy.cloud.api.factory.businessDesign;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import net.qixiaowei.strategy.cloud.api.remote.businessDesign.RemoteBusinessDesignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;


public class RemoteBusinessDesignFallbackFactory implements FallbackFactory<RemoteBusinessDesignService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteBusinessDesignFallbackFactory.class);

    @Override
    public RemoteBusinessDesignService create(Throwable throwable) {
        log.error("业务设计远程服务调用失败:{}", throwable.getMessage());
        return new RemoteBusinessDesignService() {

            @Override
            public R<List<BusinessDesignDTO>> remoteBusinessDesign(BusinessDesignDTO businessDesignDTO, String source) {
                return R.fail("远程调用业务设计列表失败:" + throwable.getMessage());
            }

            @Override
            public R<List<BusinessDesignParamDTO>> remoteBusinessDesignParams(BusinessDesignParamDTO businessDesignParamDTO, String source) {
                return R.fail("远程调用战略衡量指标详情列表失败:" + throwable.getMessage());
            }

        };
    }
}
