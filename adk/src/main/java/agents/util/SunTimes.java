package agents.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SunTimes {

    private SunTimes() {
    }

    public record SunTimesResult(ZonedDateTime sunrise, ZonedDateTime sunset) {
    }

    public static SunTimesResult calculate(double latitude, double longitude, ZonedDateTime date) {
        // Convert the date to UTC
        ZonedDateTime utcDate = date.withZoneSameInstant(ZoneId.of("UTC"));

        int dayOfYear = utcDate.getDayOfYear();

        // Calculate the fractional year in radians
        double fractionalYear = 2 * Math.PI / 365.0 * (dayOfYear - 1 + (utcDate.getHour() - 12) / 24.0);

        // Calculate the equation of time in minutes
        double eqTime = 229.18 * (0.000075 + 0.001868 * Math.cos(fractionalYear) - 0.032077 * Math.sin(fractionalYear)
            - 0.014615 * Math.cos(2 * fractionalYear) - 0.040849 * Math.sin(2 * fractionalYear));

        // Calculate the solar declination angle in radians
        double decl = 0.006918 - 0.399912 * Math.cos(fractionalYear) + 0.070257 * Math.sin(fractionalYear)
            - 0.006758 * Math.cos(2 * fractionalYear) + 0.000907 * Math.sin(2 * fractionalYear)
            - 0.002697 * Math.cos(3 * fractionalYear) + 0.00148 * Math.sin(3 * fractionalYear);

        // Calculate the hour angle in radians
        double latRad = Math.toRadians(latitude);
        double ha = Math.acos(Math.cos(Math.toRadians(90.833)) / (Math.cos(latRad) * Math.cos(decl)) - Math.tan(latRad) * Math.tan(decl));

        // Calculate the sunrise and sunset times in minutes from midnight
        double sunrise = 720 - 4 * (longitude + Math.toDegrees(ha)) - eqTime;
        double sunset = 720 - 4 * (longitude - Math.toDegrees(ha)) - eqTime;

        // Convert the times to ZonedDateTime objects
        ZonedDateTime sunriseTime = utcDate.toLocalDate().atStartOfDay(utcDate.getZone()).plusMinutes((long) sunrise);
        ZonedDateTime sunsetTime = utcDate.toLocalDate().atStartOfDay(utcDate.getZone()).plusMinutes((long) sunset);

        return new SunTimesResult(sunriseTime.withZoneSameInstant(date.getZone()), sunsetTime.withZoneSameInstant(date.getZone()));
    }
}
