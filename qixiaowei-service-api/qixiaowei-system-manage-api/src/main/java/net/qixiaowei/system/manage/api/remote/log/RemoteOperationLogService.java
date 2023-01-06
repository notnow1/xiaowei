package net.qixiaowei.system.manage.api.remote.log;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.log.OperationLogDTO;
import net.qixiaowei.system.manage.api.factory.log.RemoteOperationLogFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * 日志服务
 */
@FeignClient(contextId = "remoteOperationLogService", value = ServiceNameConstants.SYSTEM_MANAGE_SERVICE, fallbackFactory = RemoteOperationLogFallbackFactory.class)
public interface RemoteOperationLogService {

    String API_PREFIX_OPERATION_LOG = "/operationLog";

    /**
     * 新增操作日志表
     */

    @PostMapping(API_PREFIX_OPERATION_LOG + "/add")
    R<?> add(@RequestBody OperationLogDTO operationLogDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 批量新增操作日志表
     */
    @PostMapping(API_PREFIX_OPERATION_LOG + "/insertOperationLogs")
    R<?> insertOperationLogs(@RequestBody List<OperationLogDTO> operationLogDTOList, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


}
