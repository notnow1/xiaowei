package net.qixiaowei.operate.cloud.api.remote.performance;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceAppraisalObjects;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.factory.performance.RemotePerformanceAppraisalFallbackFactory;
import net.qixiaowei.operate.cloud.api.factory.product.RemoteProductFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

//    /**
//     * 根据员工ID查询绩效考核是否被引用
//     */
//    @GetMapping(API_PREFIX_PERFORMANCE_APPRAISAL + "/queryEmployeeResult")
//    R<List<PerformanceAppraisalObjectsDTO>> performanceResult(@RequestParam("performanceObjectId") Long performanceObjectId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
