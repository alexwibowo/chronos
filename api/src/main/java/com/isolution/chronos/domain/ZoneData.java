package com.isolution.chronos.domain;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

/**
 * User: alexwibowo
 */
public class ZoneData implements Comparable {
    private final String iID;
    private final DateTimeZone iZone;

    ZoneData(String id, DateTimeZone zone) {
        iID = id;
        iZone = zone;
    }

    public String getID() {
        return iID;
    }

    public String getCanonicalID() {
        return iZone.getID();
    }

    public boolean isCanonical() {
        return getID().equals(getCanonicalID());
    }

    public String getStandardOffsetStr() {
        long millis = System.currentTimeMillis();
        while (iZone.getOffset(millis) != iZone.getStandardOffset(millis)) {
            millis = iZone.nextTransition(millis);
        }
        DateTimeFormatter cOffsetFormatter = new DateTimeFormatterBuilder()
                  .appendTimeZoneOffset(null, true, 2, 4)
                  .toFormatter();

        return cOffsetFormatter.withZone(iZone).print(millis);
    }

    public int compareTo(Object obj) {
        long cNow = System.currentTimeMillis();
        ZoneData other = (ZoneData) obj;

        int offsetA = iZone.getStandardOffset(cNow);
        int offsetB = other.iZone.getStandardOffset(cNow);

        if (offsetA < offsetB) {
            return -1;
        }
        if (offsetA > offsetB) {
            return 1;
        }

        int result = getCanonicalID().compareTo(other.getCanonicalID());

        if (result != 0) {
            return result;
        }

        if (isCanonical()) {
            if (!other.isCanonical()) {
                return -1;
            }
        } else if (other.isCanonical()) {
            return 1;
        }

        return getID().compareTo(other.getID());
    }
}