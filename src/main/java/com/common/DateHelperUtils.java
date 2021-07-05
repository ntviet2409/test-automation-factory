package com.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelperUtils {
    public static String getTomorrowDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(tomorrow);
    }
}
