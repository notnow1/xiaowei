package net.qixiaowei.system.manage.api.remote.basic;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.factory.basic.RemoteEmployeeFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

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

}
