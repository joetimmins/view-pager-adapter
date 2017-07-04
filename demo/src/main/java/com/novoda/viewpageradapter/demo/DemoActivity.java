package com.novoda.viewpageradapter.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;

import com.novoda.viewpageradapter.ViewPagerAdapter;

public class DemoActivity extends Activity implements ItemClickListener {

    private ViewPagerAdapter<Page, RecyclerView> viewPagerAdapter;
    private Pages pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        pages = new Pages(
                Page.newInstance(0, 20, Color.RED),
                Page.newInstance(1, 8, Color.GREEN),
                Page.newInstance(2, 12, Color.BLUE),
                Page.newInstance(3, 25, Color.MAGENTA),
                Page.newInstance(4, 17, Color.YELLOW),
                Page.newInstance(5, 23, Color.CYAN)
        );

        PagesAdapterFactory pagerAdapterFactory = new PagesAdapterFactory(this, pages);
        viewPagerAdapter = pagerAdapterFactory.build();
        ((ViewPager) findViewById(R.id.pager)).setAdapter(viewPagerAdapter);
    }

    @Override
    public void onClick(Item item) {
        this.pages = pages.copyButToggleFavoriteFor(item);

        viewPagerAdapter.setItems(pages);
        viewPagerAdapter.notifyDataSetChanged();
    }
}
