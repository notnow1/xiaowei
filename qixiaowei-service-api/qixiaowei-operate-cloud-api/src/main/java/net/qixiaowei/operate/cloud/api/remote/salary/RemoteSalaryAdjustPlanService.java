package net.qixiaowei.operate.cloud.api.remote.salary;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustPlanDTO;
import net.qixiaowei.operate.cloud.api.factory.salary.RemoteSalaryAdjustPlanFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(contextId = "remoteSalaryAdjustPlanService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteSalaryAdjustPlanFallbackFactory.class)
public interface RemoteSalaryAdjustPlanService {

    String API_PREFIX_SALARY_ITEM = "/salaryAdjustPlan";

    /**
     * 根据Id查询个人调薪
     */
    @GetMapping(API_PREFIX_SALARY_ITEM + "/selectByEmployeeId")
    R<List<EmpSalaryAdjustPlanDTO>> selectByEmployeeId(@RequestParam("employeeId") Long employeeId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据Id集合查询个人调薪
     */
    @PostMapping(API_PREFIX_SALARY_ITEM + "/selectByEmployeeIds")
    R<List<EmpSalaryAdjustPlanDTO>> selectByEmployeeIds(@RequestBody List<Long> employeeIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据职级体系ID集合获取个人调薪
     */
    @PostMapping(API_PREFIX_SALARY_ITEM + "/selectBySystemIds")
    R<List<EmpSalaryAdjustPlanDTO>> selectBySystemIds(@RequestBody List<Long> officialRankSystemIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据部门ID集合获取个人调薪
     */
    @PostMapping(API_PREFIX_SALARY_ITEM + "/selectByDepartmentId")
    R<List<EmpSalaryAdjustPlanDTO>> selectByDepartmentId(@RequestParam("departmentId") Long officialRankSystemIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}