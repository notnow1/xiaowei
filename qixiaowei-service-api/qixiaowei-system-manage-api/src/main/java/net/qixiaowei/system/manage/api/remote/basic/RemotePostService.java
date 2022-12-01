package net.qixiaowei.system.manage.api.remote.basic;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.system.manage.api.factory.basic.RemotePostFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "RemotePostService", value = ServiceNameConstants.SYSTEM_MANAGE_SERVICE, fallbackFactory = RemotePostFallbackFactory.class)
public interface RemotePostService {
    String API_PREFIX_POST = "/post";

    /**
     * 查找职级等级列表
     *
     * @return 结果
     */
    @GetMapping(API_PREFIX_POST + "/selectPostListByOfficialRank")
    R<List<PostDTO>> selectPostListByOfficialRank(@RequestParam Long officialRankSystemId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
