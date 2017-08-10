package com.hedyhidoury.calendar.horizontallibrary.decorator.decoratorimpl;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.hedyhidoury.calendar.horizontallibrary.decorator.WeekDecorator;


/**
 * Created by hidou on 8/9/2017.
 */

public class DefaultWeekDecorator implements WeekDecorator {
    private Drawable previouWeekIcon;
    private Drawable forwardWeekIcon;

    public DefaultWeekDecorator(Drawable previouWeekIcon, Drawable forwardWeekIcon) {
        this.forwardWeekIcon = forwardWeekIcon;
        this.previouWeekIcon = previouWeekIcon;
    }

    @Override
    public void decorate(ImageView previousImage, ImageView forwardImage) {

        if (previouWeekIcon != null){
            previousImage.setImageDrawable(previouWeekIcon);
        }

        if(forwardWeekIcon != null){
            forwardImage.setImageDrawable(forwardWeekIcon);
        }
    }
}
