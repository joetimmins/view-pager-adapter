package com.novoda.viewpageradapter;

import android.view.View;
import android.view.ViewGroup;

public interface ViewCreator<T, V extends View> {
    V createView(T item, ViewGroup container);
}
