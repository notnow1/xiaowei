package net.qixiaowei.operate.cloud.api.factory.bonus;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusBudgetDTO;
import net.qixiaowei.operate.cloud.api.remote.bonus.RemoteBonusBudgetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * 总奖金预算服务降级处理
 */
public class RemoteBonusBudgetFallbackFactory implements FallbackFactory<RemoteBonusBudgetService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteBonusBudgetFallbackFactory.class);

    @Override
    public RemoteBonusBudgetService create(Throwable throwable) {
        log.error("奖金发放申请服务调用失败:{}", throwable.getMessage());
        return new RemoteBonusBudgetService() {

            @Override
            public R<BonusBudgetDTO> selectBonusBudgetByIndicatorId(BonusBudgetDTO bonusBudgetDTO, String source) {
                return R.fail("根据人员id查询奖金发放申请失败:" + throwable.getMessage());
            }
        };
    }
}
