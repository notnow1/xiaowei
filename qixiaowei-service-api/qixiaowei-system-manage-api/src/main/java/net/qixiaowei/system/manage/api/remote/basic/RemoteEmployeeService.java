package net.qixiaowei.system.manage.api.remote.basic;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.factory.basic.RemoteEmployeeFallbackFactory;
import net.qixiaowei.system.manage.api.vo.basic.EmployeeSalaryPlanVO;
import net.qixiaowei.system.manage.api.vo.basic.EmployeeSalarySnapVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 人员服务
 */
@FeignClient(contextId = "remoteEmployeeService", value = ServiceNameConstants.SYSTEM_MANAGE_SERVICE, fallbackFactory = RemoteEmployeeFallbackFactory.class)
public interface RemoteEmployeeService {

    String API_PREFIX_EMPLOYEE = "/employee";

    /**
     * 通过Code集合查找人员列表
     *
     * @param employeeCodes
     * @return 结果
     */
    @PostMapping(API_PREFIX_EMPLOYEE + "/codeList")
    R<List<EmployeeDTO>> selectCodeList(@RequestBody List<String> employeeCodes, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 远程查询人员数据
     *
     * @param employeeDTO
     * @param source
     * @return
     */
    @PostMapping(API_PREFIX_EMPLOYEE + "/remoteList")
    R<List<EmployeeDTO>> selectRemoteList(@RequestBody EmployeeDTO employeeDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 远程查询用户数据
     *
     * @param employeeDTO
     * @param source
     * @return
     */
    @PostMapping(API_PREFIX_EMPLOYEE + "/selectUserByEmployeeName")
    R<List<EmployeeDTO>> selectUserByEmployeeName(@RequestBody EmployeeDTO employeeDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过id查找人员
     *
     * @param employeeId
     * @return 结果
     */
    @GetMapping(API_PREFIX_EMPLOYEE + "/employeeId")
    R<EmployeeDTO> selectByEmployeeId(@RequestParam("employeeId") Long employeeId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过id集合查找人员列表
     *
     * @param employeeIds
     * @return 结果
     */
    @PostMapping(API_PREFIX_EMPLOYEE + "/employeeIds")
    R<List<EmployeeDTO>> selectByEmployeeIds(@RequestBody List<Long> employeeIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据部门 职级 获取人员信息集合
     *
     * @param list
     * @return 结果
     */
    @PostMapping(API_PREFIX_EMPLOYEE + "/selectByBudgeList")
    R<List<EmployeeDTO>> selectByBudgeList(@RequestBody List<List<Long>> list, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据Code集合
     *
     * @param assessmentList
     * @return 结果
     */
    @PostMapping(API_PREFIX_EMPLOYEE + "/selectByCodes")
    R<List<EmployeeDTO>> selectByCodes(@RequestBody List<String> assessmentList, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据Name集合
     *
     * @param employeeNames 员工名称
     * @return 结果
     */
    @PostMapping(API_PREFIX_EMPLOYEE + "/selectByNames")
    R<List<EmployeeDTO>> selectByNames(@RequestBody List<String> employeeNames, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 相同部门下 相同职级的 在职人数
     *
     * @param source
     * @return
     */
    @PostMapping(API_PREFIX_EMPLOYEE + "/departmentAndOfficialRankSystem")
    R<List<EmployeeDTO>> selectDepartmentAndOfficialRankSystem(@RequestBody List<Long> departmentIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过部门，岗位，职级集合查询员工表
     *
     * @param idMaps id集合
     * @param source
     * @return
     */
    @PostMapping(API_PREFIX_EMPLOYEE + "/selectEmployeeByPDRIds")
    R<List<EmployeeDTO>> selectEmployeeByPDRIds(@RequestBody Map<String, List<String>> idMaps, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 查询部门下所有人员
     *
     * @param departmentId
     * @param source
     * @return
     */
    @GetMapping(API_PREFIX_EMPLOYEE + "/selectEmployeeByDepts")
    R<List<EmployeeDTO>> selectEmployeeByDepts(@RequestParam("departmentId") Long departmentId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 查询一级部门下所有的人员 返回部门id和职级体系id
     *
     * @param departmentIdAll
     * @return
     */
    @PostMapping(API_PREFIX_EMPLOYEE + "/selectEmployeeByParPDRIds")
    R<List<EmployeeDTO>> selectParentDepartmentIdAndOfficialRankSystem(@RequestBody List<Long> departmentIdAll, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 查询在职所有人员
     *
     * @param source
     * @return
     */
    @GetMapping(API_PREFIX_EMPLOYEE + "/getAll")
    R<List<EmployeeDTO>> getAll(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据部门ID 集合查询人员
     *
     * @param departmentIds ID 集合
     * @return
     */
    @PostMapping(API_PREFIX_EMPLOYEE + "/selectEmployeeByDepartmentIds")
    R<List<EmployeeDTO>> selectEmployeeByDepartmentIds(@RequestBody List<Long> departmentIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 远程个人调薪计划员工信息
     *
     * @param employeeDTO 员工信息
     * @return R
     */
    @PostMapping(API_PREFIX_EMPLOYEE + "/empSalaryAdjustPlan")
    R<EmployeeSalaryPlanVO> empSalaryAdjustPlan(@RequestBody EmployeeDTO employeeDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据调整策略进行更新人员薪资，岗位，职级
     *
     * @param employeeSalarySnapVO 计划VO
     * @return R
     */
    @PostMapping(API_PREFIX_EMPLOYEE + "/empAdjustUpdate")
    R<Integer> empAdjustUpdate(@RequestBody EmployeeSalarySnapVO employeeSalarySnapVO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据调整策略进行更新人员薪资，岗位，职级
     *
     * @param employeeSalarySnapVOS 计划VO集合
     * @return R
     */
    @PostMapping(API_PREFIX_EMPLOYEE + "/empAdjustUpdates")
    R<Integer> empAdjustUpdates(@RequestBody List<EmployeeSalarySnapVO> employeeSalarySnapVOS, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 人员远程高级搜索
     *
     * @param params 请求参数
     * @return int
     */
    @PostMapping(API_PREFIX_EMPLOYEE + "/empAdvancedSearch")
    R<List<EmployeeDTO>> empAdvancedSearch(@RequestBody Map<String, Object> params, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
