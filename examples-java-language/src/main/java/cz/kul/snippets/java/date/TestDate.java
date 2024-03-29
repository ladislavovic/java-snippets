/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.java.date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class TestDate {
    
    @After
    public void resetTimezone() {
        TimeZone.setDefault(null);
    }
    
    @Test
    public void dateInstanceHoldsMillisecondsFrom1970_1_1_GMT() {
        //
        // Date always holds milliseconds from 1970-01-01. It is NOT time zone specific.
        // When I create a Date instace by new Date(10) a always get the same instance
        // regardless local timezone.
        long MS = 100;
        
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
        Date date1 = new Date(MS);

        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        Date date2 = new Date(MS);
        
        assertEquals(MS, date1.getTime());
        assertEquals(MS, date2.getTime());
    }
    
    @Test
    public void dateToStringIsTimezoneSpecific() {
        // Date instance is not timezone specific, always holds difference to January 1, 1970, 00:00:00 GMT.
        // But toString() method write current timezone representation.
        Date date = new Date(0);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
        String moscowTime = date.toString();
        
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        String newYorkTime = date.toString();
        
        assertEquals("Thu Jan 01 03:00:00 MSK 1970", moscowTime);
        assertEquals("Wed Dec 31 19:00:00 EST 1969", newYorkTime);
    }

    @Test
    public void System_currentTimeMills_ignoreTimezone() {
        // It returns milliseconds from 1970-01-01 GMT, regardless current timezone.
        // It is the same as date.
        // Actually when you call new Date() it internally calls this method
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
        long time1 = System.currentTimeMillis();
        
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        long time2 = System.currentTimeMillis();
        
        assertTrue(Math.abs(time1 - time2) < 1000);
    }

    @Test
    public void calendar() {
        // Calendar is a convertero between an instant in time (that is
        // point in time regardless local timezone, calendar, anything) and
        // calendar fields as hour, day etc.
        
        // Celendar internally holds instant in time. The timezone has no
        // effect on it.
        {
            Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
            Calendar c2 = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
            assertTrue(Math.abs(c1.getTimeInMillis() - c2.getTimeInMillis()) < 1000);
        }
        
        // Timezone has an effect on converzion of course
        {
            Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("America/New_York"));
            int hourInNY = c.get(Calendar.HOUR_OF_DAY);
            c.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
            int hourInMoscow = c.get(Calendar.HOUR_OF_DAY);
            assertTrue(Math.abs(hourInMoscow - hourInNY) > 4);
        }
    }

    @Test
    public void dateInJDBC() {
        // Representation of date in JDBC
        //
        // Date, Time and Timestamp are descendant of java.util.Date

        java.util.Date utilDate = new java.util.Date(3600 * 1000 * 5); // 1970-01-01 05:00:00 GMT

        // java.sql.Date represents Database type, which stores the date
        // only, no time. But it is very simple class, it is not sophisticated
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        Assert.assertTrue("util date can be equal to sql date", utilDate.equals(sqlDate));
        Assert.assertTrue("and also sql date can be equal to util date", sqlDate.equals(utilDate));
        Assert.assertEquals("java.sql.Date does not truncate time", utilDate.getTime(), sqlDate.getTime());
        Assert.assertEquals("java.sql.Date prints date only", "1970-01-01", sqlDate.toString());
        
        // java.sql.Time represents Database type, which stores the time
        // only, no date. But it is very simple class, it is not sophisticated
        java.sql.Time sqlTime = new java.sql.Time(utilDate.getTime());
        Assert.assertEquals("you can equal these instances.", utilDate, sqlTime);
        Assert.assertEquals("java.sql.Time does not truncate date", utilDate.getTime(), sqlTime.getTime());
        Assert.assertEquals("java.sql.Time prints time only", "06:00:00", sqlTime.toString()); // there is 06 because it is converted to local time zone
        
        // java.sql.Timestamp represents Database type, which stores the date
        // and time
        java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(utilDate.getTime());
        Assert.assertTrue("you can equal this way", utilDate.equals(sqlTimestamp));
        Assert.assertFalse("but not this way", sqlTimestamp.equals(utilDate));
        Assert.assertEquals("java.sql.Timestamp prints date and time only", "1970-01-01 06:00:00.0", sqlTimestamp.toString());
    }

}
