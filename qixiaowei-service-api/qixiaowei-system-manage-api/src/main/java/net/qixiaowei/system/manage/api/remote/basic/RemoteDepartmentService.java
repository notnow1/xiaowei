package net.qixiaowei.system.manage.api.remote.basic;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.factory.basic.RemoteDepartmentFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门服务
 */
@FeignClient(contextId = "remoteDepartmentService", value = ServiceNameConstants.SYSTEM_MANAGE_SERVICE, fallbackFactory = RemoteDepartmentFallbackFactory.class)
public interface RemoteDepartmentService {
    String API_PREFIX_DEPARTMEN = "/department";

    /**
     * 通过Code查找部门列表
     *
     * @param departmentCodes
     * @param source
     * @return
     */
    @PostMapping(API_PREFIX_DEPARTMEN + "/codeList")
    R<List<DepartmentDTO>> selectCodeList(@RequestBody List<String> departmentCodes, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过id查找部门信息
     *
     * @param departmentId
     * @param source
     * @return
     */
    @GetMapping(API_PREFIX_DEPARTMEN + "/departmentId")
    R<DepartmentDTO> selectdepartmentId(@RequestParam("departmentId") Long departmentId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过id集合查找部门信息
     *
     * @param departmentIds
     * @param source
     * @return
     */
    @PostMapping(API_PREFIX_DEPARTMEN + "/departmentIds")
    R<List<DepartmentDTO>> selectdepartmentIds(@RequestBody List<Long> departmentIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 查找部门列表
     *
     * @param departmentDTO
     * @param source
     * @return
     */
    @PostMapping(API_PREFIX_DEPARTMEN + "/selectAll")
    R<List<DepartmentDTO>> selectDepartment(@RequestBody DepartmentDTO departmentDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 查找部门列表
     *
     * @param source
     * @return
     */
    @GetMapping(API_PREFIX_DEPARTMEN + "/getAll")
    R<List<DepartmentDTO>> getAll(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 查看所有一级部门
     * @param inner
     */
    @GetMapping(API_PREFIX_DEPARTMEN + "/selectParentDepartment")
    R<List<DepartmentDTO>> selectParentDepartment(@RequestHeader(SecurityConstants.FROM_SOURCE) String inner);
}
