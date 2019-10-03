package getjobin.it.portal.elasticservice.infrastructure;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CurrentDate {

    private static final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Poland"));

    public static Date get() {
        return calendar.getTime();
    }
}
