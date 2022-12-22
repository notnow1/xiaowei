package net.qixiaowei.integration.common.utils.excel;

import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author TANGMICHI
 * @date 2022/11/15
 */
public class ExcelUtils {
    /**
     * 将实体类转换为list
     *
     * @param object
     * @return
     */
    public static void packList(Object object, List<Object> objectList) {
        // 获取object的Class对象
        Class<?> clazz = object.getClass();
        // 获取对象的所有属性
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            // 使非Public类型的属性可以被访问
            field.setAccessible(true);
            try {
                objectList.add(field.get(object));
            } catch (Exception e) {
                throw new ServiceException(e.toString());
            }
        }
    }

    /**
     * @param row  行
     * @param line 列
     * @param maps 数据
     * @param t    实体类
     * @param list 实体类集合
     * @param <T>  泛型
     */
    public static <T> void mapToListModel(int row, int line, List<Map<Integer, String>> maps, T t, List<T> list) {
        for (int i = 0; i < maps.size(); i++) {
            List<Object> list2 = new ArrayList<>();
            //从第几行开始
            if (i >= row) {
                Map<Integer, String> map = maps.get(i);
                map.forEach((key, value) -> {
                    //从第几列开始
                    if (key >= line) {
                        String s = map.get(key);
                        if (StringUtils.isBlank(s)) {
                            list2.add("");
                        } else {
                            list2.add(s.trim());
                        }

                    }
                });
                try {
                    T t1 = listToModel(list2, t);
                    //添加list
                    list.add(t1);
                } catch (Exception e) {
                    throw new ServiceException("读取excel数据 转换实体类失败！");
                }
            }
        }


    }


    /**
     * 将list转换为实体类(使用此方法字段属性为BigDecimal时，会报错，请改为其他属性，插入数据库时需修改成对应的属性，此方法的实体类仅作为Excel导入导出使用！)
     *
     * @param <T>
     * @param list
     * @param t
     * @throws Exception
     * @return
     */
    public static <T> T listToModel(List<Object> list, T t) throws Exception {
        T t1 = (T) t.getClass().newInstance();

        Field[] fields = t1.getClass().getDeclaredFields();
        if (list.size() != fields.length) {
            return t1;
        }
        for (int k = 0, len = fields.length; k < len; k++) {
            // 根据属性名称,找寻合适的set方法
            String fieldName = fields[k].getName();
            Class<?> type = fields[k].getType();
            String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase()
                    + fieldName.substring(1);

            Method method = null;
            Class<?> clazz = t1.getClass();

            try {
                method = clazz.getMethod(setMethodName, list.get(k).getClass());
            } catch (SecurityException e1) {
                e1.printStackTrace();
                return t1;
            } catch (NoSuchMethodException e1) {
                String newMethodName = "set" + fieldName.substring(0, 1).toLowerCase()
                        + fieldName.substring(1);
                try {
                    method = clazz.getMethod(newMethodName, list.get(k).getClass());
                } catch (SecurityException e) {
                    e.printStackTrace();
                    return t1;
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    return t1;
                }
            }
            if (method == null) {
                return t1;
            }
            method.invoke(t1, list.get(k));
        }
        return t1;
    }

    /***
     * 设置 excel 的样式
     * @return
     */
    public static WriteHandler createTableStyle() {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 设置字体
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 10);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 10);
        contentWriteCellStyle.setWriteFont(contentWriteFont);


        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        return horizontalCellStyleStrategy;
    }

    //设置样式 去除默认表头样式及设置内容居中
    public static HorizontalCellStyleStrategy getStyleStrategy(){
        //内容样式策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //垂直居中,水平居中
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        //设置 自动换行
        contentWriteCellStyle.setWrapped(true);
        // 字体策略
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 12);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        //头策略使用默认
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }
}
