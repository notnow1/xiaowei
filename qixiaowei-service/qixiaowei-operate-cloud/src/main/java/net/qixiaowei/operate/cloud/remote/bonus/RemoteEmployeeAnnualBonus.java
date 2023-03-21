package net.qixiaowei.operate.cloud.remote.bonus;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.domain.bonus.EmployeeAnnualBonus;
import net.qixiaowei.operate.cloud.api.remote.bonus.RemoteEmployeeAnnualBonusService;
import net.qixiaowei.operate.cloud.service.bonus.IEmployeeAnnualBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author TMICHI
 * @Date 2022-11-17
 **/
@RestController
@RequestMapping("/employeeAnnualBonus/remote")
public class RemoteEmployeeAnnualBonus implements RemoteEmployeeAnnualBonusService {

    @Autowired
    private IEmployeeAnnualBonusService employeeAnnualBonusService;

    /**
     * 根据人员id查询个人年终奖 申请人id
     * @param employeeId
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @GetMapping("/queryEmployeeIdEmployeeAnnualBonus")
    public R<List<EmployeeAnnualBonus>> selectEmployeeAnnualBonusByEmployeeId(@RequestParam("employeeId")Long employeeId, String source) {
        return R.ok(employeeAnnualBonusService.selectEmployeeAnnualBonusByEmployeeId(employeeId));
    }

    /**
     * 根据部门id查询个人年终奖 (一级部门,申请部门)
     * @param departmentIds
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/queryDepartmentIdEmployeeAnnualBonus")
    public R<List<EmployeeAnnualBonus>> selectEmployeeAnnualBonusByDepartmentIds(@RequestBody  List<Long> departmentIds, String source) {
        return R.ok(employeeAnnualBonusService.selectEmployeeAnnualBonusByDepartmentIds(departmentIds));
    }
}
