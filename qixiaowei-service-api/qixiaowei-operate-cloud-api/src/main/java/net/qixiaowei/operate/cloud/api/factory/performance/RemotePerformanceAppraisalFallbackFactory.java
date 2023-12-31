package net.qixiaowei.operate.cloud.api.factory.performance;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalItemsDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.api.remote.performance.RemotePerformanceAppraisalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * 绩效考核服务降级处理
 */
@Component
public class RemotePerformanceAppraisalFallbackFactory implements FallbackFactory<RemotePerformanceAppraisalService> {
    private static final Logger log = LoggerFactory.getLogger(RemotePerformanceAppraisalFallbackFactory.class);

    @Override
    public RemotePerformanceAppraisalService create(Throwable throwable) {
        log.error("服务调用失败:{}", throwable.getMessage());
        return new RemotePerformanceAppraisalService() {

            @Override
            public R<List<PerformanceAppraisalObjectsDTO>> performanceResult(Long performanceObjectId, String source) {
                return R.fail("查询员工近三次考核成绩失败:" + throwable.getMessage());
            }

            @Override
            public R<List<PerformanceAppraisalObjectsDTO>> queryQuoteEmployeeById(Long employeeId, String source) {
                return R.fail("根据员工ID查询绩效考核是否被引用失败:" + throwable.getMessage());
            }

            @Override
            public R<List<PerformanceAppraisalItemsDTO>> selectByIndicatorIds(Map<Integer, List<Long>> map, String source) {
                return R.fail("根据员工ID查询绩效考核是否被引用失败:" + throwable.getMessage());
            }

            @Override
            public R<List<PerformanceAppraisalObjectsDTO>> selectByDepartmentIds(List<Long> departmentIds, String source) {
                return R.fail("根据部门ID查询绩效考核是否被引用失败:" + throwable.getMessage());
            }

            @Override
            public R<List<PerformanceAppraisalObjectsDTO>> queryQuoteEmployeeByIds(List<Long> employeeIds, String source) {
                return R.fail("根据人员id集合查询考核绩效失败:" + throwable.getMessage());
            }

        };
    }
}
