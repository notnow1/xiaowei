package net.qixiaowei.operate.cloud.api.factory.bonus;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.domain.bonus.BonusPayObjects;
import net.qixiaowei.operate.cloud.api.domain.bonus.EmployeeAnnualBonus;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayApplicationDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayObjectsDTO;
import net.qixiaowei.operate.cloud.api.remote.bonus.RemoteBonusPayApplicationService;
import net.qixiaowei.operate.cloud.api.remote.bonus.RemoteEmployeeAnnualBonusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * 奖金发放申请服务降级处理
 */
public class RemoteBonusPayApplicationFallbackFactory implements FallbackFactory<RemoteBonusPayApplicationService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteBonusPayApplicationFallbackFactory.class);

    @Override
    public RemoteBonusPayApplicationService create(Throwable throwable) {
        log.error("奖金发放申请服务调用失败:{}", throwable.getMessage());
        return new RemoteBonusPayApplicationService() {

            @Override
            public R<List<BonusPayObjectsDTO>> selectBonusPayApplicationByEmployeeId(Long employeeId, String source) {
                return R.fail("根据人员id查询奖金发放申请失败:" + throwable.getMessage());
            }

            @Override
            public R<List<BonusPayApplicationDTO>> selectBonusPayApplicationByDepartmentId(Long departmentId, String source) {
                return R.fail("根据部门id查询奖金发放申请失败:" + throwable.getMessage());
            }
        };
    }
}
