package net.qixiaowei.operate.cloud.api.remote.employee;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDTO;
import net.qixiaowei.operate.cloud.api.factory.employee.RemoteEmployeeBudgetFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 人力预算服务
 */
@FeignClient(contextId = "remoteEmployeeBudgetService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteEmployeeBudgetFallbackFactory.class)
public interface RemoteEmployeeBudgetService {

    String API_PREFIX_PERFORMANCE_APPRAISAL = "/employeeBudget";

    /**
     * 根据职级体系ID集合查询预算表
     */
    @PostMapping(API_PREFIX_PERFORMANCE_APPRAISAL + "/selectBySystemIds")
    R<List<EmployeeBudgetDTO>> selectBySystemIds(@RequestBody List<Long> officialRankSystemIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据部门ID集合查询预算表
     */
    @PostMapping(API_PREFIX_PERFORMANCE_APPRAISAL + "/queryDepartmentIds")
    R<List<EmployeeBudgetDTO>> selectByDepartmentIds(@RequestBody List<Long> departmentIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
