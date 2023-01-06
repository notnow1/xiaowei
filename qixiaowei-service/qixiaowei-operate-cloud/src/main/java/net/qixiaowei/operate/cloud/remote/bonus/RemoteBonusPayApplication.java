package net.qixiaowei.operate.cloud.remote.bonus;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.domain.bonus.BonusPayObjects;
import net.qixiaowei.operate.cloud.api.domain.bonus.EmployeeAnnualBonus;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayObjectsDTO;
import net.qixiaowei.operate.cloud.api.remote.bonus.RemoteBonusPayApplicationService;
import net.qixiaowei.operate.cloud.api.remote.bonus.RemoteEmployeeAnnualBonusService;
import net.qixiaowei.operate.cloud.service.bonus.IBonusPayApplicationService;
import net.qixiaowei.operate.cloud.service.bonus.IEmployeeAnnualBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author TMICHI
 * @Date 2022-11-17
 **/
@RestController
@RequestMapping("/bonusPayApplication/remote")
public class RemoteBonusPayApplication implements RemoteBonusPayApplicationService {

    @Autowired
    private IBonusPayApplicationService bonusPayApplicationService;

    /**
     * 根据人员id查询个人年终奖 奖金发放对象ID(员工id)
     * @param employeeId
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @GetMapping("/queryBonusPayApplication")
    public R<List<BonusPayObjectsDTO>> selectBonusPayApplicationByEmployeeId(@RequestParam("employeeId")Long employeeId, String source) {
        return R.ok(bonusPayApplicationService.selectBonusPayApplicationByEmployeeId(employeeId));
    }
}
