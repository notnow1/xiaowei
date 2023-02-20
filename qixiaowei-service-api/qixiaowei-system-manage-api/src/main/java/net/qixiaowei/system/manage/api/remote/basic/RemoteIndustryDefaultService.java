package net.qixiaowei.system.manage.api.remote.basic;

import cn.hutool.core.lang.tree.Tree;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.factory.basic.RemoteIndustryDefaultFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 行业服务
 */
@FeignClient(contextId = "remoteIndustryDefaultService", value = ServiceNameConstants.SYSTEM_MANAGE_SERVICE, fallbackFactory = RemoteIndustryDefaultFallbackFactory.class)
public interface RemoteIndustryDefaultService {
    String API_PREFIX_INDUSTRY_DEFAULT = "/industryDefault";

    /**
     * 获取默认行业树列表
     *
     * @return 结果
     */
    @GetMapping(API_PREFIX_INDUSTRY_DEFAULT + "/getTreeList")
    R<List<Tree<Long>>> getTreeList(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
