package net.qixiaowei.operate.cloud.remote.salary;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustPlanDTO;
import net.qixiaowei.operate.cloud.api.remote.salary.RemoteSalaryAdjustPlanService;
import net.qixiaowei.operate.cloud.api.remote.salary.RemoteSalaryItemService;
import net.qixiaowei.operate.cloud.service.salary.IEmpSalaryAdjustPlanService;
import net.qixiaowei.operate.cloud.service.salary.ISalaryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @description: 个人调薪远程实现类
 * @Author: Graves
 * @date: 2022/12/12 20:12
 **/
@RestController
@RequestMapping("/salaryAdjustPlan")
public class RemoteSalaryAdjustPlan implements RemoteSalaryAdjustPlanService {

    @Autowired
    private IEmpSalaryAdjustPlanService empSalaryAdjustPlanService;


    @Override
    @InnerAuth
    @GetMapping("/selectByEmployeeId")
    public R<List<EmpSalaryAdjustPlanDTO>> selectByEmployeeId(@RequestParam("employeeId") Long employeeId, String source) {
        return R.ok(empSalaryAdjustPlanService.selectByEmployeeId(employeeId));
    }
}
