package cz.kul.snippets.java.example13_java8features;

import org.junit.Assert;
import org.junit.Test;

import java.time.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestJavaTime {

    @Test
    public void overview() {
        // Advantages in comparison with old API
        // * new API is immutable - no setters. So it is thread safe.
        // * old API has poor design - month starts from 1, days starts from 0
        // * much easier timezone handling

        {
            // Local Date and time
            //
            // Use this when you do not need to handle timezone.
            
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();

            LocalDate particularDate = LocalDate.of(2017, Month.JANUARY, 10);
            Assert.assertEquals(1, particularDate.getMonthValue());

            LocalDate first = particularDate.withDayOfMonth(1);
            Assert.assertEquals(1, first.getDayOfMonth());

            LocalDate parsedDate = LocalDate.parse("2017-01-02");
            Assert.assertEquals(2017, parsedDate.getYear());
            Assert.assertEquals(1, parsedDate.getMonthValue());
            Assert.assertEquals(2, parsedDate.getDayOfMonth());

            LocalTime parsedTime = LocalTime.parse("16:10:22");
            Assert.assertEquals(16, parsedTime.getHour());
            Assert.assertEquals(10, parsedTime.getMinute());
            Assert.assertEquals(22, parsedTime.getSecond());
        }

        {
            // Instant
            //
            // It is just point on the timeline. It has different local time in every
            // timezone.
            // But this instance does not know nothing about timezone or offset.
            Instant startOfEpoch = Instant.ofEpochSecond(0);

            // OffsetDateTime
            //
            // It adds to instance an offset in comparison with UTC/Greenwich. You can getAppender local
            // date and time from it.
            OffsetDateTime offsetPlusFive = startOfEpoch.atOffset(ZoneOffset.of("+5"));
            assertTrue(startOfEpoch.equals(offsetPlusFive.toInstant()));
            assertEquals(5, offsetPlusFive.getHour());
            LocalDateTime local = offsetPlusFive.toLocalDateTime();
            assertEquals(5, local.getHour());

            // ZonedDateTime
            //
            // Add complete zone information (daylight saving, ... not the offset only)
            // to instance
            ZonedDateTime newYork = startOfEpoch.atZone(ZoneId.of("America/New_York"));
            assertTrue(startOfEpoch.equals(newYork.toInstant()));
            assertEquals(1969, newYork.toLocalDate().getYear());
            
            // Period
            //
            // The Period class represents a quantity of time in terms of years, months and days 
            LocalDate ld1 = LocalDate.parse("2019-08-01");
            LocalDate ld2 = LocalDate.parse("2020-07-10");
            Period p1 = Period.between(ld1, ld2);
            assertEquals(0, p1.getYears());
            assertEquals(11, p1.getMonths());
            assertEquals(9, p1.getDays());

            // Duration
            //
            // The Duration class represents a quantity of time in terms of seconds and nano seconds.
            LocalTime t1 = LocalTime.now();
            LocalTime t2 = t1
                    .plusHours(1)
                    .plusMinutes(1)
                    .plusSeconds(10);
            Duration d1 = Duration.between(t1, t2);
            assertEquals(3600 + 60 + 10, d1.getSeconds());
            assertEquals(0, d1.getNano());
        }

    }
}
