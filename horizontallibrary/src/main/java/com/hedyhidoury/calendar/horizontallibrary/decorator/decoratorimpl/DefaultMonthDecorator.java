package com.hedyhidoury.calendar.horizontallibrary.decorator.decoratorimpl;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.hedyhidoury.calendar.horizontallibrary.decorator.MonthDecorator;


/**
 * Created by hidou on 8/1/2017.
 */

public class DefaultMonthDecorator implements MonthDecorator {


    private Typeface headerTypeFace;
    private Drawable previousHeaderIcon;
    private Drawable forwardHeaderIcon;

    public DefaultMonthDecorator(Typeface headerTypeFace, Drawable previousHeaderIcon, Drawable forwardHeaderIcon) {
        this.headerTypeFace = headerTypeFace;
        this.forwardHeaderIcon = forwardHeaderIcon;
        this.previousHeaderIcon = previousHeaderIcon;
    }

    @Override
    public void decorate(TextView monthYearTextView, ImageView previousImage, ImageView forwardImage) {

        if(headerTypeFace != null){
            monthYearTextView.setTypeface(headerTypeFace);
        }

        if (previousHeaderIcon != null){
            previousImage.setImageDrawable(previousHeaderIcon);
        }

        if(forwardHeaderIcon != null){
            forwardImage.setImageDrawable(forwardHeaderIcon);
        }
    }
}
