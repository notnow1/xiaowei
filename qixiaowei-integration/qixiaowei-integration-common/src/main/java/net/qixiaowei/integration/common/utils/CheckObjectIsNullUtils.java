package net.qixiaowei.integration.common.utils;

import net.qixiaowei.integration.common.exception.ServiceException;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author 周鑫(玖枭)
 */
public class CheckObjectIsNullUtils {

    /**
     * 判断一个对象是否为null
     *
     * @param object
     * @return
     */
    public static boolean isNull(Object object) {
        // 获取object的Class对象
        Class<?> clazz = object.getClass();
        // 获取对象的所有属性
        Field[] fields = clazz.getDeclaredFields();
        // 定义返回结果
        boolean flag = true;

        for (Field field : fields) {
            // 使非Public类型的属性可以被访问
            field.setAccessible(true);
            Object fieldValue = null;
            Type type = null;

            try {
                fieldValue = field.get(object);
                // 获取到属性类型
                type = field.getType();
                // 获取属性名称
                String fieldName = field.getName();

            } catch (Exception e) {
                throw new ServiceException(e.toString());
            }

            // 只要有一个属性值不为null 就返回false 表示对象不为null
            if (fieldValue != null) {

                // 如果fieldValue不为null,并且fieldValue的值等于false时，则不认为对象不为空。
                if (Objects.equals(type.getTypeName(), "boolean")
                        && Objects.equals(fieldValue, false)) {
                    continue;
                } else {
                    flag = false;
                    break;
                }
            }
        }

        return flag;
    }
}
