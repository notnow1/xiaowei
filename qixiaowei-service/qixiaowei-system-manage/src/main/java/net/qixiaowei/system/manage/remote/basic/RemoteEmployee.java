package net.qixiaowei.system.manage.remote.basic;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.vo.basic.EmployeeSalaryPlanVO;
import net.qixiaowei.system.manage.api.vo.basic.EmployeeSalarySnapVO;
import net.qixiaowei.system.manage.service.basic.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee")
public class RemoteEmployee implements RemoteEmployeeService {
    @Autowired
    private IEmployeeService employeeService;

    /**
     * 根据code集合查询人员数据
     *
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
     * 远程查询人员Excel下拉框数据
     * @param employeeDTO
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/dropEmployeeList")
    public R<List<EmployeeDTO>> selectDropEmployeeList(@RequestBody EmployeeDTO employeeDTO, String source) {
        return R.ok(employeeService.selectDropEmployeeList(employeeDTO));
    }

    /**
     * 远程查询用户数据
     *
     * @param employeeDTO 员工DTO
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/selectUserByEmployeeName")
    public R<List<EmployeeDTO>> selectUserByEmployeeName(@RequestBody EmployeeDTO employeeDTO, String source) {
        return R.ok(employeeService.selectUserList(employeeDTO));
    }

    /**
     * 通过id查找人员
     *
     * @param employeeId
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @GetMapping("/employeeId")
    public R<EmployeeDTO> selectByEmployeeId(@RequestParam("employeeId") Long employeeId, String source) {
        return R.ok(employeeService.selectEmployeeByEmployeeId(employeeId));
    }

    /**
     * 通过id集合查找人员列表
     *
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
     *
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

    /**
     * 根据Code集合
     *
     * @param assessmentList
     * @return 结果
     */
    @Override
    @InnerAuth
    @PostMapping("/selectByCodes")
    public R<List<EmployeeDTO>> selectByCodes(@RequestBody List<String> assessmentList, String source) {
        return R.ok(employeeService.selectByCodes(assessmentList));
    }

    /**
     * 根据Name集合
     *
     * @param employeeNames 员工名称
     * @return 结果
     */
    @Override
    @InnerAuth
    @PostMapping("/selectByNames")
    public R<List<EmployeeDTO>> selectByNames(@RequestBody List<String> employeeNames, String source) {
        return R.ok(employeeService.selectByNames(employeeNames));
    }

    /**
     * 相同部门下 相同职级的 在职人数
     *
     * @param departmentIds
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/departmentAndOfficialRankSystem")
    public R<List<EmployeeDTO>> selectDepartmentAndOfficialRankSystem(@RequestBody List<Long> departmentIds, String source) {
        return R.ok(employeeService.selectDepartmentAndOfficialRankSystem(departmentIds));
    }

    /**
     * 通过部门，岗位，职级集合查询员工表
     *
     * @param idMaps id集合
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/selectEmployeeByPDRIds")
    public R<List<EmployeeDTO>> selectEmployeeByPDRIds(@RequestBody Map<String, List<String>> idMaps, String source) {
        return R.ok(employeeService.selectEmployeeByPDRIds(idMaps));
    }

    /**
     * 查询部门下所有人员
     *
     * @param departmentId
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @GetMapping("/selectEmployeeByDepts")
    public R<List<EmployeeDTO>> selectEmployeeByDepts(@RequestParam("departmentId") Long departmentId, String source) {
        return R.ok(employeeService.selectEmployeeByDepts(departmentId));
    }

    /**
     * 查询一级部门下所有的人员 返回部门id和职级体系id
     *
     * @param departmentIdAll
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/selectEmployeeByParPDRIds")
    public R<List<EmployeeDTO>> selectParentDepartmentIdAndOfficialRankSystem(@RequestBody List<Long> departmentIdAll, String source) {
        return R.ok(employeeService.selectParentDepartmentIdAndOfficialRankSystem(departmentIdAll));
    }

    /**
     * 查询生效所有人员
     *
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @GetMapping("/getAll")
    public R<List<EmployeeDTO>> getAll(String source) {
        return R.ok(employeeService.getAll());
    }

    /**
     * 根据部门ID 集合查询人员
     *
     * @param departmentIds ID 集合
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/selectEmployeeByDepartmentIds")
    public R<List<EmployeeDTO>> selectEmployeeByDepartmentIds(@RequestBody List<Long> departmentIds, String source) {
        return R.ok(employeeService.selectEmployeeByDepartmentIds(departmentIds));
    }

    /**
     * 远程个人调薪计划员工信息
     *
     * @param employeeDTO 员工信息
     * @param source      根源
     * @return R
     */
    @Override
    @InnerAuth
    @PostMapping("/empSalaryAdjustPlan")
    public R<EmployeeSalaryPlanVO> empSalaryAdjustPlan(EmployeeDTO employeeDTO, String source) {
        return R.ok(employeeService.empSalaryAdjustPlan(employeeDTO));
    }

    /**
     * 根据调整策略进行更新人员薪资，岗位，职级
     *
     * @param employeeSalarySnapVO 计划VO
     * @return R
     */
    @Override
    @PostMapping("/empAdjustUpdate")
    public R<Integer> empAdjustUpdate(@RequestBody EmployeeSalarySnapVO employeeSalarySnapVO, String source) {
        return R.ok(employeeService.empAdjustUpdate(employeeSalarySnapVO));
    }

    /**
     * 根据调整策略进行更新人员薪资，岗位，职级
     *
     * @param employeeSalarySnapVOS 计划VO集合
     * @return int
     */
    @Override
    @PostMapping("/empAdjustUpdates")
    public R<Integer> empAdjustUpdates(@RequestBody List<EmployeeSalarySnapVO> employeeSalarySnapVOS, String source) {
        return R.ok(employeeService.empAdjustUpdates(employeeSalarySnapVOS));
    }

    /**
     * 人员远程高级搜索
     *
     * @param params 请求参数
     * @return int
     */
    @Override
    @PostMapping("/empAdvancedSearch")
    public R<List<EmployeeDTO>> empAdvancedSearch(@RequestBody Map<String, Object> params, String source) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        if (StringUtils.isNotNull(params)) {
            employeeDTO.setParams(params);
        }
        return R.ok(employeeService.selectEmployeeList(employeeDTO));
    }
}
