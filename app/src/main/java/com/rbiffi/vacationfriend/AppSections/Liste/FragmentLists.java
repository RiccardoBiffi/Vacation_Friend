package com.rbiffi.vacationfriend.AppSections.Liste;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.MenuBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rbiffi.vacationfriend.R;

public class FragmentLists extends Fragment {

    private FragmentAdapter fragmentAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public FragmentLists() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // you should initialize essential components of the fragment that you want to retain when
        // the fragment is paused or stopped, then resumed.
        setHasOptionsMenu(true); // il frammento può aggiungere voci al menù chiamando onCreateOptionsMenu
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // The system calls this when it's time for the fragment to draw its user interface for the
        // first time. To draw a UI for your fragment, you must return a View from this method that
        // is the root of your fragment's layout. You can return null if the fragment does not provide a UI.
        return inflater.inflate(R.layout.navigation_fragment_lists, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentAdapter = new FragmentAdapter(getChildFragmentManager());
        viewPager = view.findViewById(R.id.tabs_lists_viewpager);
        tabLayout = view.findViewById(R.id.tabs_lists);

        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onPause() {
        super.onPause();
        // This is usually where you should commit any changes that should be persisted beyond the
        // current user session (because the user might not come back).
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // todo salvare lo stato dal kill del processo
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // todo modifica il menù della action bar
        inflater.inflate(R.menu.appbar_lists_menu, menu);

        // per rendere visibile l'icona anche nell'overflow menù
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //nb l'evento passa prima all'activity e, se non gestito, al fragment

        switch (item.getItemId()) {
            case R.id.action_lists_sort:
                Toast.makeText(getContext(), getString(R.string.op_sort) + " list", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_lists_settings:
                Toast.makeText(getContext(), getString(R.string.op_settings) + " list", Toast.LENGTH_SHORT).show();
                return true;
            default:
                // per gestire eventuali voci di menù extra
                return super.onOptionsItemSelected(item);
        }
    }

    //classe interna per gestire i frammenti
    public class FragmentAdapter extends FragmentPagerAdapter {

        FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentListPersonal();
                case 1:
                    return new FragmentListForEveryone();
                case 2:
                    return new FragmentListCollaborative();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.lists_tag_personal);
                case 1:
                    return getString(R.string.lists_tag_foreveryone);
                case 2:
                    return getString(R.string.lists_tag_collaborative);
                default:
                    return "";
            }
        }

    }
}
