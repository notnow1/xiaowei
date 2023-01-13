package net.qixiaowei.operate.cloud.api.remote.salary;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptSalaryAdjustItemDTO;
import net.qixiaowei.operate.cloud.api.factory.salary.RemoteDeptSalaryAdjustPlanFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门调薪
 */
@FeignClient(contextId = "remoteDeptSalaryAdjustPlanService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteDeptSalaryAdjustPlanFactory.class)
public interface RemoteDeptSalaryAdjustPlanService {

    String API_PREFIX_SALARY_ITEM = "/deptSalaryAdjustPlan";

    /**
     * 根据部门ID集合查询部门调薪
     */
    @GetMapping(API_PREFIX_SALARY_ITEM + "/selectByDepartmentId")
    R<List<DeptSalaryAdjustItemDTO>> selectByDepartmentId(@RequestParam("departmentId") Long departmentId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}