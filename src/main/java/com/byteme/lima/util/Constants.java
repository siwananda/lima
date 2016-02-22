package com.byteme.lima.util;

public interface Constants {
    interface Avatar {
        String ADMIN = "default-avatar-admin.png";
        String USER = "default-avatar-user.png";
    }

    interface Dates {
        Long ONE_SECOND = 1000L;
        Long ONE_MINUTE = 60L * ONE_SECOND;
        Long ONE_HOUR   = 60L * ONE_MINUTE;
        Long ONE_DAY    = 24L * ONE_HOUR;
        Long ONE_WEEK   = 7L * ONE_DAY;
        Long DUE_DAYS = 3L;
    }
}
