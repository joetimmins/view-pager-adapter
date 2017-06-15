A simple implementation of PagerAdapter that supports Views.

## Description

A ViewPagerAdapter for your ViewPager, this implementation will attempt to rebind to existing views when you call `notifyDataSetChanged()`, rather than recreate all the Views.

## Simple usage

`ViewPagerAdapter` is typed so you can specify the type of `View` explicitly. If you're making a ViewPager with just TextViews, you can use the following implementation:

```java
class TextViewPagerAdapter extends ViewPagerAdapter<TextView> {
    
    private List<String> text;

    TextViewPagerAdapter(List<String> text) {
        this.text = text;
    }

    public void update(List<String> text) {
        this.text = text;
        notifyDataSetChanged();
    }

    @Override
    protected TextView createView(ViewGroup container, int position) {
        // inflate the view, do not attach to parent (the `false` param at the end of the `inflate()`)
        TextView view = (TextView) LayoutInflater.from(container.getContext()).inflate(R.layout.view_my_text_view, container, false);
        return view;
    }

    @Override
    protected void bindView(TextView view, int position) {
        String textForView = text.get(position);
        view.setText(textForView);
    }

    @Override
    public int getCount() {
        return text.size();
    }

}
```
