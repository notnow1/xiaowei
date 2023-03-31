package net.qixiaowei.strategy.cloud.api.factory.strategyDecode;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureTaskDTO;
import net.qixiaowei.strategy.cloud.api.remote.strategyDecode.RemoteStrategyMeasureService;
import net.qixiaowei.strategy.cloud.api.vo.strategyDecode.StrategyMeasureDetailVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;


public class RemoteStrategyMeasureFallbackFactory implements FallbackFactory<RemoteStrategyMeasureService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteStrategyMeasureFallbackFactory.class);

    @Override
    public RemoteStrategyMeasureService create(Throwable throwable) {
        log.error("战略举措清单远程服务调用失败:{}", throwable.getMessage());
        return new RemoteStrategyMeasureService() {

            @Override
            public R<List<StrategyMeasureDTO>> remoteStrategyMeasure(StrategyMeasureDTO strategyMeasureDTO, String source) {
                return R.fail("远程调用战略举措清单列表失败:" + throwable.getMessage());
            }

            @Override
            public R<List<StrategyMeasureTaskDTO>> remoteDutyMeasure(StrategyMeasureDetailVO strategyMeasureDetailVO, String source) {
                return R.fail("查询远程战略举措清单列表责任部门失败:" + throwable.getMessage());
            }
        };
    }
}
