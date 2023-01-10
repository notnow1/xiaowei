package net.qixiaowei.operate.cloud.api.remote.bonus;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.domain.bonus.EmployeeAnnualBonus;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecompose;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDetailsDTO;
import net.qixiaowei.operate.cloud.api.factory.bonus.RemoteEmployeeAnnualBonusFallbackFactory;
import net.qixiaowei.operate.cloud.api.factory.targetManager.RemoteDecomposeFallbackFactory;
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
     * @param employeeId
     * @param source
     * @return
     */
    @GetMapping(API_PREFIX_PERFORMANCE_APPRAISAL + "/queryEmployeeIdEmployeeAnnualBonus")
    R<List<EmployeeAnnualBonus>> selectEmployeeAnnualBonusByEmployeeId(@RequestParam("employeeId") Long employeeId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据部门id查询个人年终奖 (一级部门,申请部门)
     * @param departmentId
     * @param source
     * @return
     */
    @GetMapping(API_PREFIX_PERFORMANCE_APPRAISAL + "/queryDepartmentIdEmployeeAnnualBonus")
    R<List<EmployeeAnnualBonus>> selectEmployeeAnnualBonusByDepartmentId(@RequestParam("departmentId") Long departmentId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}