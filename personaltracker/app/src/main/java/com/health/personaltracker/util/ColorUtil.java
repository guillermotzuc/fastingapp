package com.health.personaltracker.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

import com.health.personaltracker.R;

import java.util.function.Function;

public class ColorUtil {

    public static Function<Context, ColorStateList> DEFAULT_COLOR = (c) -> ColorStateList.valueOf(ContextCompat.getColor(c, R.color.light_orange));
    public static ColorStateList COMPLETION_COLOR = ColorStateList.valueOf(Color.rgb(169, 223, 191));

}
