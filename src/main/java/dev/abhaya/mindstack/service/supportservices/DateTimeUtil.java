package dev.abhaya.mindstack.service.supportservices;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");


    public static String format(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault())
                .format(FORMATTER);
    }
}
