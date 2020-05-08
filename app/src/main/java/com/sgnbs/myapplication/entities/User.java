package com.sgnbs.myapplication.entities;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

public class User {

    public final ObservableInt id = new ObservableInt();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableInt age = new ObservableInt();


    @BindingAdapter("pic")
    public static void  setImage(ImageView iv, Drawable drawable) {
        iv.setImageDrawable(drawable);
    }
}
