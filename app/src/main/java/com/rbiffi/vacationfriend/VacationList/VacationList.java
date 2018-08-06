package com.rbiffi.vacationfriend.VacationList;

import android.annotation.SuppressLint;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.rbiffi.vacationfriend.R;

public class VacationList extends AppCompatActivity {

    private ViewPager viewPager;
    private FragmentAdapter fragmentAdapter;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(fragmentAdapter); // attacca al viewpager il gestore dei frammenti

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager); // farà comparire le linguette all'interno dei tabs come definito nell'adapter

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // trasforma la toolbar in una action bar
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
