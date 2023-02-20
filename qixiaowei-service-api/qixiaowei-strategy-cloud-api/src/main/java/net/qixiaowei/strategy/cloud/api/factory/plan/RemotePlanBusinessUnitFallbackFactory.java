package net.qixiaowei.strategy.cloud.api.factory.plan;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.plan.PlanBusinessUnitDTO;
import net.qixiaowei.strategy.cloud.api.remote.plan.RemotePlanBusinessUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;


public class RemotePlanBusinessUnitFallbackFactory implements FallbackFactory<RemotePlanBusinessUnitService> {

    private static final Logger log = LoggerFactory.getLogger(RemotePlanBusinessUnitFallbackFactory.class);

    @Override
    public RemotePlanBusinessUnitService create(Throwable throwable) {
        log.error("规划业务单元远程服务调用失败:{}", throwable.getMessage());
        return new RemotePlanBusinessUnitService() {

            @Override
            public R<List<PlanBusinessUnitDTO>> remotePlanBusinessUnit(PlanBusinessUnitDTO planBusinessUnitDTO, String source) {
                return R.fail("远程调用规划业务单元列表失败:" + throwable.getMessage());
            }
        };
    }
}
