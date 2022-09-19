package net.qixiaowei.system.api;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import net.qixiaowei.system.api.domain.SysLoginInfo;
import net.qixiaowei.system.api.domain.SysOperLog;
import net.qixiaowei.system.api.factory.RemoteLogFallbackFactory;

/**
 * 日志服务
 * 
 * 
 */
@FeignClient(contextId = "remoteLogService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteLogFallbackFactory.class)
public interface RemoteLogService
{
    /**
     * 保存系统日志
     *
     * @param sysOperLog 日志实体
     * @param source 请求来源
     * @return 结果
     */
    @PostMapping("/operlog")
    public R<Boolean> saveLog(@RequestBody SysOperLog sysOperLog, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 保存访问记录
     *
     * @param sysLoginInfo 访问实体
     * @param source 请求来源
     * @return 结果
     */
    @PostMapping("/loginInfo/add")
    public R<Boolean> saveLoginInfo(@RequestBody SysLoginInfo sysLoginInfo, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
