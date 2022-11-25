package net.qixiaowei.system.manage.remote.basic;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.service.basic.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class RemoteEmployee implements RemoteEmployeeService {
    @Autowired
    private IEmployeeService employeeService;

    /**
     * 根据code集合查询人员数据
     * @param employeeCodes
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/codeList")
    public R<List<EmployeeDTO>> selectCodeList(@RequestBody List<String> employeeCodes, String source) {
        return R.ok(employeeService.selectCodeList(employeeCodes));
    }

    /**
     * 远程查询人员数据
     *
     * @param employeeDTO
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/remoteList")
    public R<List<EmployeeDTO>> selectRemoteList(@RequestBody EmployeeDTO employeeDTO, String source) {
        return R.ok(employeeService.selectEmployeeList(employeeDTO));
    }

    /**
     * 通过id查找人员
     * @param employeeId
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @GetMapping("/employeeId")
    public R<EmployeeDTO> selectByEmployeeId(@RequestParam("employeeId")Long employeeId, String source) {
        return R.ok(employeeService.selectEmployeeByEmployeeId(employeeId));
    }

    /**
     * 通过id集合查找人员列表
     * @param employeeIds
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/employeeIds")
    public R<List<EmployeeDTO>> selectByEmployeeIds(@RequestBody List<Long> employeeIds, String source) {
        return R.ok(employeeService.selectEmployeeByEmployeeIds(employeeIds));
    }

    /**
     * 根据部门 职级 获取人员信息集合
     * @param list
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/selectByBudgeList")
    public R<List<EmployeeDTO>> selectByBudgeList(@RequestBody List<List<Long>> list, String source) {
        return R.ok(employeeService.selectByBudgeList(list));
    }
}
