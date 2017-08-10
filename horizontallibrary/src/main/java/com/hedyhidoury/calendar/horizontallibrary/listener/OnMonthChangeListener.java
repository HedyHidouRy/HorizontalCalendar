package com.hedyhidoury.calendar.horizontallibrary.listener;

import org.joda.time.DateTime;

/**
 * Created by hidou on 8/1/2017.
 */

public interface OnMonthChangeListener {
    void onMonthChange(DateTime dateTime, boolean onForward);
    void onMonthChangeFromWeeks(DateTime dateTime, boolean onForward);
}
