package net.qixiaowei.integration.log.service;

import net.qixiaowei.system.manage.api.dto.log.OperationLogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.system.manage.api.remote.log.RemoteOperationLogService;

import java.util.List;

/**
 * 异步调用日志服务
 */
@Service
public class AsyncLogService {
    @Autowired
    private RemoteOperationLogService remoteOperationLogService;

    /**
     * 保存系统日志记录
     */
    @Async
    public void addOperationLogLog(OperationLogDTO operationLogDTO) {
        remoteOperationLogService.add(operationLogDTO, SecurityConstants.INNER);
    }

    /**
     * 批量保存系统日志记录
     */
    @Async
    public void insertOperationLogs(List<OperationLogDTO> operationLogDTOListO) {
        remoteOperationLogService.insertOperationLogs(operationLogDTOListO, SecurityConstants.INNER);
    }
}
