package net.qixiaowei.operate.cloud.remote.salary;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustPlanDTO;
import net.qixiaowei.operate.cloud.api.remote.salary.RemoteSalaryAdjustPlanService;
import net.qixiaowei.operate.cloud.service.salary.IEmpSalaryAdjustPlanService;
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

    @Override
    @InnerAuth
    @PostMapping("/selectByEmployeeIds")
    public R<List<EmpSalaryAdjustPlanDTO>> selectByEmployeeIds(@RequestBody List<Long> employeeIds, String source) {
        return R.ok(empSalaryAdjustPlanService.selectByEmployeeIds(employeeIds));
    }

    /**
     * 根据职级体系ID集合获取个人调薪
     *
     * @param officialRankSystemIds 职级体系ID集合
     * @return List
     */
    @Override
    @InnerAuth
    @PostMapping("/selectBySystemIds")
    public R<List<EmpSalaryAdjustPlanDTO>> selectBySystemIds(@RequestBody List<Long> officialRankSystemIds, String source) {
        return R.ok(empSalaryAdjustPlanService.selectBySystemIds(officialRankSystemIds));
    }

    @Override
    @InnerAuth
    @GetMapping("/selectByDepartmentId")
    public R<List<EmpSalaryAdjustPlanDTO>> selectByDepartmentId(@RequestParam("departmentId") Long departmentId, String source) {
        return R.ok(empSalaryAdjustPlanService.selectByDepartmentId(departmentId));
    }

    /**
     * 根据岗位ID集合获取个人调薪
     *
     * @param postId
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @GetMapping("/selectByPostId")
    public R<List<EmpSalaryAdjustPlanDTO>> selectByPostId(@RequestParam("postId") Long postId, String source) {
        return R.ok(empSalaryAdjustPlanService.selectByPostId(postId));
    }

    /**
     * 个人调薪到达生效日期更新员工信息
     *
     * @param inner 内部
     * @return R
     */
    @Override
    @InnerAuth
    @GetMapping("/empAdjustUpdate")
    public R<Integer> empAdjustUpdate(String source) {
        empSalaryAdjustPlanService.empAdjustUpdate();
        return R.ok();
    }
}
