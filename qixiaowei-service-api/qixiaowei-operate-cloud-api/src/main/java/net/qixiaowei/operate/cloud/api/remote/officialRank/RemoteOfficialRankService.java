package net.qixiaowei.operate.cloud.api.remote.officialRank;

import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import net.qixiaowei.operate.cloud.api.factory.RemoteOfficialRankFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(contextId = "remoteOfficialRankService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteOfficialRankFallbackFactory.class)
public interface RemoteOfficialRankService {
    /**
     * 查询产品是否用到枚举
     */
    @GetMapping("/area/dropList")
    R<List<AreaDTO>> queryOfficialRankList(@RequestBody AreaDTO areaDTO);
}