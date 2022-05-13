package me.eastcause.bans.utils.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {

    private static Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);

    public static String longToStringTime(long l) {
        long time = (l - System.currentTimeMillis());
        long rest = (time % 1000);
        int seconds = (int) (time / 1000);
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Integer, TimeUnit> entry : TimeUnit.getUNITS().entrySet()) {
            int ad = seconds / entry.getKey();
            if (ad >= 1) {
                int x = (int) Math.floor(ad);
                stringBuilder.append(x + entry.getValue().getId() + " ");
                seconds -= (x * entry.getKey());
            }
        }
        String result = stringBuilder.toString();
        if (result.isEmpty()) {
            return "<1s";
        }
        return result;
    }

    public static long parseDateDiff(String time, boolean future) {
        try {
            Matcher m = timePattern.matcher(time);
            int years = 0;
            int months = 0;
            int weeks = 0;
            int days = 0;
            int hours = 0;
            int minutes = 0;
            int seconds = 0;
            boolean found = false;
            while (m.find()) {
                if (m.group() == null || m.group().isEmpty()) {
                    continue;
                }
                for (int i = 0; i < m.groupCount(); i++) {
                    if (m.group(i) != null && !m.group(i).isEmpty()) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    if (m.group(1) != null && !m.group(1).isEmpty()) {
                        years = Integer.parseInt(m.group(1));
                    }
                    if (m.group(2) != null && !m.group(2).isEmpty()) {
                        months = Integer.parseInt(m.group(2));
                    }
                    if (m.group(3) != null && !m.group(3).isEmpty()) {
                        weeks = Integer.parseInt(m.group(3));
                    }
                    if (m.group(4) != null && !m.group(4).isEmpty()) {
                        days = Integer.parseInt(m.group(4));
                    }
                    if (m.group(5) != null && !m.group(5).isEmpty()) {
                        hours = Integer.parseInt(m.group(5));
                    }
                    if (m.group(6) != null && !m.group(6).isEmpty()) {
                        minutes = Integer.parseInt(m.group(6));
                    }
                    if (m.group(7) != null && !m.group(7).isEmpty()) {
                        seconds = Integer.parseInt(m.group(7));
                    }
                    break;
                }
            }
            if (!found) {
                return -1L;
            }
            Calendar c = new GregorianCalendar();
            if (years > 0) {
                c.add(Calendar.YEAR, years * (future ? 1 : -1));
            }
            if (months > 0) {
                c.add(Calendar.MONTH, months * (future ? 1 : -1));
            }
            if (weeks > 0) {
                c.add(Calendar.WEEK_OF_YEAR, weeks * (future ? 1 : -1));
            }
            if (days > 0) {
                c.add(Calendar.DAY_OF_MONTH, days * (future ? 1 : -1));
            }
            if (hours > 0) {
                c.add(Calendar.HOUR_OF_DAY, hours * (future ? 1 : -1));
            }
            if (minutes > 0) {
                c.add(Calendar.MINUTE, minutes * (future ? 1 : -1));
            }
            if (seconds > 0) {
                c.add(Calendar.SECOND, seconds * (future ? 1 : -1));
            }
            Calendar max = new GregorianCalendar();
            max.add(Calendar.YEAR, 10);
            if (c.after(max)) {
                return max.getTimeInMillis();
            }
            return c.getTimeInMillis();
        }catch (Exception e){

        }
        return -1;
    }

    private static final SimpleDateFormat dateFormat;
    private static final SimpleDateFormat timeFormat;
    private static Calendar calendar;

    public static String getDate(final long time) {
        return dateFormat.format(new Date(time));
    }

    public static String getTime(final long time) {
        return timeFormat.format(new Date(time));
    }

    static {
        dateFormat = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        calendar = Calendar.getInstance();
    }

}
