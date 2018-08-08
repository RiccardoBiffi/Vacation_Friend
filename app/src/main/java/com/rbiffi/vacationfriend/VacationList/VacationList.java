package com.rbiffi.vacationfriend.VacationList;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.rbiffi.vacationfriend.R;

import java.util.ArrayList;

public class VacationList extends AppCompatActivity {

    private ViewPager viewPager;
    private FragmentAdapter fragmentAdapter;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private FloatingActionButton floatingButton;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    //todo da spostare
    private BottomNavigationView bottomNavigationView;

    private ListView vacationList;
    private ArrayList dataSource;
    private ArrayAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        setupFragments();
        setupTabsLabel();
        setupActionBar();
        setupFloatingButton();
        setupSideDrawer();

        //todo da spostare nella classe corretta assieme al campo
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.actionHome:
                        //todo apri attività
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

        // uso un adapter per recuperare i dati da una sorgente e posizionarli nelle giuste posizioni dell'interfaccia
        // è l'intermediario che visualizza i dati
        // tipo l'array adapter ha per sorgente un array. (a me serve sia salvarli che leggerli)
        dataSource = new ArrayList();
        dataSource.add("Oggetto 1");
        dataSource.add("Oggetto 2");
        dataSource.add("Oggetto 3");

        vacationList = findViewById(R.id.vacationElList);
        vacationList.setDivider(null); // rimuovo il divisore dalle liste (perché noi l'abbiamo fatto interno all'elemento

        // gli dico all'adapter dove sono i dati (source) e dove metterli (layout, elemento)
        dataAdapter = new ArrayAdapter(this, R.layout.vacation_list_row, R.id.rowText, dataSource);
        vacationList.setAdapter(dataAdapter); // connetto la lista e l'adapter
    }

    private void setupSideDrawer() {
        setupDrawerLayoutToggle();
        setupDrawerMenuActions();
    }

    private void setupDrawerLayoutToggle() {
        drawerLayout = findViewById(R.id.drawer_layout);
        // elemento interattivo per aprire sidedrawer e visualizzarlo in action bar
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.sdOpenMenu, R.string.sdCloseMenu);
        drawerLayout.addDrawerListener(drawerToggle); // connetto il layout con il pulsante
        drawerToggle.syncState(); // "operazione di pulizia"
    }

    private void setupDrawerMenuActions() {
        navigationView = findViewById(R.id.nav_view);
        // definisco cosa succede quando premo sugli elementi del side drawer
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.actionImpostazioni:
                        Toast.makeText(getApplicationContext(), "Impostazioni", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.actionNovita:
                        Toast.makeText(getApplicationContext(), "Novità", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.actionFeedback:
                        //todo apri attività
                        Toast.makeText(getApplicationContext(), "Feedback", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.actionAbout:
                        //todo apri attività
                        Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }

    private void setupFloatingButton() {
        floatingButton = findViewById(R.id.floatingActionButton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo chiama activity per aggiungere nuova vacanza
                Toast.makeText(getApplicationContext(), "Add", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupActionBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // trasforma la toolbar in una action bar
    }

    private void setupTabsLabel() {
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager); // farà comparire le linguette all'interno dei tabs come definito nell'adapter
    }

    private void setupFragments() {
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(fragmentAdapter); // attacca al viewpager il gestore dei frammenti
    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // mostra il menù sulla "action" bar
        // dobbiamo creare il menu partendo dal xml -> inflater
        MenuInflater menuInf = getMenuInflater();
        menuInf.inflate(R.menu.test_menu, menu); // mette il menu gonfiato nel parametro in input

        // per rendere visibile l'icona anche nell'overflow menù
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // decide cosa fare quando una voce è selezionata
        // l'item è la voce selezionata
        switch (item.getItemId()){
            case R.id.actionTest1:
                Toast.makeText(getApplicationContext(), "Test1", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.actionTest2:
                Toast.makeText(getApplicationContext(), "Test2", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item); // ogni tanto android aggiunge voci di menù. Così le gestisce
        }
    }

    //classe interna per gestire i frammenti
    public class FragmentAdapter extends FragmentPagerAdapter{

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // riempie l'interfaccia con gli elementi che voglio, passandogli il fragment giusto
            switch(position){
                case 0:
                    return new Recenti();
                case 1:
                    return new Archivio();
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
            switch(position){
                case 0:
                    return getString(R.string.vlTab1);
                case 1:
                    return getString(R.string.vlTab2);
                default:
                    return "";
            }
        }
    }
}
