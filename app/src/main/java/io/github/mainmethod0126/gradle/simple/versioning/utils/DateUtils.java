package io.github.mainmethod0126.gradle.simple.versioning.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public enum DateUnit {
        YEAR,
        MONTH,
        DAY,
        HOUR,
        SECONDS,
        MILLISECONDS
    }

    public static String getCurrentDate(DateUnit lastUnit) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (lastUnit == DateUnit.DAY) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        } else if (lastUnit == DateUnit.SECONDS) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

        return dateFormat.format(new Date());
    }

}
