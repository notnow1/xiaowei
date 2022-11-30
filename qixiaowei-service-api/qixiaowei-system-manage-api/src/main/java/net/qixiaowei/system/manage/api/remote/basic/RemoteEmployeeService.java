package net.qixiaowei.system.manage.api.remote.basic;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.factory.basic.RemoteEmployeeFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 相同部门下 相同职级的 在职人数
     *
     * @param source
     * @return
     */
    @PostMapping(API_PREFIX_EMPLOYEE + "/departmentAndOfficialRankSystem")
    R<List<EmployeeDTO>> selectDepartmentAndOfficialRankSystem(@RequestBody List<Long> departmentIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
