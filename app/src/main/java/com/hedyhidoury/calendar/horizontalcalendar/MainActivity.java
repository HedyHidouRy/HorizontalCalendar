package com.hedyhidoury.calendar.horizontalcalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.hedyhidoury.calendar.horizontallibrary.HorizontalCalendarView;
import com.hedyhidoury.calendar.horizontallibrary.listener.OnDatePickedListener;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HorizontalCalendarView horizontalCalendarView = (HorizontalCalendarView)findViewById(R.id.horizontal);
        horizontalCalendarView.setHoursRange("10h30","17h00");
        horizontalCalendarView.setInvalidatedDays(HorizontalCalendarView.THURSDAY,HorizontalCalendarView.FRIDAY);
        horizontalCalendarView.setDatePickedListener(new OnDatePickedListener() {
            @Override
            public void OnDatePicked(Date datePicked) {
                Toast.makeText(MainActivity.this,datePicked.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
