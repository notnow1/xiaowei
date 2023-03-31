package net.qixiaowei.strategy.cloud.api.factory.strategyDecode;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDetailDTO;
import net.qixiaowei.strategy.cloud.api.remote.strategyDecode.RemoteAnnualKeyWorkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;


public class RemoteAnnualKeyWorkFallbackFactory implements FallbackFactory<RemoteAnnualKeyWorkService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteAnnualKeyWorkFallbackFactory.class);

    @Override
    public RemoteAnnualKeyWorkService create(Throwable throwable) {
        log.error("年度重点工作远程服务调用失败:{}", throwable.getMessage());
        return new RemoteAnnualKeyWorkService() {

            @Override
            public R<List<AnnualKeyWorkDTO>> remoteAnnualKeyWork(AnnualKeyWorkDTO annualKeyWorkDTO, String source) {
                return R.fail("远程调用年度重点工作列表失败:" + throwable.getMessage());
            }

            @Override
            public R<List<AnnualKeyWorkDetailDTO>> remoteAnnualKeyWorkDepartment(AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO, String source) {
                return R.fail("远程调用根据部门获取年度重点工作列表失败:" + throwable.getMessage());
            }
        };
    }
}
