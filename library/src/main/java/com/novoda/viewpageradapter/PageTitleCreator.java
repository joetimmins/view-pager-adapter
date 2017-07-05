package com.novoda.viewpageradapter;

public interface PageTitleCreator<T> {
    CharSequence createTitle(T item);
}
