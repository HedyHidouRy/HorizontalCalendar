package com.hedyhidoury.calendar.horizontallibrary.decorator;



import android.view.View;
import android.widget.TextView;

import org.joda.time.DateTime;

/**
 * Created by hidou on 7/31/2017.
 */
public interface DayDecorator {
    void decorate(View view, TextView dayTextView, TextView dayNameTextView, DateTime dateTime, DateTime firstDayOfTheWeek, DateTime selectedDateTime);
    void decorateInvalidate(View view, TextView dayTextView, TextView dayNameTextView, DateTime dateTime, DateTime firstDayOfTheWeek, DateTime selectedDateTime);
}