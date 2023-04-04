package net.qixiaowei.integration.common.utils;

import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.excel.ExcelUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";


    public static String YYYY_MM_DIAGONAL = "yyyy/MM";

    public static String YYYY_MM_DD_DIAGONAL = "yyyy/MM/dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static String YYYY_MM_DD_HH_CN = "yyyy年MM月dd日 HH:mm";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static LocalDate getLocalDate() {
        Instant instant = new Date().toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        return instant.atZone(zoneId).toLocalDate();
    }

    /**
     * 获取中文当前日期, 默认格式为yyyy年MM月dd日 00:00
     *
     * @return String
     */
    public static final String getCNOfTime(Date date) {
        return parseDateToStr(YYYY_MM_DD_HH_CN, date);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将Excel中各种日期型字符串 转化为日期格式
     */
    public static Date parseAnalysisExcelDate(Object str) {
        String replace = str.toString();
        if (replace == null) {
            return null;
        }
        if (str.toString().contains("\\-") && !str.toString().contains("日") && str.toString().contains("月")) {
            replace = ExcelUtils.parseExcelTime(str.toString());
        } else if (str.toString().contains("年") && str.toString().contains("月") && str.toString().contains("日")) {
            replace = str.toString().replace("年", "/").replace("月", "/").replace("日", "");
        }
        try {
            return parseDate(replace, parsePatterns);
        } catch (ParseException e) {
            throw new ServiceException("请输入正确的日期!" + str);
        }
    }

    /**
     * 获取当前月
     */
    public static int getMonth() {
        Calendar calendar = Calendar.getInstance();
        // 获取当前月
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前年
     */
    public static int getYear() {
        Calendar calendar = Calendar.getInstance();
        // 获取当前年
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取指定时间的年值
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 获取当前年
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取指定时间的月值
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 获取当前年
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前日期所在季度
     */
    public static int getQuarter() {
        int month = DateUtils.getMonth();
        return month % 3 == 0 ? month / 3 : month / 3 + 1;
    }

    /**
     * 获取当前日期所在半年度
     */
    public static int getHalfYears() {
        int month = DateUtils.getMonth();
        return month % 6 == 0 ? month / 6 : month / 6 + 1;
    }

    /**
     * 今年的第几周
     */
    public static int getDayOfWeek() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD);
        int week = 7;
        int dayOfYear = DateUtils.getDayOfYear();
        String format = simpleDateFormat.format(DateUtils.getNowDate());
        String format1 = simpleDateFormat.format(DateUtils.getCurrYearLast());
        if (StringUtils.equals(format, format1)) {
            return dayOfYear / week - 1;
        }
        return dayOfYear / week;
    }

    /**
     * 格式化时间
     */
    public static String format(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD_DIAGONAL);
        return simpleDateFormat.format(date);
    }

    /**
     * 获取今年最后一天日期
     * 年份
     *
     * @return Date
     */
    public static Date getCurrYearLast() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }

    /**
     * 获取某年某月的最后一天
     *
     * @param month,year 时间
     * @return Date
     */
    public static Date getMonthStart(int year, int month) { // 月份开始
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.YEAR, year);
        startCalendar.set(Calendar.MONTH, month - 1);
        startCalendar.roll(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        //获取目标月和目标年份的当月第一天时间
        final int start = Calendar.YEAR;
        startCalendar.set(Calendar.DAY_OF_MONTH, start);
        return startCalendar.getTime();
    }

    /**
     * 获取某年某月的最后一天
     *
     * @return Date
     */
    public static Date getMonthLast(int year, int month) {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.YEAR, year);
        endCalendar.set(Calendar.MONTH, month - 1);
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.SECOND, 59);
        //获取目标月和目标年份的当月第一天时间
        final int last = endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        endCalendar.set(Calendar.DAY_OF_MONTH, last);
        return endCalendar.getTime();
    }

    /**
     * 获取某年某某个季度的第一天
     *
     * @return Date
     */
    public static Date getQuarterStart(int year, int quarter) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.YEAR, year);//年
        startCalendar.set(Calendar.DATE, 15);//日
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);//时
        startCalendar.set(Calendar.MINUTE, 0);//分
        startCalendar.set(Calendar.SECOND, 0);//秒
        switch (quarter) {
            case 1:
                startCalendar.set(Calendar.MONTH, Calendar.JANUARY);
                break;
            case 2:
                startCalendar.set(Calendar.MONTH, Calendar.APRIL);
                break;
            case 3:
                startCalendar.set(Calendar.MONTH, Calendar.JULY);
                break;
            case 4:
                startCalendar.set(Calendar.MONTH, Calendar.OCTOBER);
                break;
        }
        //获取目标月和目标年份的当月第一天时间
        final int last = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        return startCalendar.getTime();
    }

    /**
     * 获取某年某某个季度的最后一天
     *
     * @return Date
     */
    public static Date getQuarterLast(int year, int quarter) {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.YEAR, year);//年
        endCalendar.set(Calendar.DATE, 15);//日
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);//时
        endCalendar.set(Calendar.MINUTE, 59);//分
        endCalendar.set(Calendar.SECOND, 59);//秒
        switch (quarter) {
            case 1:
                endCalendar.set(Calendar.MONTH, Calendar.MARCH);
                break;
            case 2:
                endCalendar.set(Calendar.MONTH, Calendar.JUNE);
                break;
            case 3:
                endCalendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
                break;
            case 4:
                endCalendar.set(Calendar.MONTH, Calendar.DECEMBER);
                break;
        }
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return endCalendar.getTime();
    }

    /**
     * 获取半年度的开始日期
     *
     * @param year     年份
     * @param halfYear 半年度
     * @return Date
     */
    public static Date getHalfYearStart(int year, int halfYear) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.YEAR, year);//年
        startCalendar.set(Calendar.DATE, 15);//日
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);//时
        startCalendar.set(Calendar.MINUTE, 0);//分
        startCalendar.set(Calendar.SECOND, 0);//秒
        switch (halfYear) {
            case 1:
                startCalendar.set(Calendar.MONTH, Calendar.JANUARY);
                break;
            case 2:
                startCalendar.set(Calendar.MONTH, Calendar.JULY);
                break;
        }
        //获取目标月和目标年份的当月第一天时间
        final int last = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        return startCalendar.getTime();
    }

    /**
     * 获取半年度的结束日期
     *
     * @param year     年份
     * @param halfYear 半年度
     * @return Date
     */
    public static Date getHalfYearLast(int year, int halfYear) {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.YEAR, year);//年
        endCalendar.set(Calendar.DATE, 15);//日
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);//时
        endCalendar.set(Calendar.MINUTE, 59);//分
        endCalendar.set(Calendar.SECOND, 59);//秒
        switch (halfYear) {
            case 1:
                endCalendar.set(Calendar.MONTH, Calendar.JUNE);
                break;
            case 2:
                endCalendar.set(Calendar.MONTH, Calendar.DECEMBER);
                break;
        }
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return endCalendar.getTime();
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearStart(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, --year);
        return calendar.getTime();
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    /**
     * 今年的第几天
     */
    public static int getDayOfYear() {
        Calendar calendar = Calendar.getInstance();
        Date nowDate = DateUtils.getNowDate();
        calendar.setTime(nowDate);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 Date ==> LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        return instant.atZone(zoneId).toLocalDate();
    }

    /**
     * 获取String类型的年份
     *
     * @param stringDate
     * @return
     */
    public static int getYear(final String format, String stringDate) {
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(stringDate);
        } catch (ParseException e) {
            throw new ServiceException("时间转化失败 请检查时间格式");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 获取当前年
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取String类型的月份
     *
     * @param stringDate
     * @return
     */
    public static int getMonth(final String format, String stringDate) {
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(stringDate);
        } catch (ParseException e) {
            throw new ServiceException("时间转化失败 请检查时间格式");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 获取当前年
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取LocalTime时间类型的月份
     *
     * @param localTime 时间
     * @return
     */
    public static int getMonth(LocalDate localTime) {
        Calendar calendar = Calendar.getInstance();
        LocalDateTime localDateTime = LocalDateTime.of(localTime, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        calendar.setTime(Date.from(zdt.toInstant()));
        // 获取当前年
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取LocalTime时间类型的月份
     *
     * @param localTime 时间
     * @return String
     */
    public static String localToString(LocalDate localTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localTime.format(df);
    }
}
