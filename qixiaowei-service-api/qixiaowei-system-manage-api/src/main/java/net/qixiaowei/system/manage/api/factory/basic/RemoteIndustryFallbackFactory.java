package net.qixiaowei.system.manage.api.factory.basic;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndustryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 部门服务降级处理
 */
@Component
public class RemoteIndustryFallbackFactory implements FallbackFactory<RemoteIndustryService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteIndustryFallbackFactory.class);

    @Override
    public RemoteIndustryService create(Throwable throwable) {
        log.error("行业服务调用失败:{}", throwable.getMessage());
        return new RemoteIndustryService() {

            @Override
            public R<List<IndustryDTO>> selectCodeList(List<String> departmentCodes, String source) {
                return R.fail("根据code集合获取行业信息失败:" + throwable.getMessage());
            }
        };
    }
}
