package net.qixiaowei.integration.log.annotation;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.log.enums.OperationType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义操作日志记录注解
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 模块
     */
    public String title() default "";

    /**
     * 业务类型:{@link net.qixiaowei.integration.common.enums.message.BusinessType}
     */
    public BusinessType businessType();

    /**
     * 业务ID标识
     */
    public String businessId();

    /**
     * 业务操作类型
     */
    public OperationType operationType() default OperationType.OTHER;

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    public boolean isSaveResponseData() default true;
}
