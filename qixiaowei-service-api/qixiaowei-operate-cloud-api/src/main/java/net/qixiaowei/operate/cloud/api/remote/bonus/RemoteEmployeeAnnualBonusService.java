package net.qixiaowei.operate.cloud.api.remote.bonus;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.domain.bonus.EmployeeAnnualBonus;
import net.qixiaowei.operate.cloud.api.factory.bonus.RemoteEmployeeAnnualBonusFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 个人年终奖服务
 */
@FeignClient(contextId = "remoteEmployeeAnnualBonusService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteEmployeeAnnualBonusFallbackFactory.class)
public interface RemoteEmployeeAnnualBonusService {
    String API_PREFIX_PERFORMANCE_APPRAISAL = "/employeeAnnualBonus/remote";
    /**
     * 根据人员id查询个人年终奖 申请人id
     * @param employeeIds
     * @param source
     * @return
     */
    @PostMapping(API_PREFIX_PERFORMANCE_APPRAISAL + "/queryEmployeeIdEmployeeAnnualBonus")
    R<List<EmployeeAnnualBonus>> selectEmployeeAnnualBonusByEmployeeIds(@RequestBody List<Long> employeeIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据部门id查询个人年终奖 (一级部门,申请部门)
     * @param departmentIds
     * @param source
     * @return
     */
    @PostMapping(API_PREFIX_PERFORMANCE_APPRAISAL + "/queryDepartmentIdEmployeeAnnualBonus")
    R<List<EmployeeAnnualBonus>> selectEmployeeAnnualBonusByDepartmentIds(@RequestBody List<Long> departmentIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}