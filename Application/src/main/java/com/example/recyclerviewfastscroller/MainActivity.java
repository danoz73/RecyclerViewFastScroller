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

package com.example.recyclerviewfastscroller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.recyclerview.R;
import com.example.recyclerviewfastscroller.fragment.RecyclerViewFragment;
import com.example.recyclerviewfastscroller.fragment.RecyclerViewWithSectionsFragment;

/**
 * Simple activity for displaying the {@link RecyclerViewFragment}
 */
public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            RecyclerViewFragment fragment = new RecyclerViewFragment();
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
            case R.id.menu_item_list_with_sections:
                RecyclerViewWithSectionsFragment sectionsFragment = new RecyclerViewWithSectionsFragment();
                replaceCurrentFragment(sectionsFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }}
