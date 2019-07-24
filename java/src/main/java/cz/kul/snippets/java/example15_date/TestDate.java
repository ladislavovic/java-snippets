/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.java.example15_date;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.TimeZone;

public class TestDate {
    
    @Test
    public void dateToStringIsTimezoneSpecific() {
        // To
        // When you create Date instance, the date parameter is the milliseconds since January 1, 1970, 00:00:00 GMT.
        // So it is not timezone specific. But Whne you call toString it print local time.
        Date date = new Date(0);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
        String moscowTime = date.toString();
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        String newYorkTime = date.toString();
        Assert.assertNotEquals(moscowTime, newYorkTime);
        TimeZone.setDefault(null);
    }

    @Test
    public void dateInJDBC() {
        // Representation of date in JDBC
        //
        // Date, Time and Timestamp are descendant of java.util.Date

        java.util.Date utilDate = new java.util.Date(3600 * 1000 * 5); // 1970-01-01 05:00:00

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
