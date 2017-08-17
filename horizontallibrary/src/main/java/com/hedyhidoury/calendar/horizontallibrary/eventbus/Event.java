package com.hedyhidoury.calendar.horizontallibrary.eventbus;

/**
 * Created by hidou on 7/31/2017.
 */

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;

/**
 * Created by nor on 12/5/2015.
 */
public class Event {

    /**
     * On date clicked event
     */
    public static class OnDateClickEvent {
        public OnDateClickEvent(DateTime dateTime) {
            this.dateTime = dateTime;
        }

        private DateTime dateTime;

        public DateTime getDateTime() {
            return dateTime;
        }
    }

    public static class InvalidateEvent {
    }

    public static class UpdateSelectedDateEvent {
        /***
         * Direction -1 for background and 1 for forward
         *
         * @param direction
         */
        public UpdateSelectedDateEvent(int direction) {
            this.direction = direction;
        }

        public int getDirection() {
            return direction;
        }

        private int direction;
    }

    /**
     * set current page event
     */
    public static class SetCurrentPageEvent {
        public int getDirection() {
            return direction;
        }

        public SetCurrentPageEvent(int direction) {

            this.direction = direction;
        }

        private int direction;
    }

    public static class ResetEvent {
    }

    /**
     * Set selected date event
     */
    public static class SetSelectedDateEvent {
        public SetSelectedDateEvent(DateTime selectedDate) {
            this.selectedDate = selectedDate;
        }

        public DateTime getSelectedDate() {
            return selectedDate;
        }

        private DateTime selectedDate;
    }

    /**
     * Setting start date event
     */
    public static class SetStartDateEvent {


        public SetStartDateEvent(DateTime startDate) {
            this.startDate = startDate;
        }

        public DateTime getStartDate() {
            return startDate;
        }

        private DateTime startDate;
    }

    /**
     * ond day decorate event
     */
    public static class OnDayDecorateEvent {

        private final View view;
        private final TextView dayTextView;
        private final TextView dayNameTextView;
        private final DateTime dateTime;
        private final boolean invalidate;
        private DateTime firstDay;
        private DateTime selectedDateTime;

        public OnDayDecorateEvent(View view, TextView dayTextView, TextView dayNameTextView, DateTime dateTime,
                                  DateTime firstDayOfTheWeek, DateTime selectedDateTime, boolean invalidate) {
            this.view = view;
            this.dayTextView = dayTextView;
            this.dateTime = dateTime;
            this.firstDay = firstDayOfTheWeek;
            this.selectedDateTime = selectedDateTime;
            this.dayNameTextView = dayNameTextView;
            this.invalidate = invalidate;
        }

        public View getView() {
            return view;
        }

        public TextView getDayTextView() {
            return dayTextView;
        }

        public DateTime getDateTime() {
            return dateTime;
        }

        public DateTime getFirstDay() {
            return firstDay;
        }

        public DateTime getSelectedDateTime() {
            return selectedDateTime;
        }

        public TextView getDayNameTextView() {
            return dayNameTextView;
        }

        public boolean isInvalidate() {
            return invalidate;
        }
    }

    public static class OnWeekDecorateEvent {

        private final ImageView previousImage;
        private final ImageView forwardImage;


        public OnWeekDecorateEvent(ImageView previousImage, ImageView forwardImage) {
            this.previousImage = previousImage;
            this.forwardImage = forwardImage;
        }

        public ImageView getPreviousImage() {
            return previousImage;
        }

        public ImageView getForwardImage() {
            return forwardImage;
        }
    }
    /**
     * On Week change
     */
    public static class OnWeekChange {

        private final DateTime firstDayOfTheWeek;
        private final boolean forward;

        public OnWeekChange(DateTime firstDayOfTheWeek, boolean isForward) {
            this.firstDayOfTheWeek = firstDayOfTheWeek;
            this.forward = isForward;
        }

        public DateTime getFirstDayOfTheWeek() {
            return firstDayOfTheWeek;
        }

        public boolean isForward() {
            return forward;
        }
    }

    /**
     * On Month Change, used to scroll for month view
     */
    public static class OnMonthChange {

        private final DateTime monthDay;
        private final boolean forward;

        public OnMonthChange(DateTime monthDay, boolean isForward) {
            this.monthDay = monthDay;
            this.forward = isForward;
        }

        public DateTime getMonthDay() {
            return monthDay;
        }

        public boolean isForward() {
            return forward;
        }
    }

    /**
     * If Week view swipe requested
     */
    public static class OnSwipeWeekRequested {

        private final boolean forward;

        public OnSwipeWeekRequested(boolean isForward) {
            this.forward = isForward;
        }

        public boolean isForward() {
            return forward;
        }
    }

    /**
     * On Month Swipe requested
     */
    public static class OnSwipeMonthRequested {

        private final boolean forward;
        private boolean isFromMonthSwipe;

        public OnSwipeMonthRequested(boolean isForward, boolean isFromMonthSwipe) {
            this.forward = isForward;
            this.isFromMonthSwipe = isFromMonthSwipe;
        }

        public boolean isForward() {
            return forward;
        }

        public boolean isFromMonthSwipe() {
            return isFromMonthSwipe;
        }
    }

    public static class OnMonthDecorate {

        private TextView dateHeaderTitle;
        private ImageView previousHeaderImage;
        private ImageView forwardHeaderImage;
        public OnMonthDecorate(TextView dateHeaderTitle, ImageView previousHeaderImage, ImageView forwardHeaderImage) {
            this.dateHeaderTitle = dateHeaderTitle;
            this.previousHeaderImage= previousHeaderImage;
            this.forwardHeaderImage= forwardHeaderImage;
        }

        public TextView getDateHeaderTitle() {
            return dateHeaderTitle;
        }

        public ImageView getPreviousHeaderImage() {
            return previousHeaderImage;
        }

        public ImageView getForwardHeaderImage() {
            return forwardHeaderImage;
        }
    }

    public static class OnMonthDateChanged {

        private DateTime dateTime;
        private boolean isForward;
        public OnMonthDateChanged(DateTime dateTime, boolean isForward) {
            this.dateTime = dateTime;
            this.isForward = isForward;
        }

        public DateTime getDateTime() {
            return dateTime;
        }

        public boolean isForward() {
            return isForward;
        }
    }

    public static class OnUpdateUi {
    }
}
