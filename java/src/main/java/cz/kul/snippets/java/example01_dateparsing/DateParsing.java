package cz.kul.snippets.java.example01_dateparsing;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateParsing {

    @Test
    public void testISO8601_itShouldParseSimpleDates() throws ParseException {
        // This solutin is not fully compliant with ISO8601. ISO allows
        // some date formats which are not correctly parsed by this
        // solution. But sumetimes it is sufficient.
        // TODO is the not compliance with ISO still valid with Java9?
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse("2014-12-16T10:11:12.123-03:00"));
        calendar.setTimeZone(TimeZone.getTimeZone("GMT-3")); // NOTE otherwise Calendar uses your default timezone
        Assert.assertEquals(2014, calendar.get(Calendar.YEAR));
        Assert.assertEquals(Calendar.DECEMBER, calendar.get(Calendar.MONTH));
        Assert.assertEquals(16, calendar.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(10, calendar.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(11, calendar.get(Calendar.MINUTE));
        Assert.assertEquals(12, calendar.get(Calendar.SECOND));
        Assert.assertEquals(123, calendar.get(Calendar.MILLISECOND));
        Assert.assertEquals(-3 * 3600 * 1000, calendar.get(Calendar.ZONE_OFFSET));
    }

}
