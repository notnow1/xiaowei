package net.qixiaowei.operate.cloud.remote.salary;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptSalaryAdjustItemDTO;
import net.qixiaowei.operate.cloud.api.remote.salary.RemoteDeptSalaryAdjustPlanService;
import net.qixiaowei.operate.cloud.mapper.salary.DeptSalaryAdjustItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @description: 部门调薪
 * @Author: Graves
 * @date: 2022/12/12 20:12
 **/
@RestController
@RequestMapping("/deptSalaryAdjustPlan")
public class RemoteDeptSalaryAdjustPlan implements RemoteDeptSalaryAdjustPlanService {

    @Autowired
    private DeptSalaryAdjustItemMapper deptSalaryAdjustItemMapper;

    /**
     * 根据部门ID集合查询部门调薪
     *
     * @param departmentId 部门ID集合
     * @return R
     */
    @Override
    @InnerAuth
    @GetMapping("/selectByDepartmentId")
    public R<List<DeptSalaryAdjustItemDTO>> selectByDepartmentId(@RequestParam("departmentId") Long departmentId, String source) {
        return R.ok(deptSalaryAdjustItemMapper.selectByDepartmentId(departmentId));
    }
}
