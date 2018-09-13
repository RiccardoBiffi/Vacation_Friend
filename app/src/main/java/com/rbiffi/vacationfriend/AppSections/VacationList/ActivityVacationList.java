package com.rbiffi.vacationfriend.AppSections.VacationList;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Utils.ActivityNavigateAppObj;

public class ActivityVacationList extends ActivityNavigateAppObj {

    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewPager = getViewPager();
        viewPager.setAdapter(fragmentAdapter); // attacca al viewpager il gestore dei frammenti

        setupTabsLabel();
        setupSideDrawer();
    }

    @Override
    protected void setActivityContentView() {
        setContentView(R.layout.sidedrawer_activity);
    }

    @NonNull
    @Override
    protected Toolbar getToolbarView() {
        return findViewById(R.id.vacationlist_toolbar);
    }

    @NonNull
    @Override
    protected FragmentAdapter getFragmentAdapter() {
        return new FragmentAdapter(getSupportFragmentManager());
    }

    @NonNull
    private ViewPager getViewPager() {
        return findViewById(R.id.vacationlist_viewpager);
    }

    private void setupTabsLabel() {
        tabLayout = findViewById(R.id.vacationlist_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupSideDrawer() {
        setupDrawerLayoutToggle();
        setupDrawerMenuActions();
    }

    private void setupDrawerLayoutToggle() {
        drawerLayout = findViewById(R.id.drawer_layout);

        // elemento interattivo per aprire sidedrawer e visualizzarlo in action bar
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.sdOpenMenu, R.string.sdCloseMenu);
        drawerLayout.addDrawerListener(drawerToggle); // connetto il layout con il pulsante
        drawerToggle.syncState(); // "operazione di pulizia"
    }

    private void setupDrawerMenuActions() {
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.actionImpostazioni:
                        Toast.makeText(getApplicationContext(), "Impostazioni", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.actionNovita:
                        Toast.makeText(getApplicationContext(), "Novità", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.actionFeedback:
                        Toast.makeText(getApplicationContext(), "Feedback", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.actionAbout:
                        Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }


    //classe interna per gestire i frammenti
    public class FragmentAdapter extends ActivityNavigateAppObj.FragmentAdapter {

        FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // riempie l'interfaccia con gli elementi che voglio, passandogli il fragment giusto
            switch (position) {
                case 0:
                    return new FragmentVacationRecent();
                case 1:
                    return new FragmentVacationStore();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        // utile per visualizzare i fragment tramite tabs. Visualizza il testo dei tabs.
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.vacationlist_tag_myvacations);
                case 1:
                    return getString(R.string.vacationlist_tag_stored);
                default:
                    return "";
            }
        }
    }
}
