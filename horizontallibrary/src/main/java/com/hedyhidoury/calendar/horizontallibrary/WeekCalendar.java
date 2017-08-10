package com.hedyhidoury.calendar.horizontallibrary;

/**
 * Created by hidou on 7/31/2017.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hedyhidoury.calendar.horizontallibrary.decorator.DayDecorator;
import com.hedyhidoury.calendar.horizontallibrary.decorator.MonthDecorator;
import com.hedyhidoury.calendar.horizontallibrary.decorator.WeekDecorator;
import com.hedyhidoury.calendar.horizontallibrary.decorator.decoratorimpl.DefaultDayDecorator;
import com.hedyhidoury.calendar.horizontallibrary.decorator.decoratorimpl.DefaultMonthDecorator;
import com.hedyhidoury.calendar.horizontallibrary.decorator.decoratorimpl.DefaultWeekDecorator;
import com.hedyhidoury.calendar.horizontallibrary.eventbus.BusProvider;
import com.hedyhidoury.calendar.horizontallibrary.eventbus.Event;
import com.hedyhidoury.calendar.horizontallibrary.listener.OnDateClickListener;
import com.hedyhidoury.calendar.horizontallibrary.listener.OnMonthChangeListener;
import com.hedyhidoury.calendar.horizontallibrary.listener.OnWeekChangeListener;
import com.hedyhidoury.calendar.horizontallibrary.views.MonthPager;
import com.hedyhidoury.calendar.horizontallibrary.views.WeekPager;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import static com.hedyhidoury.calendar.horizontallibrary.utils.CalendarUtils.AVG_WEEKS_IN_MONTH;
import static com.hedyhidoury.calendar.horizontallibrary.utils.CalendarUtils.DEFAULT_MONTHS_NUMBER;


/**
 * Created by nor on 12/6/2015.
 */
