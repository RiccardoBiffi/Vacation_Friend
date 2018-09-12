package com.rbiffi.vacationfriend.Utils;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rbiffi.vacationfriend.R;

public abstract class ActivityNavigateAppObj extends AppCompatActivity {

    private ViewPager viewPager;
    private FragmentAdapter fragmentAdapter;

    protected Toolbar toolbar;
    private TabLayout tabLayout;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContentView();

        setupActionBar();
        setupFragments();
        setupTabsLabel();
        setupBottomNavigation();
    }

    protected abstract void setActivityContentView();


    protected void setupActionBar() {
        toolbar = findViewById(R.id.vacationlist_toolbar);
        setSupportActionBar(toolbar); // trasforma la toolbar in una action bar
    }

    protected void setupFragments() {
        fragmentAdapter = getFragmentAdapter();
        viewPager = findViewById(R.id.vacationlist_viewpager);
        viewPager.setAdapter(fragmentAdapter); // attacca al viewpager il gestore dei frammenti

    }

    @NonNull
    protected abstract FragmentAdapter getFragmentAdapter();

    private void setupTabsLabel() {
        tabLayout = findViewById(R.id.vacationlist_tabs);
        tabLayout.setupWithViewPager(viewPager); // farà comparire le linguette all'interno dei tabs come definito nell'adapter
    }

    protected void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.navigation_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.actionHome:
                        //todo apri attività se non già aperta
                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.actionItinerario:
                        //todo apri attività
                        Toast.makeText(getApplicationContext(), "Itinerario", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.actionSpese:
                        //todo apri attività
                        Toast.makeText(getApplicationContext(), "Spese", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.actionListe:
                        //todo apri attività
                        Toast.makeText(getApplicationContext(), "Liste", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    protected class FragmentAdapter extends FragmentPagerAdapter {

        protected FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }
    }

}
