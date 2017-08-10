package com.hedyhidoury.calendar.horizontallibrary.decorator;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hidou on 8/1/2017.
 */

public interface MonthDecorator {
    void decorate(TextView monthYearTextView, ImageView previousImage, ImageView forwardImage);

}
