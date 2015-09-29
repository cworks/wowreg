package net.cworks;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.UUID;

public class Wow {

    public static String eventId() {
        return System.getProperty("eventId", "WoW_2016");
    }

    public static DateTime now() {
        return DateTime.now(DateTimeZone.UTC);
    }

    public synchronized static String nextRegistrationId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public synchronized static String nextGroupId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String nextId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


}
