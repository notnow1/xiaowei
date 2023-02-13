package net.qixiaowei.operate.cloud.api.factory.targetManager;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecompose;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDetailsDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteDecomposeService;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * 目标分解服务降级处理
 */
public class RemoteSettingFallbackFactory implements FallbackFactory<RemoteSettingService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteSettingFallbackFactory.class);

    @Override
    public RemoteSettingService create(Throwable throwable) {
        log.error("目标制定服务调用失败:{}", throwable.getMessage());
        return new RemoteSettingService() {
            @Override
            public R<List<TargetSettingDTO>> queryIndicatorSetting(List<Long> indicatorIds, String source) {
                return R.fail("根据指标ID查询目标制定是否引用失败:" + throwable.getMessage());
            }

            @Override
            public R<List<TargetSettingDTO>> queryIndicatorSettingList(TargetSettingDTO targetSettingDTO, String source) {
                return R.fail("查询目标制定list引用失败:" + throwable.getMessage());
            }
        };
    }
}
