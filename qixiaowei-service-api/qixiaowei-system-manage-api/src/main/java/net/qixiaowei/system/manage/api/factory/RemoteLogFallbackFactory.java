package net.qixiaowei.system.manage.api.factory;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.RemoteLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import net.qixiaowei.system.manage.api.domain.SysLoginInfo;
import net.qixiaowei.system.manage.api.domain.SysOperLog;

/**
 * 日志服务降级处理
 * 
 * 
 */
@Component
public class RemoteLogFallbackFactory implements FallbackFactory<RemoteLogService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteLogFallbackFactory.class);

    @Override
    public RemoteLogService create(Throwable throwable)
    {
        log.error("日志服务调用失败:{}", throwable.getMessage());
        return new RemoteLogService()
        {
            @Override
            public R<Boolean> saveLog(SysOperLog sysOperLog, String source)
            {
                return null;
            }

            @Override
            public R<Boolean> saveLoginInfo(SysLoginInfo sysLoginInfo, String source)
            {
                return null;
            }
        };

    }
}
