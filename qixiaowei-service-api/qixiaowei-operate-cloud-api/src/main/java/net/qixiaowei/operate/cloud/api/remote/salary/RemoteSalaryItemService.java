package net.qixiaowei.operate.cloud.api.remote.salary;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDTO;
import net.qixiaowei.operate.cloud.api.factory.salary.RemoteSalaryItemFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(contextId = "remoteSalaryItemService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteSalaryItemFallbackFactory.class)
public interface RemoteSalaryItemService {

    String API_PREFIX_SALARY_ITEM = "/salaryItem";

    /**
     * 根据员工ID查询工资条
     *
     * @param employeeId 员工ID
     * @return R
     */
    @GetMapping(API_PREFIX_SALARY_ITEM + "/selectByEmployeeId")
    R<List<SalaryPayDTO>> selectByEmployeeId(@RequestParam("employeeId") Long employeeId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}