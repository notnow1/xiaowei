package net.qixiaowei.operate.cloud.api.remote.salary;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustPlanDTO;
import net.qixiaowei.operate.cloud.api.factory.salary.RemoteSalaryAdjustPlanFallbackFactory;
import net.qixiaowei.operate.cloud.api.factory.salary.RemoteSalaryItemFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(contextId = "remoteSalaryAdjustPlanService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteSalaryAdjustPlanFallbackFactory.class)
public interface RemoteSalaryAdjustPlanService {

    String API_PREFIX_SALARY_ITEM = "/salaryAdjustPlan";

    /**
     * 初始化工资项
     */
    @PostMapping(API_PREFIX_SALARY_ITEM + "/selectByEmployeeId")
    R<List<EmpSalaryAdjustPlanDTO>> selectByEmployeeId(@RequestParam("employeeId") Long employeeId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}