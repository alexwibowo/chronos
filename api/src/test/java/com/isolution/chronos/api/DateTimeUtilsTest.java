package com.isolution.chronos.api;

import com.isolution.chronos.domain.TimezoneLabel;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * User: alexwibowo
 */
public class DateTimeUtilsTest {

    @Test
    public void should_be_able_to_get_all_available_timezone_ids() throws IOException {
        Set<TimezoneLabel> availableTimezoneIDs = DateTimeUtils.getAvailableTimezoneIDs();
        assertFalse(availableTimezoneIDs.isEmpty());
    }

    @Test
    public void should_be_able_to_get_timezone_ids_in_ascending_timezone_offset() throws IOException {
        Set<TimezoneLabel> availableTimezoneIDs = DateTimeUtils.getAvailableTimezoneIDs();

        TimezoneLabel previousTimezoneLabel = null;
        for (TimezoneLabel currentTimezoneLabel : availableTimezoneIDs) {
            if (previousTimezoneLabel != null) {
                int currentOffset = currentTimezoneLabel.getOffsetInteger();
                int previousOffset = previousTimezoneLabel.getOffsetInteger();
                assertThat("Expected timezone with offset [" + previousOffset +"] to be listed before [" + currentOffset +"]" ,
                        currentOffset, greaterThanOrEqualTo(previousOffset));
            }
            previousTimezoneLabel = currentTimezoneLabel;
        }
    }

    @Test
    public void should_be_able_to_convert_date_between_two_different_timezone() {
        DateTime dateTime = new DateTime()
                .withDayOfMonth(27)
                .withMonthOfYear(8)
                .withYear(2013)
                .withHourOfDay(21)
                .withMinuteOfHour(0)
                .withZone(DateTimeZone.forID("Australia/Melbourne"));

        DateTime converted = DateTimeUtils.convert(dateTime,
                DateTimeZone.forID("Australia/Melbourne"),
                DateTimeZone.forID("Asia/Jakarta"));

        assertThat(converted.getDayOfMonth(), equalTo(27));
        assertThat(converted.getMonthOfYear(), equalTo(8));
        assertThat(converted.getYear(), equalTo(2013));
        assertThat(converted.getHourOfDay(), equalTo(18));
        assertThat(converted.getMinuteOfHour(), equalTo(0));
    }

    @Test
    public void should_be_able_to_convert_date_from_zone_with_daylight_saving_to_zone_without_daylight_saving_correctly() {
        // on 6 October 2013, move the clock forward one hour at 2am
        DateTime dateTime = new DateTime()
                .withDayOfMonth(7)
                .withMonthOfYear(10)
                .withYear(2013)
                .withHourOfDay(21)
                .withMinuteOfHour(0)
                .withZone(DateTimeZone.forID("Australia/Melbourne"));

        DateTime converted = DateTimeUtils.convert(dateTime,
                DateTimeZone.forID("Australia/Melbourne"),
                DateTimeZone.forID("Asia/Jakarta"));

        assertThat(converted.getDayOfMonth(), equalTo(7));
        assertThat(converted.getMonthOfYear(), equalTo(10));
        assertThat(converted.getYear(), equalTo(2013));
        assertThat(converted.getHourOfDay(), equalTo(17));
        assertThat(converted.getMinuteOfHour(), equalTo(0));
    }

    @Test
    public void test_daylight_saving_boundary_at_exact_time_transition() {
        // on 6 October 2013, move the clock forward one hour at 2am
        DateTime australiaTime = new DateTime()
                .withDayOfMonth(6)
                .withMonthOfYear(10)
                .withYear(2013)
                .withHourOfDay(3)
                .withMinuteOfHour(0)
                .withZone(DateTimeZone.forID("Australia/Melbourne"));

        DateTime jakartaTime = DateTimeUtils.convert(australiaTime,
                DateTimeZone.forID("Australia/Melbourne"),
                DateTimeZone.forID("Asia/Jakarta"));

        assertThat(jakartaTime.getDayOfMonth(), equalTo(5));
        assertThat(jakartaTime.getMonthOfYear(), equalTo(10));
        assertThat(jakartaTime.getYear(), equalTo(2013));
        assertThat(jakartaTime.getHourOfDay(), equalTo(23));
        assertThat(jakartaTime.getMinuteOfHour(), equalTo(0));

        DateTime originalAustraliaTime = DateTimeUtils.convert(jakartaTime,
                DateTimeZone.forID("Asia/Jakarta"),
                DateTimeZone.forID("Australia/Melbourne"));
        assertThat("Should be associative",originalAustraliaTime, equalTo(australiaTime));
    }

    @Test
    public void test_daylight_saving_boundary_before_time_transition() {
        // on 6 October 2013, move the clock forward one hour at 2am
        DateTime australiaTime = new DateTime()
                .withDayOfMonth(6)
                .withMonthOfYear(10)
                .withYear(2013)
                .withHourOfDay(1)
                .withMinuteOfHour(59)
                .withSecondOfMinute(59)
                .withZone(DateTimeZone.forID("Australia/Melbourne"));

        DateTime jakartaTime = DateTimeUtils.convert(australiaTime,
                DateTimeZone.forID("Australia/Melbourne"),
                DateTimeZone.forID("Asia/Jakarta"));

        assertThat(jakartaTime.getDayOfMonth(), equalTo(5));
        assertThat(jakartaTime.getMonthOfYear(), equalTo(10));
        assertThat(jakartaTime.getYear(), equalTo(2013));
        assertThat(jakartaTime.getHourOfDay(), equalTo(22));
        assertThat(jakartaTime.getMinuteOfHour(), equalTo(59));
        assertThat(jakartaTime.getSecondOfMinute(), equalTo(59));


        DateTime originalAustraliaTime = DateTimeUtils.convert(jakartaTime,
                DateTimeZone.forID("Asia/Jakarta"),
                DateTimeZone.forID("Australia/Melbourne"));
        assertThat("Should be associative",originalAustraliaTime, equalTo(australiaTime));
    }

}
