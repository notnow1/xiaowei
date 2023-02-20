package net.qixiaowei.strategy.cloud.api.factory.industry;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.remote.industry.RemoteIndustryAttractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;


public class RemoteIndustryAttractionFallbackFactory implements FallbackFactory<RemoteIndustryAttractionService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteIndustryAttractionFallbackFactory.class);

    @Override
    public RemoteIndustryAttractionService create(Throwable throwable) {
        log.error("行业吸引力配置服务调用失败:{}", throwable.getMessage());
        return new RemoteIndustryAttractionService() {

            @Override
            public R<Boolean> initIndustryAttraction(String source) {
                return R.fail("初始化行业吸引力配置失败:" + throwable.getMessage());
            }
        };
    }
}
