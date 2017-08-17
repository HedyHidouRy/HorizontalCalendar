package com.hedyhidoury.calendar.horizontallibrary.utils;

/**
 * Created by hidou on 7/31/2017.
 */

import android.support.annotation.NonNull;
import android.util.MonthDisplayHelper;


import com.hedyhidoury.calendar.horizontallibrary.Day;

import org.joda.time.DateTime;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public final class CalendarUtils {

    public final static double AVG_WEEKS_IN_MONTH = 4.35;
    public final static int DEFAULT_MONTHS_NUMBER = 48;
    public final static String DEFAULT_END_OF_HOURS = ":00";
    public final static String DEFALT_HOURS_FORMAT = "hh:mm";
    public static DateTime selectedDateTime = new DateTime();
}
