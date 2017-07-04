package com.novoda.viewpageradapter.demo;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.novoda.viewpageradapter.ViewBinder;
import com.novoda.viewpageradapter.ViewCreator;
import com.novoda.viewpageradapter.ViewPagerAdapter;

import java.util.List;

class PagesAdapterFactory {

    private final ItemClickListener listener;
    private List<Page> pages;

    PagesAdapterFactory(ItemClickListener listener, List<Page> pages) {
        this.listener = listener;
        this.pages = pages;
    }

    private ViewCreator<Page, RecyclerView> viewCreator = new ViewCreator<Page, RecyclerView>() {
        @Override
        public RecyclerView createView(Page item, ViewGroup container) {
            Log.d("VPA", "createView: " + item);

            RecyclerView view = (RecyclerView) LayoutInflater.from(container.getContext()).inflate(R.layout.view_page, container, false);
            view.setLayoutManager(new LinearLayoutManager(container.getContext()));
            view.setItemAnimator(null); // workaround for bug - https://code.google.com/p/android/issues/detail?id=204277

            return view;
        }
    };

    private ViewBinder<Page, RecyclerView> viewBinder = new ViewBinder<Page, RecyclerView>() {
        @Override
        public void bindView(Page item, RecyclerView view) {
            Log.d("VPA", "bindView: " + item);

            view.swapAdapter(new PageItemsAdapter(listener, item), false);
        }
    };

    ViewPagerAdapter<Page, RecyclerView> build() {
        return new ViewPagerAdapter<>(pages, viewCreator, viewBinder);
    }

}