public class WeekCalendar extends LinearLayout implements
        OnWeekChangeListener,
        OnMonthChangeListener {
    private static final String TAG = "WeekCalendarTAG";
    private OnDateClickListener listener;
    private TypedArray typedArray;
    private DayDecorator dayDecorator;
    private MonthDecorator monthDecorator;
    private WeekPager weekPager;
    private MonthPager monthPager;
    public static DateTime actualDate;
    private Drawable hoursBackgroundDrawable;
    private WeekDecorator weekDecorator;

    public WeekCalendar(Context context) {
        super(context);
        init(context, null);
    }

    public WeekCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public WeekCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        actualDate = new DateTime();
        if (attrs != null) {
            typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.WeekCalendar);
            // selected date color
            int nbOfMonths = typedArray.getColor(R.styleable
                    .WeekCalendar_nbOfMonths, DEFAULT_MONTHS_NUMBER);

            // Setting up week view
            // selected date color
            int selectedDateColor = typedArray.getColor(R.styleable
                    .WeekCalendar_selectedBgColor, Color.BLUE);
            // today date color
            int todayDateColor = typedArray.getColor(R.styleable
                    .WeekCalendar_todaysDateBgColor, Color.BLUE);
            // days text color
            int daysTextColor = typedArray.getColor(R.styleable
                    .WeekCalendar_daysTextColor, Color.BLACK);
            // calendar background color
            int calendarBgColor = typedArray.getColor(R.styleable
                    .WeekCalendar_calendarBgColor, Color.WHITE);
            // days of week text size
            float daysTextSize = typedArray.getDimension(R.styleable
                    .WeekCalendar_daysTextSize, -1);
            // today date text color
            int todayDateTextColor = typedArray.getColor(R.styleable
                    .WeekCalendar_todaysDateTextColor, Color.WHITE);
            // week font path
            String weekDayFontPath = typedArray.getString(R.styleable.WeekCalendar_weekTypeFace);
            Typeface weekDayTypeFace;
            if(weekDayFontPath != null){
                weekDayTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/" + weekDayFontPath);
            }else{
                weekDayTypeFace = Typeface.create(Typeface.SANS_SERIF, 0);
            }

            Drawable previousWeekIcon = typedArray.getDrawable(R.styleable.WeekCalendar_weekPreviousIcon);
            if(previousWeekIcon == null){
                previousWeekIcon = getResources().getDrawable(R.drawable.back_arrow);
            }

            Drawable forwardWeekIcon = typedArray.getDrawable(R.styleable.WeekCalendar_weekForwardIcon);
            if(forwardWeekIcon == null){
                forwardWeekIcon = getResources().getDrawable(R.drawable.right_arrow);
            }
            // using decorator for a week view
            setWeekDecorator(new DefaultWeekDecorator(previousWeekIcon,forwardWeekIcon));
            // using decorator for single day
            setDayDecorator(new DefaultDayDecorator(getContext(),
                    selectedDateColor,
                    todayDateColor,
                    todayDateTextColor,
                    daysTextColor,
                    daysTextSize
                    , weekDayTypeFace));
            setBackgroundColor(calendarBgColor);

            // Setting up month view
            // get header month and year typeface
            String headerFontPath = typedArray.getString(R.styleable.WeekCalendar_headerTypeFace);
            Typeface headerTypeFace;
            if(headerFontPath != null){
                headerTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/" + headerFontPath);
            }else{
                headerTypeFace = Typeface.create(Typeface.SANS_SERIF, 0);
            }

            Drawable previousHeaderIcon = typedArray.getDrawable(R.styleable.WeekCalendar_headerPreviousIcon);
            if(previousHeaderIcon == null){
                previousHeaderIcon = getResources().getDrawable(R.drawable.back_arrow);
            }

            Drawable forwardHeaderIcon = typedArray.getDrawable(R.styleable.WeekCalendar_headerForwardIcon);
            if(forwardHeaderIcon == null){
                forwardHeaderIcon = getResources().getDrawable(R.drawable.right_arrow);
            }

            setMonthDecorator(new DefaultMonthDecorator(headerTypeFace,previousHeaderIcon,forwardHeaderIcon));

            int marginViews = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2 , getResources().getDisplayMetrics());

            // setting up current view
            setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            setOrientation(VERTICAL);

            // adding month view
            LayoutParams monthParamsLayout = new LayoutParams(LayoutParams.MATCH_PARENT,
                    (int)getResources().getDimension(R.dimen.full_month_view_height));
            monthParamsLayout.setMargins(0,marginViews,0,marginViews);
            monthPager = new MonthPager(getContext(), nbOfMonths, idCheck(), this);
            addView(monthPager,monthParamsLayout);

            // Adding week view
            LayoutParams weekParamsLayout = new LayoutParams(LayoutParams.MATCH_PARENT,
                    (int)getResources().getDimension(R.dimen.full_week_view_height));
            weekPager = new WeekPager(getContext(),(int)( nbOfMonths * AVG_WEEKS_IN_MONTH ),idCheck(),this,this);
            addView(weekPager,weekParamsLayout);

            // Adding hours view
            // all hours params
            LayoutParams hoursLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            hoursLayoutParams.setMargins((int)getResources().getDimension(R.dimen.full_hours_text_margin),
                    0,(int)getResources().getDimension(R.dimen.full_hours_text_margin),0);
            // hour item params
            LayoutParams hourLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                    (int)getResources().getDimension(R.dimen.full_hours_text_height));

            // hours font
            String hoursFontPath = typedArray.getString(R.styleable.WeekCalendar_hoursTypeFace);
            Typeface hoursTypeFace;
            if(hoursFontPath != null){
                hoursTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/" + weekDayFontPath);
            }else{
                hoursTypeFace = Typeface.create(Typeface.SANS_SERIF, 0);
            }

            String[] hoursArray = getResources().getStringArray(R.array.hours_of_day);
            RadioButton[] radiobutton = new RadioButton[hoursArray.length];

            final  RadioGroup radiogroup = new RadioGroup(context);
            radiogroup.setLayoutParams(hoursLayoutParams);
            for (int i = 0; i < hoursArray.length; i++) {
                radiobutton[i] = new RadioButton(context);
                radiobutton[i].setText(hoursArray[i]);
                radiobutton[i].setTypeface(hoursTypeFace);
                radiobutton[i].setId(i + 100);
                radiobutton[i].setButtonDrawable(R.drawable.hour_checkbox); // making our own custom drawabe for checkbox
                radiobutton[i].setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelOffset(R.dimen.hours_text_size));
                radiobutton[i].setPadding((int)getResources().getDimension(R.dimen.padding_hour_from_check),0,0,0);
                radiobutton[i].setLayoutParams(hourLayoutParams);
                radiobutton[i].setBackgroundResource(R.drawable.hours_bg);

                radiogroup.addView(radiobutton[i]);

            }
            addView(radiogroup);
        }

        invalidate();
    }


    /***
     * Do not use this method
     * this is for receiving date,
     * use "setOndateClick" instead.
     */
    @Subscribe
    public void onDateClick(Event.OnDateClickEvent event) {
        if (listener != null)
            listener.onDateClick(event.getDateTime());
    }

    @Subscribe
    public void onDayDecorate(Event.OnDayDecorateEvent event) {
        if (dayDecorator != null) {
            dayDecorator.decorate(event.getView(), event.getDayTextView(), event.getDayNameTextView(),event.getDateTime(),
                    event.getFirstDay(), event.getSelectedDateTime());
        }
    }

    @Subscribe
    public void onWeekDecorate(Event.OnWeekDecorateEvent event) {
        if (weekDecorator != null) {
            weekDecorator.decorate(event.getPreviousImage(), event.getForwardImage());
        }
    }


    @Subscribe
    public void onMonthDecorate(Event.OnMonthDecorate event) {
        if (monthDecorator != null) {
            monthDecorator.decorate(event.getDateHeaderTitle(), event.getPreviousHeaderImage(), event.getForwardHeaderImage());
        }
    }

    public void setOnDateClickListener(OnDateClickListener listener) {
        this.listener = listener;
    }

    public void setDayDecorator(DayDecorator decorator) {
        this.dayDecorator = decorator;
    }

    public void setWeekDecorator(WeekDecorator decorator){
        this.weekDecorator = decorator;
    }

    public void setMonthDecorator(MonthDecorator monthDecorator) {
        this.monthDecorator = monthDecorator;
    }

    /**
     * Renders the days again. If you depend on deferred data which need to update the calendar
     * after it's resolved to decorate the days.
     */
    public void updateUi() {
        BusProvider.getInstance().post(new Event.OnUpdateUi());
    }

    public void moveToPrevious() {
        BusProvider.getInstance().post(new Event.UpdateSelectedDateEvent(-1));
    }

    public void moveToNext() {
        BusProvider.getInstance().post(new Event.UpdateSelectedDateEvent(1));
    }

    public void reset() {
        BusProvider.getInstance().post(new Event.ResetEvent());
    }

    public void setSelectedDate(DateTime selectedDate) {
        BusProvider.getInstance().post(new Event.SetSelectedDateEvent(selectedDate));
    }

    public void setStartDate(DateTime startDate) {
        BusProvider.getInstance().post(new Event.SetStartDateEvent(startDate));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BusProvider.getInstance().unregister(this);
        BusProvider.disposeInstance();
    }
    private int idCheck() {
        int id = 0;
        while (true) {
            if (findViewById(++id) == null) break;
        }
        return id;

    }

    public static StateListDrawable getHourCheckSelector(Drawable checkedIcon, Drawable unCheckedIcon) {
        StateListDrawable res = new StateListDrawable();
        /*res.addState(new int[]{android.R.attr.state_selected, android.R.attr.state_checked, android.R.attr.state_window_focused }, checkedIcon);
        res.addState(new int[] {-android.R.attr.state_checked, android.R.attr.state_window_focused }, unCheckedIcon);*/
        res.addState (new int[]{ android.R.attr.state_pressed },
                checkedIcon);
        res.addState (new int[]{ }, unCheckedIcon);
        return res;
    }

    @Override
    public void onWeekChange(DateTime firstDayOfTheWeek, boolean forward) {

    }

    @Override
    public void onWeekChange(DateTime firstDayOfTheWeek, boolean forward, boolean isFromMonthSwipe) {

    }

    @Override
    public void onMonthChange(DateTime dateTime, boolean onForward) {
        if(weekPager != null){
            weekPager.onMonthDateChanged(dateTime,onForward);
        }
    }

    @Override
    public void onMonthChangeFromWeeks(DateTime dateTime, boolean onForward) {
        if(monthPager != null){
             monthPager.onSwipeRequested(dateTime,onForward, false);
        }
    }
}