package net.qixiaowei.operate.cloud.remote.performance;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalItemsDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.api.remote.performance.RemotePerformanceAppraisalService;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceAppraisalItemsService;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceAppraisalObjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @author TANGMICHI
 * @since 2022-10-08
 */
@RestController
@RequestMapping("performanceAppraisal")
public class RemotePerformanceAppraisal implements RemotePerformanceAppraisalService {

    @Autowired
    private IPerformanceAppraisalObjectsService performanceAppraisalObjectsService;

    @Autowired
    private IPerformanceAppraisalItemsService performanceAppraisalItemsService;

    @Override
    @InnerAuth
    @GetMapping("/queryEmployeeResult")
    public R<List<PerformanceAppraisalObjectsDTO>> performanceResult(@RequestParam("performanceObjectId") Long performanceObjectId, String source) {
        return R.ok(performanceAppraisalObjectsService.performanceResult(performanceObjectId));
    }

    @Override
    @InnerAuth
    @GetMapping("/queryQuoteEmployeeById")
    public R<List<PerformanceAppraisalObjectsDTO>> queryQuoteEmployeeById(@RequestParam("employeeId") Long employeeId, String source) {
        return R.ok(performanceAppraisalObjectsService.queryQuoteEmployeeById(employeeId));
    }

    /**
     * 根据指标ID集合查询绩效
     *
     * @param map 指标ID集合
     * @return List
     */
    @Override
    @InnerAuth
    @PostMapping("/selectByIndicatorIds")
    public R<List<PerformanceAppraisalItemsDTO>> selectByIndicatorIds(@RequestBody Map<Integer, List<Long>> map, String source) {
        return R.ok(performanceAppraisalItemsService.selectByIndicatorIds(map));
    }

    /**
     * 根据部门ID集合查询绩效
     *
     * @param departmentIds 部门ID集合
     * @return List
     */
    @Override
    @InnerAuth
    @PostMapping("/selectByDepartmentIds")
    public R<List<PerformanceAppraisalObjectsDTO>> selectByDepartmentIds(@RequestBody List<Long> departmentIds, String source) {
        return R.ok(performanceAppraisalObjectsService.selectByDepartmentIds(departmentIds));
    }

    /**
     *
     * @param employeeIds 人员ID集合
     * @return R
     */
    @Override
    @InnerAuth
    @PostMapping("/queryQuoteEmployeeByIds")
    public R<List<PerformanceAppraisalObjectsDTO>> queryQuoteEmployeeByIds(@RequestBody List<Long> employeeIds, String source) {
        return R.ok(performanceAppraisalObjectsService.queryQuoteEmployeeByIds(employeeIds));
    }

}
