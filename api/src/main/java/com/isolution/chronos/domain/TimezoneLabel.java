package com.isolution.chronos.domain;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.TimeZone;

/**
 * User: alexwibowo
 */
public class TimezoneLabel implements Comparable{
    private final DateTimeZone dateTimeZone;
    private final long offset;
    private final String canonicalId;
    private final String displayName;
    private final String offsetLabel;
    private final Integer offsetInteger;

    private final DateTimeFormatter dtf = DateTimeFormat.forPattern("ZZ");

    public TimezoneLabel(DateTimeZone dateTimeZone) {
        this.dateTimeZone = dateTimeZone;
        this.offset = dateTimeZone.getOffset(0) / (1000L * 60L * 60L);
        this.canonicalId = dateTimeZone.getID();
        TimeZone timeZone = TimeZone.getTimeZone(dateTimeZone.getID());
        this.displayName = timeZone.getDisplayName();

        this.offsetLabel = dtf.withZone(DateTimeZone.forID(this.canonicalId)).print(0);
        this.offsetInteger = Integer.valueOf(offsetLabel.replaceAll("[\\+:]", ""));
    }

    @Override
    public boolean equals(Object another) {
        if (!(another instanceof TimezoneLabel)) {
            return false;
        }
        TimezoneLabel another1 = (TimezoneLabel) another;
        return this.canonicalId.equals(another1.canonicalId);
    }

    public String getCanonicalId() {
        return canonicalId;
    }

    @Override
    public int hashCode() {
        return canonicalId.hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder(displayName).append("(").append(canonicalId).append(")").append("(").append(offsetLabel).append(")").toString();
    }

    public String getOffsetLabel() {
        return offsetLabel;
    }

    public int getOffsetInteger() {
        return offsetInteger;
    }

    @Override
    public int compareTo(Object another) {
        if (!(another instanceof TimezoneLabel)) {
            throw new IllegalArgumentException();
        }
        TimezoneLabel another1 = (TimezoneLabel) another;
        if (this.offsetInteger < another1.offsetInteger) {
            return -1;
        }else if (this.offsetInteger == another1.offsetInteger) {
            return this.displayName.compareTo(another1.displayName);
        } else {
            return 1;
        }
    }
}
