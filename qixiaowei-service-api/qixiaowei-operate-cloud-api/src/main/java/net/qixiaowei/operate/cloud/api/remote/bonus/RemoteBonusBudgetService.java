package net.qixiaowei.operate.cloud.api.remote.bonus;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayObjectsDTO;
import net.qixiaowei.operate.cloud.api.factory.bonus.RemoteBonusBudgetFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 总奖金预算服务
 */
@FeignClient(contextId = "remoteBonusBudgetService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteBonusBudgetFallbackFactory.class)
public interface RemoteBonusBudgetService {
    String API_PREFIX_PERFORMANCE_APPRAISAL = "/bonusBudget/remote";
    /**
     * 根据人员id查询个人年终奖 奖金发放对象ID(员工id)
     */
    @PostMapping(API_PREFIX_PERFORMANCE_APPRAISAL + "/queryIndicatorIdBonusBudget")
    R<BonusBudgetDTO> selectBonusBudgetByIndicatorId(@RequestBody BonusBudgetDTO bonusBudgetDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}