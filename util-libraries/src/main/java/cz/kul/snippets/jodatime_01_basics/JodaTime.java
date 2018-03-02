package cz.kul.snippets.jodatime_01_basics;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

public class JodaTime {

    public static void main(String[] args) {
        /**
         * Instant represents the point in the timeline irrespective of any other
         * factor such as time zone, ...
         * Its precision is milisecond.
         * It is defined as number of millisend from 1970-01-01T00:00Z. It is consistent with java.util.Date.
         */
        {
            // You can create it by many ways
            System.out.println("Instants:");
            System.out.println(new Instant());
            System.out.println(Instant.now());
            System.out.println(Instant.parse("2015-01-01"));
            System.out.println(Instant.parse("2015-01-01T12:30"));
            System.out.println(Instant.parse("2015-01-01T12:30:00.505Z"));
        }
        
        /**
         * DateTime
         * It is something like Calendar from Java standard library. But it is immutable.
         * It is most widely used implementation of ReadableInstant.
         * Implementation contains number of mills from 1.1.1970 and chronology, which determines how
         * to convert these mills to date-time fields.
         */
        {
            System.out.println("\nDateTime:");
            DateTime dt = DateTime.now();
            dt = dt.plusDays(3);
            dt = dt.minusDays(3);
            System.out.println("month: " + dt.getMonthOfYear());
            System.out.println("month str: " + dt.monthOfYear().getAsText());
            System.out.println("day of week: " + dt.getDayOfWeek());
            System.out.println("day of week str: " + dt.dayOfWeek().getAsText());
            System.out.println("minute of hour: " + dt.getMinuteOfHour());
            
        }
        
        /**
         *  Partials
         *  It is local representation of time/date. It has no time zone.
         *  It can not be converted to Instant - it represents many instants, according to missing
         *  parameters (time zone, ...). If you want convert, you must specifi these params
         */
        {
            System.out.println("\nPartials:");
            LocalTime lt = new LocalTime(10, 30);
            System.out.println(lt);
        }

    }

}
