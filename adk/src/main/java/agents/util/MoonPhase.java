package agents.util;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class MoonPhase {

    private MoonPhase() {
    }

    public enum Phase {
        NEW_MOON("New Moon"),
        WAXING_CRESCENT("Waxing Crescent"),
        FIRST_QUARTER("First Quarter"),
        WAXING_GIBBOUS("Waxing Gibbous"),
        FULL_MOON("Full Moon"),
        WANING_GIBBOUS("Waning Gibbous"),
        LAST_QUARTER("Last Quarter"),
        WANING_CRESCENT("Waning Crescent");

        private final String name;

        Phase(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    // A known new moon date
    private static final ZonedDateTime KNOWN_NEW_MOON = ZonedDateTime.parse("2000-01-06T18:14:00Z");

    // The length of a lunar cycle in days
    private static final double LUNAR_CYCLE = 29.53058867;

    public static Phase getPhase(ZonedDateTime date) {
        long daysSinceKnownNewMoon = ChronoUnit.DAYS.between(KNOWN_NEW_MOON, date);
        double cycles = daysSinceKnownNewMoon / LUNAR_CYCLE;
        double intoCycle = (cycles - Math.floor(cycles)) * LUNAR_CYCLE;

        if (intoCycle < 1.84566) {
            return Phase.NEW_MOON;
        } else if (intoCycle < 5.53699) {
            return Phase.WAXING_CRESCENT;
        } else if (intoCycle < 9.22831) {
            return Phase.FIRST_QUARTER;
        } else if (intoCycle < 12.91963) {
            return Phase.WAXING_GIBBOUS;
        } else if (intoCycle < 16.61096) {
            return Phase.FULL_MOON;
        } else if (intoCycle < 20.30228) {
            return Phase.WANING_GIBBOUS;
        } else if (intoCycle < 23.99361) {
            return Phase.LAST_QUARTER;
        } else if (intoCycle < 27.68493) {
            return Phase.WANING_CRESCENT;
        } else {
            return Phase.NEW_MOON;
        }
    }
    
    public static String getPhaseName(ZonedDateTime date) {
        return getPhase(date).getName();
    }
}
