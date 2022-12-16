package net.qixiaowei.operate.cloud.remote.performance;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceAppraisalObjects;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.remote.performance.RemotePerformanceAppraisalService;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceAppraisalObjectsService;
import net.qixiaowei.operate.cloud.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author TANGMICHI
 * @since 2022-10-08
 */
@RestController
@RequestMapping("performanceAppraisal")
public class RemotePerformanceAppraisal implements RemotePerformanceAppraisalService {

    @Autowired
    private IPerformanceAppraisalObjectsService performanceAppraisalObjectsService;

    @Override
    @InnerAuth
    @PostMapping("/performanceAppraisal")
    public R<List<PerformanceAppraisalObjectsDTO>> performanceResult(@RequestParam("performanceObjectId") Long performanceObjectId, String source) {
        return R.ok(performanceAppraisalObjectsService.performanceResult(performanceObjectId));
    }

}
