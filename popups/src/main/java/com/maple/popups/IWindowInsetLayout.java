package com.maple.popups;

import android.graphics.Rect;

import androidx.core.view.WindowInsetsCompat;

public interface IWindowInsetLayout {

    boolean applySystemWindowInsets19(Rect insets);

    WindowInsetsCompat applySystemWindowInsets21(WindowInsetsCompat insets);
}
