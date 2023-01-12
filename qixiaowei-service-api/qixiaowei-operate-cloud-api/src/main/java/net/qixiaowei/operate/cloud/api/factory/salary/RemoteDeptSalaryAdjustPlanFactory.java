package net.qixiaowei.operate.cloud.api.factory.salary;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptSalaryAdjustItemDTO;
import net.qixiaowei.operate.cloud.api.remote.salary.RemoteDeptSalaryAdjustPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


public class RemoteDeptSalaryAdjustPlanFactory implements FallbackFactory<RemoteDeptSalaryAdjustPlanService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteDeptSalaryAdjustPlanFactory.class);

    @Override
    public RemoteDeptSalaryAdjustPlanService create(Throwable throwable) {
        log.error("部门调薪服务调用失败:{}", throwable.getMessage());
        return new RemoteDeptSalaryAdjustPlanService() {

            @Override
            public R<List<DeptSalaryAdjustItemDTO>> selectByDepartmentId(Long departmentId, String source) {
                return R.fail("根据部门ID集合查询部门调薪:" + throwable.getMessage());
            }

        };
    }
}
