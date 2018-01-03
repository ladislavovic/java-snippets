package cz.kul.snippets.kulutils;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

public class KulutilsTest {
    
    @Test
    public void testGetFirstDayOfWeek() throws ParseException {

        Calendar expected = Calendar.getInstance();
        expected = DateUtils.truncate(expected, Calendar.DAY_OF_MONTH);
        expected.set(Calendar.DAY_OF_WEEK, expected.getFirstDayOfWeek());
        
        {
            expected.set(Calendar.YEAR, 2014);
            expected.set(Calendar.WEEK_OF_YEAR, 1);
            assertEquals(expected.getTime(), Kulutils.getFirsDayOfWeek(2014, 1));
        }
        
        {
            expected.set(Calendar.YEAR, 2014);
            expected.set(Calendar.WEEK_OF_YEAR, 10);
            assertEquals(expected.getTime(), Kulutils.getFirsDayOfWeek(2014, 10));
        }

        {
            expected.set(Calendar.YEAR, 2014);
            expected.set(Calendar.WEEK_OF_YEAR, 52);
            assertEquals(expected.getTime(), Kulutils.getFirsDayOfWeek(2014, 52));
        }
        
        {
            expected.set(Calendar.YEAR, 2014);
            expected.set(Calendar.WEEK_OF_YEAR, 53);
            assertEquals(expected.getTime(), Kulutils.getFirsDayOfWeek(2014, 53));
        }

    }
    
    @Test
    public void testGetFirstDayOfMonth() {
        Calendar first = Kulutils.getFirstDayOfMonth(Locale.US, 2012, 1);
        assertEquals(createDate(Locale.US, 2012, 1, 1), first.getTime());
    }

    @Test
    public void testGetLastDayOfMonth() {
        Calendar last = Kulutils.getLastDayOfMonth(Locale.US, 2012, 1);
        assertEquals(createDate(Locale.US, 2012, 1, 29), last.getTime());
    }
    
    private Date createDate(Locale locale, int year, int month, int day) {
        Calendar c = Calendar.getInstance(locale);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c = DateUtils.truncate(c, Calendar.DAY_OF_MONTH);
        return c.getTime();
    }
    
    @Test
    public void test() {
       assertEquals(52, Kulutils.getWeekCountOfYear(2014, Locale.US));
       assertEquals(52, Kulutils.getWeekCountOfYear(2015, Locale.US));
       assertEquals(52, Kulutils.getWeekCountOfYear(2014, Locale.GERMANY));
       assertEquals(53, Kulutils.getWeekCountOfYear(2015, Locale.GERMANY));
    }
    
    
    
    
}
