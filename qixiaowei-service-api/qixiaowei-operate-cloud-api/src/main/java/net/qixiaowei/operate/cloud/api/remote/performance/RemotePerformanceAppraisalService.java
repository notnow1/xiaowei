package net.qixiaowei.operate.cloud.api.remote.performance;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalItemsDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.api.factory.performance.RemotePerformanceAppraisalFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户服务
 */
@FeignClient(contextId = "remotePerformanceAppraisalService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemotePerformanceAppraisalFallbackFactory.class)
public interface RemotePerformanceAppraisalService {

    String API_PREFIX_PERFORMANCE_APPRAISAL = "/performanceAppraisal";

    /**
     * 查询员工近三次考核成绩
     */
    @GetMapping(API_PREFIX_PERFORMANCE_APPRAISAL + "/queryEmployeeResult")
    R<List<PerformanceAppraisalObjectsDTO>> performanceResult(@RequestParam("performanceObjectId") Long performanceObjectId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据员工ID查询绩效考核是否被引用
     */
    @GetMapping(API_PREFIX_PERFORMANCE_APPRAISAL + "/queryQuoteEmployeeById")
    R<List<PerformanceAppraisalObjectsDTO>> queryQuoteEmployeeById(@RequestParam("employeeId") Long employeeId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据指标ID集合查询绩效
     *
     * @param map 指标ID集合
     * @return List
     */
    @PostMapping(API_PREFIX_PERFORMANCE_APPRAISAL + "/selectByIndicatorIds")
    R<List<PerformanceAppraisalItemsDTO>> selectByIndicatorIds(@RequestBody Map<Integer, List<Long>> map, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据指标ID集合查询绩效
     *
     * @param departmentIds 部门ID集合
     * @return List
     */
    @PostMapping(API_PREFIX_PERFORMANCE_APPRAISAL + "/selectByDepartmentIds")
    R<List<PerformanceAppraisalObjectsDTO>> selectByDepartmentIds(@RequestBody List<Long> departmentIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
