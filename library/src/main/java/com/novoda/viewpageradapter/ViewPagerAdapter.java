package com.novoda.viewpageradapter;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class ViewPagerAdapter<T, V extends View> extends PagerAdapter {

    private static final String ILLEGAL_STATE_ID_ON_VIEW = "The view created for position %d has an ID. Page view IDs must be set by the adapter to deal with state saving and restoring. Make sure your inflated views have an ID of View.NO_ID";

    private final Map<V, Integer> instantiatedViews = new HashMap<>();
    private final ViewIdGenerator viewIdGenerator = new ViewIdGenerator();

    private final ViewCreator<T, V> viewCreator;
    private final ViewBinder<T, V> viewBinder;

    private ViewPagerAdapterState viewPagerAdapterState = ViewPagerAdapterState.newInstance();
    private List<T> items = new ArrayList<>();

    public ViewPagerAdapter(List<T> items, ViewCreator<T, V> viewCreator, ViewBinder<T, V> viewBinder) {
        this.viewCreator = viewCreator;
        this.viewBinder = viewBinder;
        this.items = items;
    }

    @Override
    public final V instantiateItem(ViewGroup container, int position) {
        T item = items.get(position);
        V view = viewCreator.createView(item, container);

        assertViewHasNoId(position, view);
        int restoredId = viewPagerAdapterState.getId(position);
        view.setId(restoredId == View.NO_ID ? viewIdGenerator.generateViewId() : restoredId);

        SparseArray<Parcelable> viewState = viewPagerAdapterState.getViewState(position);
        bindView(item, view, viewState);
        instantiatedViews.put(view, position);
        container.addView(view);

        // key with which to associate this view
        return view;
    }

    private void assertViewHasNoId(int position, V view) {
        if (view.getId() != View.NO_ID) {
            String errorMessage = String.format(Locale.US, ILLEGAL_STATE_ID_ON_VIEW, position);
            throw new IllegalStateException(errorMessage);
        }
    }

    /**
     * Bind the view to the given item and restore view state.
     *
     * @param item      the item to bind
     * @param view      the page view to bind
     * @param viewState the state of the view
     */
    private void bindView(T item, V view, @Nullable SparseArray<Parcelable> viewState) {
        viewBinder.bindView(item, view);
        restoreHierarchyState(view, viewState);
    }

    /**
     * Restore state on the given page view.
     *
     * @param view      the page view to restore state on
     * @param viewState the state of the view
     */
    private void restoreHierarchyState(V view, @Nullable SparseArray<Parcelable> viewState) {
        if (viewState != null) {
            view.restoreHierarchyState(viewState);
        }
    }

    @Override
    public final void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        for (Map.Entry<V, Integer> entry : instantiatedViews.entrySet()) {
            int position = entry.getValue();
            SparseArray<Parcelable> viewState = viewPagerAdapterState.getViewState(position);
            bindView(items.get(position), entry.getKey(), viewState);
        }
    }

    @SuppressWarnings("unchecked")
    // `key` is the object we return in `instantiateItem(ViewGroup container, int position)`
    @Override
    public final void destroyItem(ViewGroup container, int position, Object key) {
        V view = (V) key;
        saveViewState(position, view);
        container.removeView(view);
        instantiatedViews.remove(view);
    }

    private void saveViewState(int position, V view) {
        SparseArray<Parcelable> viewState = new SparseArray<>();
        saveHierarchyState(view, viewState);
        viewPagerAdapterState.put(view.getId(), position, viewState);
    }

    /**
     * Save state on the given page view.
     *
     * @param view      the page view to restore state on
     * @param viewState the state of the view
     */
    private void saveHierarchyState(V view, SparseArray<Parcelable> viewState) {
        view.saveHierarchyState(viewState);
    }

    @Override
    public final Parcelable saveState() {
        for (Map.Entry<V, Integer> entry : instantiatedViews.entrySet()) {
            int position = entry.getValue();
            V view = entry.getKey();
            saveViewState(position, view);
        }
        return viewPagerAdapterState;
    }

    @Override
    public final void restoreState(Parcelable state, ClassLoader loader) {
        if (state instanceof ViewPagerAdapterState) {
            this.viewPagerAdapterState = ((ViewPagerAdapterState) state);
        } else {
            super.restoreState(state, loader);
        }
    }

    @Override
    public final boolean isViewFromObject(View view, Object key) {
        return view.equals(key);
    }

    @Override
    public final int getCount() {
        return items.size();
    }

    public final void setItems(List<T> items) {
        this.items = items;
    }

}
