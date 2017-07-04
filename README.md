A simple implementation of PagerAdapter that supports Views.

## Description

A ViewPagerAdapter for your ViewPager. This implementation will attempt to rebind to existing views when you call `notifyDataSetChanged()`, rather than recreate all the Views.

## Simple usage

`ViewPagerAdapter` is typed with the data you want to display on each page, and the view on which to display it, so you can specify your requirements quite explicitly. 

It's simple to use - just implement the `ViewCreator` and `ViewBinder` interfaces, and pass them to the `ViewPagerAdapter` constructor along with your data.

If you're making a ViewPager with just TextViews, you can use the following implementation:

```java
    List<String> items = Arrays.asList("First page", "second page", "third page");

    ViewCreator<String, TextView> textViewCreator = new ViewCreator<String, TextView>() {
        @Override
        public TextView createView(String item, ViewGroup container) {
            // inflate the view, do not attach to parent (the `false` param at the end of the `inflate()`)
            TextView view = (TextView) LayoutInflater.from(container.getContext()).inflate(R.layout.view_my_text_view, container, false);
            return view;
        }
    };

    ViewBinder<String, TextView> textViewBinder = new ViewBinder<String, TextView>() {
        @Override
        public void bindView(String item, TextView view) {
            view.setText(item);
        }
    };
    
    ViewPagerAdapter<String, TextView> textViewPagerAdapter = new ViewPagerAdapter<>(items, textViewCreator, textViewBinder);
    return textViewPagerAdapter;
```
