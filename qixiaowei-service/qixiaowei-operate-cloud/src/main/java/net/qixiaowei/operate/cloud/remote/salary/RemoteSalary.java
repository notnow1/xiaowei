package net.qixiaowei.operate.cloud.remote.salary;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDTO;
import net.qixiaowei.operate.cloud.api.remote.salary.RemoteSalaryItemService;
import net.qixiaowei.operate.cloud.service.salary.ISalaryItemService;
import net.qixiaowei.operate.cloud.service.salary.ISalaryPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @description: 工资条远程实现类
 * @Author: hzk
 * @date: 2022/12/12 20:12
 **/
@RestController
@RequestMapping("/salaryItem")
public class RemoteSalary implements RemoteSalaryItemService {

    @Autowired
    private ISalaryItemService salaryItemService;

    @Autowired
    private ISalaryPayService salaryPayService;


    @Override
    @InnerAuth
    @PostMapping("/initSalaryItem")
    public R<Boolean> initSalaryItem(String source) {
        return R.ok(salaryItemService.initSalaryItem());
    }

    /**
     * 根据员工ID查询工资条
     *
     * @param employeeId 员工ID
     * @return R
     */
    @Override
    @InnerAuth
    @GetMapping("/selectByEmployeeId")
    public R<List<SalaryPayDTO>> selectByEmployeeId(@RequestParam("employeeId") Long employeeId, String source) {
        return R.ok(salaryPayService.selectByEmployeeId(employeeId));
    }
}
