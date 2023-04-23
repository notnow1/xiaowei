package net.qixiaowei.integration.common.utils.excel;

import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static net.qixiaowei.integration.common.web.domain.AjaxResult.*;

/**
 * @author TANGMICHI
 * @date 2022/11/15
 */
public class ExcelUtils {
    /**
     * 将实体类转换为list
     *
     * @param object
     * @param errorExcelId
     * @return
     */
    public static void packList(Object object, List<Object> objectList, String errorExcelId) {
        // 获取object的Class对象
        Class<?> clazz = object.getClass();
        // 获取对象的所有属性
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            // 使非Public类型的属性可以被访问
            field.setAccessible(true);
            //错误数据id
            if (StringUtils.isBlank(errorExcelId)){
                //跳出循环不赋值
                if (StringUtils.equals(field.getName(),"errorData")){
                    continue;
                }
            }
            try {
                objectList.add(field.get(object));
            } catch (Exception e) {
                throw new ServiceException(e.toString());
            }
        }
    }

    /**
     * @param <T>  泛型
     * @param row  行
     * @param line 列
     * @param maps 数据
     * @param t    实体类
     * @param list 实体类集合
     * @param flag
     */
    public static <T> void mapToListModel(int row, int line, List<Map<Integer, String>> maps, T t, List<T> list, boolean flag) {
        for (int i = 0; i < maps.size(); i++) {
            List<Object> list2 = new ArrayList<>();
            //从第几行开始
            if (i >= row) {
                Map<Integer, String> map = maps.get(i);
                map.forEach((key, value) -> {
                    //从第几列开始
                    if (key >= line) {
                        String s =  ExcelUtils.parseExcelTime(map.get(key));
                        if (StringUtils.isBlank(s)) {
                            list2.add("");
                        } else {
                            list2.add(s.trim());
                        }

                    }
                });
                try {
                    T t1 = listToModel(list2, t,flag);
                    //添加list
                    list.add(t1);
                } catch (Exception e) {
                    throw new ServiceException("读取excel数据 转换实体类失败！");
                }
            }
        }


    }
    /**
     * 判断是否是自定义格式的数据
     * @param str
     * @return
     */
    public static String parseExcelTime(String str) {
        String excelDateTime = null;
        if (StringUtils.isNotBlank(str)){
            if (str.contains("月")){
                if (str.contains("一")){
                    excelDateTime=ExcelUtils.getExcelDateTime(excelDateTime,str);
                }else if (str.contains("二")){
                    excelDateTime=ExcelUtils.getExcelDateTime(excelDateTime,str);
                }else if (str.contains("三")){
                    excelDateTime=ExcelUtils.getExcelDateTime(excelDateTime,str);
                }else if (str.contains("四")){
                    excelDateTime=ExcelUtils.getExcelDateTime(excelDateTime,str);
                }else if (str.contains("五")){
                    excelDateTime=ExcelUtils.getExcelDateTime(excelDateTime,str);
                }else if (str.contains("六")){
                    excelDateTime=ExcelUtils.getExcelDateTime(excelDateTime,str);
                }else if (str.contains("七")){
                    excelDateTime=ExcelUtils.getExcelDateTime(excelDateTime,str);
                }else if (str.contains("八")){
                    excelDateTime=ExcelUtils.getExcelDateTime(excelDateTime,str);
                }else if (str.contains("九")){
                    excelDateTime=ExcelUtils.getExcelDateTime(excelDateTime,str);
                }else if (str.contains("十")){
                    excelDateTime= excelDateTime=ExcelUtils.getExcelDateTime(excelDateTime,str);
                }else if (str.contains("十一")){
                    excelDateTime=ExcelUtils.getExcelDateTime(excelDateTime,str);
                }else if (str.contains("十二")){
                    excelDateTime=ExcelUtils.getExcelDateTime(excelDateTime,str);
                }
            }
        }
        if (StringUtils.isNotBlank(excelDateTime)){
            str=excelDateTime;
        }
        return str;
    }
    
    /**
     * 解析中文年份
     * @param excelDateTime
     * @param str
     * @return
     */
    private static String getExcelDateTime(String excelDateTime, String str) {
        if (StringUtils.isNotBlank(str)){
            String chMonth = str.split("-")[0];
            String chYear = "20" + str.split("-")[1];
            switch (chMonth) {
                case "一月":
                    excelDateTime=chYear+"/1";
                    break;
                case "二月":
                    excelDateTime=chYear+"/2";
                    break;
                case "三月":
                    excelDateTime=chYear+"/3";
                    break;
                case "四月":
                    excelDateTime=chYear+"/4";
                    break;
                case "五月":
                    excelDateTime=chYear+"/5";
                    break;
                case "六月":
                    excelDateTime=chYear+"/6";
                    break;
                case "七月":
                    excelDateTime=chYear+"/7";
                    break;
                case "八月":
                    excelDateTime=chYear+"/8";
                    break;
                case "九月":
                    excelDateTime=chYear+"/9";
                    break;
                case "十月":
                    excelDateTime=chYear+"/10";
                    break;
                case "十一月":
                    excelDateTime=chYear+"/11";
                    break;
                case "十二月":
                    excelDateTime=chYear+"/12";
                    break;
                default:
                    break;
            }
        }
        return excelDateTime;
    }

    /**
     * 解析中文月份
     *
     * @param dateList 中文年月List
     * @return monthList
     */
    public static List<Integer> parseMonthCh(List<String> dateList) {
        List<Integer> monthList = new ArrayList<>();
        for (String dateString : dateList) {


        }
        return monthList;
    }
    /**
     * 返回成功excel数据
     *
     * @return 成功消息
     */
    public static <T> Map<Object, Object> parseExcelResult(List<T> successExcel, List<T> errorExcel, boolean excelFlag, String errorExcelId) {
        Map<Object, Object> data = new HashMap<>();
        if (StringUtils.isEmpty(successExcel)) {
            data.put(SUCCESS_TOTAL, 0);
        } else {
            data.put(SUCCESS_TOTAL, successExcel.size());
        }
        if (StringUtils.isEmpty(errorExcel)) {
            data.put(ERROR_TOTAL, 0);
        } else {
            data.put(ERROR_TOTAL, errorExcel.size());
            if (StringUtils.isNotNull(errorExcelId)){
                data.put("errorExcelId",errorExcelId);
            }
        }
        if (excelFlag){
            data.put(SUCCESS_LIST, successExcel);
            data.put(ERROR_LIST, errorExcel);
        }

        return data;
    }
    /**
     * 将list转换为实体类(使用此方法字段属性为BigDecimal时，会报错，请改为其他属性，插入数据库时需修改成对应的属性，此方法的实体类仅作为Excel导入导出使用！)
     *
     * @param <T>
     * @param list
     * @param t
     * @param flag
     * @throws Exception
     * @return
     */
    public static <T> T listToModel(List<Object> list, T t, boolean flag) throws Exception {
        T t1 = (T) t.getClass().newInstance();

        Field[] fields = t1.getClass().getDeclaredFields();
        int  difference = fields.length - list.size();
        if (difference>0){
            for (int i = 0; i < difference; i++) {
                list.add("");
            }
        }
        for (int k = 0, len = fields.length; k < len; k++) {
            // 根据属性名称,找寻合适的set方法
            String fieldName = fields[k].getName();
            //如果是true跳过第一个字段赋值
            if (flag && StringUtils.equals(fieldName,"errorData")){
                continue;
            }
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
