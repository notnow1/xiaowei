package net.qixiaowei.operate.cloud.api.remote.bonus;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayApplicationDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayObjectsDTO;
import net.qixiaowei.operate.cloud.api.factory.bonus.RemoteBonusPayApplicationFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 奖金发放申请服务
 */
@FeignClient(contextId = "remoteBonusPayApplicationService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteBonusPayApplicationFallbackFactory.class)
public interface RemoteBonusPayApplicationService {
    String API_PREFIX_PERFORMANCE_APPRAISAL = "/bonusPayApplication/remote";
    /**
     * 根据人员id查询个人年终奖 奖金发放对象ID(员工id)
     */
    @GetMapping(API_PREFIX_PERFORMANCE_APPRAISAL + "/queryEmployeeIdBonusPayApplication")
    R<List<BonusPayObjectsDTO>> selectBonusPayApplicationByEmployeeId(@RequestParam("employeeId") Long employeeId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据部门id查询个人年终奖 (申请部门,预算部门,获奖部门)
     * @param departmentIds
     * @param source
     * @return
     */
    @PostMapping(API_PREFIX_PERFORMANCE_APPRAISAL + "/queryDepartmentIdBonusPayApplication")
    R<List<BonusPayApplicationDTO>> selectBonusPayApplicationByDepartmentIds(@RequestBody List<Long> departmentIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}