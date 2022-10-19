package net.qixiaowei.operate.cloud.api.factory;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import net.qixiaowei.operate.cloud.api.remote.officialRank.RemoteOfficialRankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

public class RemoteOfficialRankFallbackFactory implements FallbackFactory<RemoteOfficialRankService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteOfficialRankFallbackFactory.class);

    @Override
    public RemoteOfficialRankService create(Throwable throwable) {
        log.error("服务调用失败:{}", throwable.getMessage());
        return new RemoteOfficialRankService() {
            @Override
            public R<List<AreaDTO>> queryOfficialRankList(AreaDTO areaDTO) {
                return null;
            }
        };
    }
}
