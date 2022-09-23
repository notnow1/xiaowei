package net.qixiaowei.integration.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.system.manage.api.RemoteLogService;
import net.qixiaowei.system.manage.api.domain.SysOperLog;

/**
 * 异步调用日志服务
 * 
 * 
 */
@Service
public class AsyncLogService
{
    @Autowired
    private RemoteLogService remoteLogService;

    /**
     * 保存系统日志记录
     */
    @Async
    public void saveSysLog(SysOperLog sysOperLog)
    {
        remoteLogService.saveLog(sysOperLog, SecurityConstants.INNER);
    }
}
