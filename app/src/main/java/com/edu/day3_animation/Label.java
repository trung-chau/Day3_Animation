package com.edu.day3_animation;

public enum Label {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private final String day;

    Label(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }
}
