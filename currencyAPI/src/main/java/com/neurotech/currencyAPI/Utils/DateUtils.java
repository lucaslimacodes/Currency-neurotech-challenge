package com.neurotech.currencyAPI.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {
    public static boolean isDifferenceBiggerThanOneDay(Date date1, Date date2){
        long differenceMillis = Math.abs(date2.getTime() - date1.getTime());
        long differenceDias = TimeUnit.MILLISECONDS.toDays(differenceMillis);
        return differenceDias > 1;
    }

    public static boolean isDifferenceBiggerOrEqualOneDay(Date date1, Date date2){
        long differenceMillis = Math.abs(date2.getTime() - date1.getTime());
        long differenceDias = TimeUnit.MILLISECONDS.toDays(differenceMillis);
        return differenceDias >= 1;
    }

    public static Date tomorrow(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);  // add 1 day
        return calendar.getTime();
    }

    public static Date yesterday(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);  // removes 1 day
        return calendar.getTime();
    }
}
