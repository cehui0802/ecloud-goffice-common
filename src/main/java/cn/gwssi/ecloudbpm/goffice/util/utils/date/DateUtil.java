package cn.gwssi.ecloudbpm.goffice.util.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;


/**
 * @Description: 时间日期格式化处理
 * @author：zxy
 * @date：2020/7/3
 */
public class DateUtil {

    /**
     * 计算时间差几天、几时
     */
    public static Map<String, String> getDiff(Date start, Date end) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = end.getTime() - start.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        Map<String, String> diffMap = new HashMap<>();
        diffMap.put("day", day + "");
        diffMap.put("hour", hour + "");
        return diffMap;
    }

    /**
     * 获取下个 周一 n = 2,周二 n=3,周日 n = 1
     */
    public static String getNextWeekMonday(Date date, int n) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            // 获得当前日期是一个星期的第几天
            int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (1 == dayWeek) {
                cal.add(Calendar.DAY_OF_MONTH, -1);
            }
            // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
            cal.setFirstDayOfWeek(n);
            // 获得当前日期是一个星期的第几天
            int day = cal.get(Calendar.DAY_OF_WEEK);
            // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
            cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
            cal.setTime(cal.getTime());
            cal.add(Calendar.DATE, 7);
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回两个时间差的分钟数(endTime > startTime)
     */
    public static String diffDate(Date startTime, Date endTime) {
        long startTimeLong = 0;
        long endTimeLong = 0;
        try {
            String startTimeStr = convertDate(startTime);
            String endTimeStr = convertDate(endTime);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            startTimeLong = df.parse(startTimeStr).getTime();
            endTimeLong = df.parse(endTimeStr).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf((endTimeLong - startTimeLong) / 1000 / 60);

    }


    /**
     * 返回剩余天数(向上取整)
     */
    public static long getSurplusTime(Date date) {
        Date currentDate = new Date();
        long days = (long) Math.ceil((date.getTime() - currentDate.getTime())*1.0 / (1000 * 3600 * 24));
        return days;
    }


    /**
     * 返回当前时间
     */
    public static String getCurrentTime() {
        return convertDate(new Date());
    }

    /**
     * 获取当前时间 + 晚n小时时间
     */

    public static String getDelay(int delayMin) {
        long currentTime = System.currentTimeMillis();
        currentTime += delayMin * 60 * 1000;
        Date date = new Date(currentTime);
        return convertDate(date);
    }


    /**
     * 转换Date为字符串
     */
    public static String convertDate(Date date) {
        if (date == null || "".equals(date)) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        String dateStr = date.toString();
        String formatStr = "";
        try {
            formatStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format((Date) formatter.parse(dateStr));
            String hour = dateStr.substring(11, 13);
            formatStr = formatStr.substring(0, 11) + hour + formatStr.substring(13, formatStr.length());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return formatStr;
        }
    }


    /**
     * 转换字符串为Date
     */
    public static Date convertStr(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获得当前时间往前推所传天数的时间 (java8适用)
     */
    public static Date beforeDate(int days) {
        LocalDateTime now = LocalDateTime.now();
        now = now.minus(days, ChronoUnit.DAYS);
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }

    /**
     * 获得两个字符串时间的差值(小时)
     */
    public static int getDifference(String start, String end) {
        Date startTime = convertStr(start);
        Date endTime = convertStr(end);

        long startSeconds = startTime.getTime();
        long endSeconds = endTime.getTime();

        String diffMi = diffDate(startTime, endTime);
        return Integer.parseInt(diffMi);
    }

    /**
     * 获取当前月第一天
     */
    public static String getFirstDayMonth() {
        Calendar calendar = Calendar.getInstance();
        // 设置月份
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        // 获取某月最小天数
        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        calendar.set(Calendar.DAY_OF_MONTH, firstDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime()) + " 00:00:00";
    }

    public static String getLastDayMonth() {
        Calendar calendar = Calendar.getInstance();
        // 设置月份
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        // 获取某月最大天数
        int lastDay = 0;
        //2月的平年瑞年天数
        if (calendar.get(Calendar.MONTH) == 2) {
            lastDay = calendar.getLeastMaximum(Calendar.DAY_OF_MONTH);
        } else {
            lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        // 设置日历中月份的最大天数
        calendar.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime()) + " 23:59:59";
    }

    /**
     * @Description: 描述：日期格式化
     * @Author: 作者：zxy
     * @Date: 日期：2020-06-08 20:03
     * @Param: 参数：
     * @return: 返回值：
     */
    public static String getFromStr(String dateString, String format) {
        Date date = new Date();
        String getString = null;
        try {
            date = new SimpleDateFormat(format).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        getString = new SimpleDateFormat(format).format(date);
        return getString;
    }

    /**
     * 获取所传年月的第一天
     */
    public static String getFirstDayByYearAndMonth(String year, String month) {
        Calendar calendar = Calendar.getInstance();
        // 设置年份
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        // 设置月份
        calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        // 获取某月最小天数
        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        calendar.set(Calendar.DAY_OF_MONTH, firstDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime()) + " 00:00:00";
    }

    /**
     * 获取所传年月的最后一天
     */
    public static String getLastDayByYearAndMonth(String year, String month) {
        Calendar calendar = Calendar.getInstance();
        // 设置年份
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        // 设置月份
        calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        // 获取某月最大天数
        int lastDay = 0;
        //2月的平年瑞年天数
        if (calendar.get(Calendar.MONTH) == 2) {
            lastDay = calendar.getLeastMaximum(Calendar.DAY_OF_MONTH);
        } else {
            lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        // 设置日历中月份的最大天数
        calendar.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime()) + " 23:59:59";
    }

    /**
     * Title: 减去几分钟<br>
     * Description: minusMinutes<br>
     * CreateDate: 2020-06-26 下午 03:11<br>
     *
     * @param src
     * @param minutes
     * @return java.util.Date
     * @throws
     * @category 减去几分钟
     * @author Lyric1st
     */
    public static Date minusMinutes(Date src, Long minutes) {

        LocalDateTime localDateTime = date2LocalDateTime(src);
        LocalDateTime localDateTime1 = localDateTime.minusMinutes(minutes);
        return localDateTime2Date(localDateTime1);
    }


    /**
     * Title: 加上几分钟<br>
     * Description: plusMinutes<br>
     * CreateDate: 2020-06-27 下午 04:50<br>
     *
     * @param src
     * @param minutes
     * @return java.util.Date
     * @throws
     * @category 加上几分钟
     * @author Lyric1st
     */
    public static Date plusMinutes(Date src, Long minutes) {
        LocalDateTime localDateTime = date2LocalDateTime(src);
        LocalDateTime localDateTime1 = localDateTime.plusMinutes(minutes);
        return localDateTime2Date(localDateTime1);
    }

    /**
     * Title: 加上几小时<br>
     * Description: plusHours<br>
     * CreateDate: 2020-07-31 下午 03:22<br>
     *
     * @param src
     * @param hours
     * @return java.util.Date
     * @throws
     * @category 加上几小时
     * @author Lyric1st
     */
    public static Date plusHours(Date src, Long hours) {

        LocalDateTime localDateTime = date2LocalDateTime(src);
        LocalDateTime localDateTime1 = localDateTime.plusHours(hours);
        return localDateTime2Date(localDateTime1);
    }

    /**
     * Title: 减去级小时<br>
     * Description: minusHours<br>
     * CreateDate: 2020-07-31 下午 03:22<br>
     *
     * @param src
     * @param hours
     * @return java.util.Date
     * @throws
     * @category 减去级小时
     * @author Lyric1st
     */
    public static Date minusHours(Date src, Long hours) {

        LocalDateTime localDateTime = date2LocalDateTime(src);
        LocalDateTime localDateTime1 = localDateTime.minusHours(hours);
        return localDateTime2Date(localDateTime1);
    }


    /**
     * Title: date2LocalDateTime<br>
     * Description: date2LocalDateTime<br>
     * CreateDate: 2020-06-27 下午 04:49<br>
     *
     * @param date
     * @return java.time.LocalDateTime
     * @throws
     * @category date2LocalDateTime
     * @author Lyric1st
     */
    public static LocalDateTime date2LocalDateTime(Date date) {
        Instant instant = date.toInstant();//An instantaneous point on the time-line.(时间线上的一个瞬时点。)
        ZoneId zoneId = ZoneId.systemDefault();//A time-zone ID, such as {@code Europe/Paris}.(时区)
        return instant.atZone(zoneId).toLocalDateTime();

    }

    /**
     * Title: localDateTime2Date<br>
     * Description: localDateTime2Date<br>
     * CreateDate: 2020-06-27 下午 04:50<br>
     *
     * @param localDateTime
     * @return java.util.Date
     * @throws
     * @category localDateTime2Date
     * @author Lyric1st
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);//Combines this date-time with a time-zone to create a  ZonedDateTime.
        return Date.from(zdt.toInstant());
    }

    /**
     *  将传入时间的年月日部分转成今天
     */
    public static String changeToCurrent(String dateTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar result = Calendar.getInstance();
        try {
            Date date1 = simpleDateFormat.parse(dateTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);

            result.setTime(new Date());
            result.set(Calendar.HOUR_OF_DAY,hour);
            result.set(Calendar.MINUTE,min);
            result.set(Calendar.SECOND,second);
            return simpleDateFormat.format(result.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @author lvyuan
     * @date 2020/7/29 15:20
     *  获取两个时间的中间值
     * @param start
     * @param end
     */
    public static Date getMid(Date start, Date end){
        long startSeconds = start.getTime();
        long endSeconds = end.getTime();
        long diff = endSeconds - startSeconds;
        long midSeconds = startSeconds + diff/2;

        Date min = new Date();
        min.setTime(midSeconds);
        return min;

    }

}
