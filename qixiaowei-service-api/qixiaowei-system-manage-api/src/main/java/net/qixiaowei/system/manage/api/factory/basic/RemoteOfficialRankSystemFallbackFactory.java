package net.qixiaowei.system.manage.api.factory.basic;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteOfficialRankSystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

public class RemoteOfficialRankSystemFallbackFactory implements FallbackFactory<RemoteOfficialRankSystemService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteIndustryFallbackFactory.class);

    @Override
    public RemoteOfficialRankSystemService create(Throwable throwable) {
        log.error("职级服务调用失败:{}", throwable.getMessage());
        return new RemoteOfficialRankSystemService() {

            @Override
            public R<List<OfficialRankSystemDTO>> selectAll(String source) {
                return R.fail("根据ID获取职级信息失败:" + throwable.getMessage());
            }

            @Override
            public R<OfficialRankSystemDTO> selectById(Long officialRankSystemId, String source) {
                return R.fail("根据ID获取职级信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<String>> selectRankById(Long officialRankSystemId, String source) {
                return R.fail("根据ID获取职级上下限信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<OfficialRankSystemDTO>> selectByIds(List<Long> officialRankSystemIds, String source) {
                return R.fail("根据ID集合获取职级信息失败:" + throwable.getMessage());
            }
        };
    }
}
