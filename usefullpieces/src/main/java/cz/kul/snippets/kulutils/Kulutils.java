package cz.kul.snippets.kulutils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.DateUtils;

public class Kulutils {

    /**
     * Return the date of the first day of the week.
     * 
     * @param year
     *            The year.
     * @param weekNumber
     *            The order number of the week. The first week of the year has the number
     *            1. It can starts in the previous year.
     * @return The date of the first day of the week.
     */
    public static Date getFirsDayOfWeek(int year, int weekNumber) {
        Calendar c = Calendar.getInstance();
        c = DateUtils.truncate(c, Calendar.DAY_OF_MONTH);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.WEEK_OF_YEAR, weekNumber);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        return c.getTime();
    }
    
    public static Calendar getFirstDayOfMonth(Locale locale, int year, int month) {
        Calendar c = Calendar.getInstance(locale);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c = DateUtils.truncate(c, Calendar.DAY_OF_MONTH);
        return c;
    }
    
    public static Calendar getLastDayOfMonth(Locale locale, int year, int month) {
        Calendar c = getFirstDayOfMonth(locale, year, month);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c;
    }
    
    public static int getWeekCountOfYear(int year, Locale locale) {
        Calendar c = Calendar.getInstance(locale);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.DECEMBER);
        c.set(Calendar.DAY_OF_MONTH, 31);
        
        if (c.get(Calendar.WEEK_OF_YEAR) == 1) {
            c.add(Calendar.WEEK_OF_YEAR, -1);
        }
        
        return c.get(Calendar.WEEK_OF_YEAR);        
    }

}
