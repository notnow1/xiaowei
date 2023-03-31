package net.qixiaowei.strategy.cloud.service.impl.marketInsight;

import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;

import java.lang.reflect.Field;

/**
 * 封装删除数据引用
 *
 * @author TANGMICHI
 * @since 2023-03-16
 */
public class PackCopyMarketInsight {
    /**
     * @param t1
     * @param t2
     * @return
     * @throws Exception
     */
    public static <T> void copyProperties(T t1, T t2) {
        Field[] fields1 = t1.getClass().getDeclaredFields();
        Field[] fields2 = t2.getClass().getDeclaredFields();
        for (int i = 0; i < fields1.length; i++) {
            // 根据属性名称,找寻合适的set方法
            String fieldName = fields1[i].getName();
            for (int i2 = 0; i2 < fields2.length; i2++) {
                // 根据属性名称,找寻合适的set方法
                String fieldName2 = fields2[i2].getName();
                if (StringUtils.equals(fieldName, fieldName2)) {
                    if (!StringUtils.equals(fieldName, "planBusinessUnitName")) {
                        BeanUtils.copyProperties(t1, t2);
                    }
                }
            }
        }
    }
}

