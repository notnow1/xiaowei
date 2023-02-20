package net.qixiaowei.system.manage.api.factory.basic;

import cn.hutool.core.lang.tree.Tree;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndustryDefaultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 部门服务降级处理
 */
@Component
public class RemoteIndustryDefaultFallbackFactory implements FallbackFactory<RemoteIndustryDefaultService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteIndustryDefaultFallbackFactory.class);

    @Override
    public RemoteIndustryDefaultService create(Throwable throwable) {
        log.error("默认行业服务调用失败:{}", throwable.getMessage());
        return new RemoteIndustryDefaultService() {
            @Override
            public R<List<Tree<Long>>> getTreeList(String source) {
                return R.fail("获取默认行业树结构失败:" + throwable.getMessage());
            }

        };
    }
}
