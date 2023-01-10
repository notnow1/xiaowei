package net.qixiaowei.operate.cloud.remote.bonus;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayObjectsDTO;
import net.qixiaowei.operate.cloud.api.remote.bonus.RemoteBonusBudgetService;
import net.qixiaowei.operate.cloud.service.bonus.IBonusBudgetService;
import net.qixiaowei.operate.cloud.service.bonus.IBonusPayApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author TMICHI
 * @Date 2022-11-17
 **/
@RestController
@RequestMapping("/bonusBudget/remote")
public class RemoteBonusBudget implements RemoteBonusBudgetService {

    @Autowired
    private IBonusBudgetService bonusBudgetService;


    /**
     * 远程查询总奖金预算
     * @param bonusBudgetDTO
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/queryIndicatorIdBonusBudget")
    public R<BonusBudgetDTO> selectBonusBudgetByIndicatorId(@RequestBody BonusBudgetDTO bonusBudgetDTO, String source) {
        return R.ok(bonusBudgetService.selectBonusBudgetByIndicatorId(bonusBudgetDTO));
    }
}
