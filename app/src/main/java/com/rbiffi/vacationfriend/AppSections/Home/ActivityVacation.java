package com.rbiffi.vacationfriend.AppSections.Home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rbiffi.vacationfriend.AppSections.Itinerario.FragmentRoute;
import com.rbiffi.vacationfriend.AppSections.Liste.FragmentLists;
import com.rbiffi.vacationfriend.AppSections.Spese.FragmentExpenses;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Utils.ActivityNavigateAppObj;
import com.rbiffi.vacationfriend.Utils.Constants;

// classe che contiene e gestisce i frammenti collegati alla navigazione primaria dell'app
public class ActivityVacation extends ActivityNavigateAppObj {

    //todo private viewmodel, con dentro la vacanza corrente
    private LinearLayout navigationViewGroup;
    private BottomNavigationView bottomNavigationView;

    private final FragmentHome fHome = new FragmentHome();
    private final FragmentRoute fRoute = new FragmentRoute();
    private final FragmentExpenses fExpenses = new FragmentExpenses();
    private final FragmentLists fLists = new FragmentLists();
    private final FragmentManager fm = getSupportFragmentManager();
    Fragment activeFragment = fHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupBottomNavigation();
        navigationViewGroup = findViewById(R.id.navigation_fragment_container);

        setupFragmentsAndStartHome();
    }

    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.navigation_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.actionHome:
                        //todo apri attività se non già aperta
                        supportInvalidateOptionsMenu();
                        fm.beginTransaction().hide(activeFragment).show(fHome).commit();
                        // fHome.updateTags();
                        activeFragment = fHome;

                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.actionItinerario:
                        //todo apri attività
                        supportInvalidateOptionsMenu();
                        fm.beginTransaction().hide(activeFragment).show(fRoute).commit();
                        fRoute.updateTags();
                        activeFragment = fRoute;

                        Toast.makeText(getApplicationContext(), "Itinerario", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.actionSpese:
                        //todo apri attività
                        supportInvalidateOptionsMenu();
                        fm.beginTransaction().hide(activeFragment).show(fExpenses).commit();
                        fExpenses.updateTags();
                        activeFragment = fExpenses;

                        Toast.makeText(getApplicationContext(), "Spese", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.actionListe:
                        //todo apri attività
                        supportInvalidateOptionsMenu();
                        fm.beginTransaction().hide(activeFragment).show(fLists).commit();
                        fLists.updateTags();
                        activeFragment = fLists;

                        Toast.makeText(getApplicationContext(), "Liste", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                // todo scroll all'inizio della schermata
                Toast.makeText(getApplicationContext(), item.getTitle() + " reselected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupFragmentsAndStartHome() {
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.navigation_fragment_container, fHome, Constants.FTAG_HOME);
        ft.add(R.id.navigation_fragment_container, fRoute, Constants.FTAG_ROUTE);
        ft.add(R.id.navigation_fragment_container, fExpenses, Constants.FTAG_EXPENSES);
        ft.add(R.id.navigation_fragment_container, fLists, Constants.FTAG_LISTS);

        ft.show(fHome); // fragment mostrato di default
        ft.hide(fRoute);
        ft.hide(fExpenses);
        ft.hide(fLists);

        // salvo il fragment in uno stack gestito dall'activity, posso simulare il
        // back per riaprire il fragment precedente
        ft.addToBackStack(null);
        // anima la transizione di fragment
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    @Override
    protected void setActivityContentView() {
        setContentView(R.layout.activity_navigate_app);
    }

    @NonNull
    @Override
    protected Toolbar getToolbarView() {
        return findViewById(R.id.toolbar);
    }

    @NonNull
    @Override
    protected FragmentAdapter getFragmentAdapter() {
        return new FragmentAdapter(getSupportFragmentManager());
    }

    //classe interna per gestire i frammenti
    //todo valuta se rimuovere, dovrebbe servire solo per il viewpager
    public class FragmentAdapter extends ActivityNavigateAppObj.FragmentAdapter {

        FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentHomeSummary();
                case 1:
                    return new FragmentHomeChatList();
                case 2:
                    return new FragmentHomeActivityLog();
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
                    return getString(R.string.home_tag_summary);
                case 1:
                    return getString(R.string.home_tag_chat);
                case 2:
                    return getString(R.string.home_tag_activitylog);
                default:
                    return "";
            }
        }

    }

}
















