package net.qixiaowei.integration.tenant.aspect;


import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.tenant.annotation.IgnoreTenant;
import net.qixiaowei.integration.tenant.context.TenantContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;


/**
 * 忽略多租户的Aspect
 *
 * @author hzk
 */

@Aspect
@Slf4j
public class TenantIgnoreAspect {

    @Around("@annotation(ignoreTenant)")
    public Object around(ProceedingJoinPoint joinPoint, IgnoreTenant ignoreTenant) throws Throwable {
        try {
            TenantContextHolder.setIgnoreTenant(true);
            // 执行逻辑
            return joinPoint.proceed();
        } finally {
            TenantContextHolder.remove();
        }
    }

}
