package com.telegram.dolarometro.model;

import java.time.LocalDateTime;

public enum EPeriod {

    DAY, WEEK, MONTH, YEAR;

    public LocalDateTime getPeriodStart() {
        LocalDateTime now = LocalDateTime.now();

        switch (this) {
            case DAY:
                return now.toLocalDate().atStartOfDay();
            case WEEK:
                // Monday at 00:00
                return now.toLocalDate()
                    .with(java.time.DayOfWeek.MONDAY)
                    .atStartOfDay();
            case MONTH:
                // First day of the month at 00:00
                return now.toLocalDate()
                    .withDayOfMonth(1)
                    .atStartOfDay();
            case YEAR:
                // First day of the year at 00:00
                return now.toLocalDate()
                    .withDayOfYear(1)
                    .atStartOfDay();
            default:
                return now;
        }
    }
}
