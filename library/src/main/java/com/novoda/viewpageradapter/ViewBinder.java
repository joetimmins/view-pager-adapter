package com.novoda.viewpageradapter;

import android.view.View;

public interface ViewBinder<T, V extends View> {
    void bindView(T item, V view);
}
