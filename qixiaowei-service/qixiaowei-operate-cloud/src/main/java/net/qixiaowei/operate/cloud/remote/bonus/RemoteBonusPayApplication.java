package net.qixiaowei.operate.cloud.remote.bonus;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayApplicationDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayObjectsDTO;
import net.qixiaowei.operate.cloud.api.remote.bonus.RemoteBonusPayApplicationService;
import net.qixiaowei.operate.cloud.service.bonus.IBonusPayApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * @param employeeIds
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/queryEmployeeIdBonusPayApplication")
    public R<List<BonusPayObjectsDTO>> selectBonusPayApplicationByEmployeeIds(@RequestBody List<Long> employeeIds, String source) {
        return R.ok(bonusPayApplicationService.selectBonusPayApplicationByEmployeeIds(employeeIds));
    }

    /**
     * 根据部门id查询个人年终奖 (申请部门,预算部门,获奖部门)
     * @param departmentIds
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/queryDepartmentIdBonusPayApplication")
    public R<List<BonusPayApplicationDTO>> selectBonusPayApplicationByDepartmentIds(@RequestBody List<Long> departmentIds, String source) {
        return R.ok(bonusPayApplicationService.selectBonusPayApplicationByDepartmentIds(departmentIds));
    }
}
