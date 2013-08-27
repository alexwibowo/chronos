package com.isolution.chronos.api;

import com.isolution.chronos.domain.TimezoneLabel;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.tz.ZoneInfoProvider;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

/**
 * User: alexwibowo
 */
public class DateTimeUtils {

    /**
     * Return all timezone data provided by the public <a href="www.twinsun.com/tz/tz-link.html"></a>
     * data, otherwise known as the Olson database.
     *
     * @return Set of {@link TimezoneLabel}
     * @throws IOException
     */
    public static Set<TimezoneLabel> getAvailableTimezoneIDs() throws IOException {
        Set<String> availableIDs = new ZoneInfoProvider("org/joda/time/tz/data").getAvailableIDs();
        Set<TimezoneLabel> result = new TreeSet<TimezoneLabel>();
        for (String availableID : availableIDs) {
            TimezoneLabel timezoneLabel = new TimezoneLabel(DateTimeZone.forID(availableID));
            result.add(timezoneLabel);
        }
        return result;
    }

    /**
     * Convert the given datetime into a specified timezone
     *
     * @param from the datetime to be converted
     * @param fromTimeZone timezone for the datetime
     * @param toTimeZone target timezone
     * @return converted datetime in the target timezone
     */
    public static DateTime convert(DateTime from,  DateTimeZone fromTimeZone, DateTimeZone toTimeZone) {
        return from.withZone(fromTimeZone).toDateTime(toTimeZone);
    }
}
