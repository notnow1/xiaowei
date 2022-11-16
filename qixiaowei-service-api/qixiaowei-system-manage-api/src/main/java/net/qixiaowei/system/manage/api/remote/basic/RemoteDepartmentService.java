package net.qixiaowei.system.manage.api.remote.basic;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.factory.basic.RemoteEmployeeFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * 部门服务
 */
@FeignClient(contextId = "remoteDepartmentService", value = ServiceNameConstants.SYSTEM_MANAGE_SERVICE, fallbackFactory = RemoteEmployeeFallbackFactory.class)
public interface RemoteDepartmentService {
    String API_PREFIX_EMPLOYEE = "/department";

    /**
     * 通过Code查找部门列表
     * @return 结果
     */
    @PostMapping(API_PREFIX_EMPLOYEE + "/codeList")
    R<List<DepartmentDTO>> selectCodeList(@RequestBody List<String> departmentCodes, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
