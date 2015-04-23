RecyclerViewFastScroller
===================================

The RecyclerViewFastScroller is a widget that can be added to a layout and connected to a RecyclerView for fast scrolling.

This project is a demonstration of using the RecyclerViewFastScroller widget in a simple activity that uses the basic workings of com.example.android.recyclerview from the v21 Android samples.

![RecyclerViewFastScroller screenshot](http://i.imgur.com/IozUtucl.png)
![RecyclerViewFastScroller with section indicator screenshot](http://i.imgur.com/2zBwIlwl.png)

As of [`b3e2d2f`](https://github.com/danoz73/RecyclerViewFastScroller/commit/b3e2d2fa8284dea31fbc5f9f218199f2a187a657), there is now support for adding a `SectionIndicator` widget, which connects to the scroller. This adds functionality similar to Google's Lollipop Contacts application.

### Download

You can grab the current version of the library from maven central
```java
compile 'xyz.danoz:recyclerviewfastscroller:0.1.3'
```

### Usage

Below are some simple steps to using a RecyclerViewFastScroller. Currently, there is only a single implementation (`VerticalRecyclerViewFastScroller`), so that will be used here.

The best way to check everything out is to peruse the example code and run the sample Application. See how `VerticalRecyclerViewFastScroller` is utilized in the `recycler_view_frag.xml`.

##### Example Code

1) In the activity or fragment XML where your `RecyclerView` resides, include a `VerticalRecyclerViewFastScroller` object. The following example would be in a relative layout:

```java
...
  <android.support.v7.widget.RecyclerView
      android:id="@+id/recyclerView"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      />

  <xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller
      android:id="@+id/fast_scroller"
      android:layout_width="@dimen/however_wide_you_want_this"
      android:layout_height="match_parent"
      android:layout_alignParentRight="true"
      />
...
```

2) In your fragment or activity where you setup layout programmatically, simply hook up the fast scroller to the recycler as follows:

```java
...
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);
      ...
      
      // Grab your RecyclerView and the RecyclerViewFastScroller from the layout
      RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
      VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller) rootView.findViewById(R.id.fast_scroller);
      
      // Connect the recycler to the scroller (to let the scroller scroll the list)
      fastScroller.setRecyclerView(recyclerView);
      
      // Connect the scroller to the recycler (to let the recycler scroll the scroller's handle)
      recyclerView.setOnScrollListener(fastScroller.getOnScrollListener());
      
      ...
      return rootView;
  }
...
```

###### Optional usage

There are currently a few attributes that can be used to customize the vertical fast scroller:

```java
  <attr name="rfs_barColor" format="color|reference" />
  <attr name="rfs_barBackground" format="reference" />
  <attr name="rfs_handleColor" format="color|reference" />
  <attr name="rfs_handleBackground" format="reference" />
```

You can see usage of some of these in the example `recycler_view_with_fast_scroller_fragment.xml` which is the layout for the example app's single fragment.

##### SectionIndicators

Refer to `RecyclerViewWithSectionIndicatorFragment` and the corresponding `recycler_view_with_fast_scroller_section_title_indicator_fragment.xml` in order to find an implementation that adds the Lollipop-Contacts-like section indicator. In addition to the above, you will need to include the indicator in your layout (example here from `recycler_view_with_fast_scroller_section_title_indicator_fragment.xml`):

```java
...
    <xyz.danoz.recyclerviewfastscroller.sample.ui.example.ColorGroupSectionTitleIndicator
      android:id="@+id/fast_scroller_section_title_indicator"
      android:layout_width="wrap_content"
      android:layout_height="@dimen/list_item_height"
      android:layout_toLeftOf="@id/fast_scroller"
      android:layout_toStartOf="@id/fast_scroller"

      recyclerviewfastscroller:rfs_backgroundColor="@android:color/white"
      recyclerviewfastscroller:rfs_textColor="@android:color/black"
       />
...
```
and then connect it to the scroller in the fragment:
```java
...
    // Connect the section indicator to the scroller
    fastScroller.setSectionIndicator(sectionTitleIndicator);
...
```

### Contribution

Feel free to submit pull requests and create issues! I will try to be vigilant about maintaining this library, but may not always be as fast as you would like ;)
