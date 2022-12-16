package net.qixiaowei.operate.cloud.api.factory.salary;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustPlanDTO;
import net.qixiaowei.operate.cloud.api.remote.salary.RemoteSalaryAdjustPlanService;
import net.qixiaowei.operate.cloud.api.remote.salary.RemoteSalaryItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;


public class RemoteSalaryAdjustPlanFallbackFactory implements FallbackFactory<RemoteSalaryAdjustPlanService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteSalaryAdjustPlanFallbackFactory.class);

    @Override
    public RemoteSalaryAdjustPlanService create(Throwable throwable) {
        log.error("个人调薪服务调用失败:{}", throwable.getMessage());
        return new RemoteSalaryAdjustPlanService() {

            @Override
            public R<List<EmpSalaryAdjustPlanDTO>> selectByEmployeeId(Long employeeId, String source) {
                return R.fail("根据员工ID获取个人调薪服务失败:" + throwable.getMessage());
            }

        };
    }
}
