package net.qixiaowei.system.manage.api.factory.basic;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.vo.basic.EmployeeSalaryPlanVO;
import net.qixiaowei.system.manage.api.vo.basic.EmployeeSalarySnapVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 人员服务降级处理
 */
@Component
public class RemoteEmployeeFallbackFactory implements FallbackFactory<RemoteEmployeeService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteEmployeeFallbackFactory.class);

    @Override
    public RemoteEmployeeService create(Throwable throwable) {
        log.error("人员服务调用失败:{}", throwable.getMessage());
        return new RemoteEmployeeService() {

            @Override
            public R<List<EmployeeDTO>> selectCodeList(List<String> employeeCodes, String source) {
                return R.fail("获取根据code集合人员信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmployeeDTO>> selectRemoteList(EmployeeDTO employeeDTO, String source) {
                return R.fail("获取根据code集合人员信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmployeeDTO>> selectUserByEmployeeName(EmployeeDTO employeeDTO, String source) {
                return R.fail("根据员工名称获取用户信息:" + throwable.getMessage());
            }

            @Override
            public R<EmployeeDTO> selectByEmployeeId(Long employeeId, String source) {
                return R.fail("根据id获取人员信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmployeeDTO>> selectByEmployeeIds(List<Long> employeeIds, String source) {
                return R.fail("根据id集合获取人员信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmployeeDTO>> selectByBudgeList(List<List<Long>> list, String source) {
                return R.fail("根据部门 职级 获取人员信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmployeeDTO>> selectByCodes(List<String> assessmentList, String source) {
                return R.fail("根据部门 职级 获取人员信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmployeeDTO>> selectDepartmentAndOfficialRankSystem(List<Long> departmentIds, String source) {
                return R.fail("获取相同部门下 相同职级的 在职人数信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmployeeDTO>> selectEmployeeByPDRIds(Map<String, List<String>> idMaps, String source) {
                return R.fail("通过部门，岗位，职级集合查询员工信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmployeeDTO>> selectEmployeeByDepts(Long departmentId, String source) {
                return R.fail("通过部门id查询所有员工信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmployeeDTO>> selectParentDepartmentIdAndOfficialRankSystem(List<Long> departmentIdAll, String source) {
                return R.fail("通过部门id集合查询一级部门及其下级部门所有员工信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmployeeDTO>> getAll(String source) {
                return R.fail("查询所有在职员工信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmployeeDTO>> selectEmployeeByDepartmentIds(List<Long> departmentIds, String source) {
                return R.fail("根据部门ID 集合查询人员失败:" + throwable.getMessage());
            }

            @Override
            public R<EmployeeSalaryPlanVO> empSalaryAdjustPlan(EmployeeDTO employeeDTO, String source) {
                return R.fail("远程个人调薪计划员工信息 集合查询人员失败:" + throwable.getMessage());
            }

            @Override
            public R<Integer> empAdjustUpdate(EmployeeSalarySnapVO employeeSalaryPlanVO, String source) {
                return R.fail("远程更新员工薪资，岗位，职级信息 集合查询人员失败:" + throwable.getMessage());
            }

            @Override
            public R<Integer> empAdjustUpdates(List<EmployeeSalarySnapVO> employeeSalarySnapVOS, String source) {
                return R.fail("远程更新员工薪资，岗位，职级信息 集合查询人员失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmployeeDTO>> empAdvancedSearch(Map<String, Object> params, String source) {
                return R.fail("人员远程高级搜索失败:" + throwable.getMessage());
            }
        };
    }
}
