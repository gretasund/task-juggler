package de.hsba.bi.projectwork.web.booking;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class BookingForm {

    @NotEmpty(message = "Please choose a date.")
    private String date;

    @Min(value = 1, message = "Please enter a whole number > 0.")
    @Max(value = 10000, message = "Please enter a whole number <= 10,000.")
    private int timeSpent;

}