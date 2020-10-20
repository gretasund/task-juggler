package de.hsba.bi.projectwork.task;

import java.util.ArrayList;
import java.util.List;


public enum Status {
    IDEA("Idea"),
    DECLINED("Declined"),
    ACCEPTED("Accepted"),
    WORK_IN_PROGRESS("Work in progress"),
    TESTING("Testing"),
    DONE("Done");

    // fields
    private final String displayValue;

    // constructor
    Status(String displayValue) {
        this.displayValue = displayValue;
    }

    // methods
    public String getDisplayValue() {
        return displayValue;
    }

    public static List<Status> getAllStatus() {
        List<Status> allStatus = new ArrayList<>();
        allStatus.add(IDEA);
        allStatus.add(ACCEPTED);
        allStatus.add(WORK_IN_PROGRESS);
        allStatus.add(TESTING);
        allStatus.add(DONE);
        return allStatus;
    }

}
