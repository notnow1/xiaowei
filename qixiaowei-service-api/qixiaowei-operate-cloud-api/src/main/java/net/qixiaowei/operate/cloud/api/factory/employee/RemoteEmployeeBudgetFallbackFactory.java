package net.qixiaowei.operate.cloud.api.factory.employee;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDTO;
import net.qixiaowei.operate.cloud.api.remote.employee.RemoteEmployeeBudgetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 人力预算服务降级处理
 */
@Component
public class RemoteEmployeeBudgetFallbackFactory implements FallbackFactory<RemoteEmployeeBudgetService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteEmployeeBudgetFallbackFactory.class);

    @Override
    public RemoteEmployeeBudgetService create(Throwable throwable) {
        log.error("服务调用失败:{}", throwable.getMessage());
        return new RemoteEmployeeBudgetService() {

            @Override
            public R<List<EmployeeBudgetDTO>> selectBySystemIds(List<Long> officialRankSystemIds, String source) {
                return R.fail("根据职级体系ID集合查询预算表失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmployeeBudgetDTO>> selectByDepartmentId(Long departmentId, String source) {
                return R.fail("根据部门ID集合查询预算表失败:" + throwable.getMessage());
            }
        };
    }
}
