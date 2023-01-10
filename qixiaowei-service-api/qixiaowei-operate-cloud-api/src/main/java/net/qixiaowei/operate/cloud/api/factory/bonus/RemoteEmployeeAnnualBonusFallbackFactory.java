package net.qixiaowei.operate.cloud.api.factory.bonus;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.domain.bonus.EmployeeAnnualBonus;
import net.qixiaowei.operate.cloud.api.remote.bonus.RemoteEmployeeAnnualBonusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * 个人年终奖服务降级处理
 */
public class RemoteEmployeeAnnualBonusFallbackFactory implements FallbackFactory<RemoteEmployeeAnnualBonusService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteEmployeeAnnualBonusFallbackFactory.class);

    @Override
    public RemoteEmployeeAnnualBonusService create(Throwable throwable) {
        log.error("个人年终奖服务调用失败:{}", throwable.getMessage());
        return new RemoteEmployeeAnnualBonusService() {
            @Override
            public R<List<EmployeeAnnualBonus>> selectEmployeeAnnualBonusByEmployeeId(Long employeeId, String source) {
                return R.fail("根据人员id查询个人年终奖失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmployeeAnnualBonus>> selectEmployeeAnnualBonusByDepartmentId(Long departmentId, String source) {
                return R.fail("根据部门id查询个人年终奖失败:" + throwable.getMessage());
            }
        };
    }
}
