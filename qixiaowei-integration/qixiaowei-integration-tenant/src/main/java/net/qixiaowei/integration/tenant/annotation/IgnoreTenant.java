package net.qixiaowei.integration.tenant.annotation;

import java.lang.annotation.*;

/**
 * @description: 忽略租户
 * @Author: hzk
 * @date: 2022/11/28 17:35
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface IgnoreTenant {
}
