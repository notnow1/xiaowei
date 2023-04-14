package net.qixiaowei.system.manage.api.factory.system;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.system.RegionDTO;
import net.qixiaowei.system.manage.api.remote.system.RemoteRegionService;
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
public class RemoteRegionFallbackFactory implements FallbackFactory<RemoteRegionService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteRegionFallbackFactory.class);

    @Override
    public RemoteRegionService create(Throwable throwable) {
        log.error("区域服务调用失败:{}", throwable.getMessage());
        return new RemoteRegionService() {
            @Override
            public R<List<RegionDTO>> getRegionsByIds(Set<Long> regionIds, String source) {
                return R.fail("获取区域失败:" + throwable.getMessage());
            }

            @Override
            public R<List<RegionDTO>> selectCodeList(List<String> regionNames, String source) {
                return R.fail("根据省份名称集合获取省份失败:" + throwable.getMessage());
            }

            @Override
            public R<List<RegionDTO>> getDropList(RegionDTO regionDTO, String source) {
                return R.fail("远程查询excel省份下拉框失败:" + throwable.getMessage());
            }
        };
    }
}
