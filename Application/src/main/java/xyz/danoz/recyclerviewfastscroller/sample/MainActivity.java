/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package xyz.danoz.recyclerviewfastscroller.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import xyz.danoz.recyclerview.sample.R;
import xyz.danoz.recyclerviewfastscroller.sample.fragment.RecyclerViewWithFastScrollerFragment;
import xyz.danoz.recyclerviewfastscroller.sample.fragment.RecyclerViewWithSectionIndicatorFragment;
import xyz.danoz.recyclerviewfastscroller.sample.ui.scroller.sectionindicator.title.SectionTitleIndicator;
import xyz.danoz.recyclerviewfastscroller.sample.ui.scroller.vertical.VerticalRecyclerViewFastScroller;

/**
 * Simple activity for displaying an example of the {@link VerticalRecyclerViewFastScroller} as well as
 * {@link SectionTitleIndicator} usage paired with the fast scroller
 */
public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            RecyclerViewWithFastScrollerFragment fragment = new RecyclerViewWithFastScrollerFragment();
            replaceCurrentFragment(fragment);
        }
    }

    private void replaceCurrentFragment(Fragment newFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.sample_content_fragment, newFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_list_no_section_indicator:
                RecyclerViewWithFastScrollerFragment simpleFastScrollerFragment = new RecyclerViewWithFastScrollerFragment();
                replaceCurrentFragment(simpleFastScrollerFragment);
                return true;
            case R.id.menu_item_list_with_sections:
                RecyclerViewWithSectionIndicatorFragment sectionsFragment = new RecyclerViewWithSectionIndicatorFragment();
                replaceCurrentFragment(sectionsFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
