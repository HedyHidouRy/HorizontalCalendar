Android Horizontal Calendar
=================

**Android Horizontal Calendar** is an Android library to display of full calendar ( especially to take exact appointement ). It supports custom styling.

![](images/screen-shot.png)

Features
------------

* Month Swiper
* Week Swiper
* Hour Picker

Who uses it
---------------

* Using the library? Just [send me an email](mailto:hedyalhidoury@gmail.com).

Usage
---------

1. Import the library into your project.

  * Grab via gradle
  
    ```groovy
    compile 'com.github.HedyHidouRy:HorizontalCalendar:1.00'
    ```
    
    ```groovy
    repositories {
			...
			maven { url 'https://jitpack.io' }
		}
    ```
    
2. Add WeekView in your xml layout.

    ```xml
    <com.hedyhidoury.calendar.horizontallibrary.HorizontalCalendarView
        android:id="@+id/horizontal"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:selectedBgColor="#89cacf"
        app:todaysDateBgColor="#89cacf"
        app:calendarBgColor="@android:color/white"
        app:daysTextColor="@android:color/black"
        app:weekTypeFace="museosans.otf"
        app:headerPreviousIcon="@drawable/ic_backward"
        app:headerForwardIcon="@drawable/ic_forward"
        app:weekForwardIcon="@drawable/ic_forward"
        app:weekPreviousIcon="@drawable/ic_backward"
        app:hoursTypeFace="MuseoSans_500.otf"
        app:headerTypeFace="MuseoSans_700.otf"
        />
    ```
3. Write the following code in your java file to make those options.

    ```java
        // Set hours range, hours format are with this format
        horizontalCalendarView.setHoursRange("10h30","17h00");
        // Set invalidate days of the week
        horizontalCalendarView.setInvalidatedDays(HorizontalCalendarView.THURSDAY,HorizontalCalendarView.FRIDAY);
        // Set DatePickedListener
        horizontalCalendarView.setDatePickedListener(new OnDatePickedListener() {
            @Override
            public void OnDatePicked(Date datePicked) {
                Toast.makeText(MainActivity.this,datePicked.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    ```
4. Implement `OnDatePickedListener` according to your need.

Customization
-------------------

You can customize the look of the `WeekView` in xml. Use the following attributes in xml. All these attributes also have getters and setters to enable you to change the style dynamically.

- `nbOfMonths`
- `daysTextSize`
- `daysTextColor`
- `calendarBgColor`
- `daysBackgroundColor`
- `weekTextSize`
- `weekTextColor`
- `weekBackgroundColor`
- `selectedBgColor`
- `todaysDateBgColor`
- `weekTypeFace`
- `headerTypeFace`
- `headerForwardIcon`
- `weekForwardIcon`
- `headerPreviousIcon`
- `weekPreviousIcon`
- `hoursTypeFace`

Sample
----------

There is also a [sample app](https://github.com/HedyHidouRy/HorizontalCalendar/tree/master/app) to get you started.

To do
-------

* Make calendar more smooth
* Adding new features
