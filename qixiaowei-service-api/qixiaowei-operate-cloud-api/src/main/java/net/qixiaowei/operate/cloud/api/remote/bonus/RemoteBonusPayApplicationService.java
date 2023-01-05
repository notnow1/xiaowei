package net.qixiaowei.operate.cloud.api.remote.bonus;

import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.operate.cloud.api.factory.bonus.RemoteBonusPayApplicationFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 奖金发放申请服务
 */
@FeignClient(contextId = "remoteBonusPayApplicationService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteBonusPayApplicationFallbackFactory.class)
public interface RemoteBonusPayApplicationService {
    String API_PREFIX_PERFORMANCE_APPRAISAL = "/bonusPayApplication";

}