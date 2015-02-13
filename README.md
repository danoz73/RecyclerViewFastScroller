RecyclerViewFastScroller
===================================

The RecyclerViewFastScroller is a widget that can be added to a layout and connected to a RecyclerView for fast scrolling. In the interest of time, I am pushing out some usable code, but I plan on updating this to better support easy customization and inclusion in projects.

This project is a demonstration of using the RecyclerViewFastScroller widget in a simple activity that uses the basic workings of com.example.android.recyclerview from the v21 Android samples.

![RecyclerViewFastScroller screenshot](http://i.imgur.com/IozUtucl.png)

### Usage

Below are some simple steps to using a RecyclerViewFastScroller. Currently, there is only a single implementation (`VerticalRecyclerViewFastScroller`), so that will be used here.

The best way to check everything out is to peruse the example code and run the example app. See how `VerticalRecyclerViewFastScroller` is utilized in the `recycler_view_frag.xml`.

##### Example Code

1) In the activity or fragment XML where your `RecyclerView` resides, include a `VerticalRecyclerViewFastScroller` object. The following example would be in a relative layout:

```java
...
  <android.support.v7.widget.RecyclerView
      android:id="@+id/recyclerView"
      android:layout_width="match_parent"
      android:layout_height="match_parent"/>

  <your.package.name.scroller.vertical.VerticalRecyclerViewFastScroller
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
```

###### Optional usage

There are currently a few attributes that can be used to customize the vertical fast scroller:

```java
  <attr name="barColor" format="color|reference" />
  <attr name="barBackground" format="reference" />
  <attr name="handleColor" format="color|reference" />
  <attr name="handleBackground" format="reference" />
```

You can see usage of some of these in the example `recycler_view_frag.xml` which is the layout for the example app's single fragment.

### Setup

Apologies for the current state of affairs:

Copy the `Application/src/main/java/com/example/recyclerviewfastscroller/ui/scroller` package and all its files into your source so you now have `scroller` as one of your packages

Copy the `VerticalRecyclerViewFastScroller` attributes from my `res/values/attrs.xml` file into your own  

Copy the `vertical_recycler_fast_scroller_layout.xml` file from my `res/layout` directory into your own

### Contribution

Feel free to submit pull requests. I haven't necessarily implemented this as well as possible, and am always open to suggestions for improvement.

I just couldn't find a simple example for a simple vertical fast scroller for RecyclerView on the internet.
