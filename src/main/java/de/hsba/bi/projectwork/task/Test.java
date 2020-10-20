package de.hsba.bi.projectwork.task;

public enum Test {
    ACCEPTED("Accepted"),
    WORK_IN_PROGRESS("Work in progress"),
    TESTING("Testing"),
    DONE("Done");

    // fields
    private final String displayValue;

    // constructor
    Test(String displayValue) {
        this.displayValue = displayValue;
    }
}
