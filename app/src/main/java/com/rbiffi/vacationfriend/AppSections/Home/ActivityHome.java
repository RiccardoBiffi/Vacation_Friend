package com.rbiffi.vacationfriend.AppSections.Home;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.menu.MenuBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Utils.ActivityNavigateAppObj;

public class ActivityHome extends ActivityNavigateAppObj {

    //todo private viewmodel;

    @Override
    protected void setActivityContentView() {
        setContentView(R.layout.activity_navigate_app);
    }

    @NonNull
    @Override
    protected FragmentAdapter getFragmentAdapter() {
        return new FragmentAdapter(getSupportFragmentManager());
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInf = getMenuInflater();
        menuInf.inflate(R.menu.home_summary_menu, menu);

        // per rendere visibile l'icona anche nell'overflow menù
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_summary_modifica:
                // todo apri l'activity di modifica passando la vacazna corrente
                // todo fai in modo che tutto si aggiorni di conseguenza ad operazione finita (return?)
                Toast.makeText(getApplicationContext(), "Modifica", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_summary_synch:
                Toast.makeText(getApplicationContext(), getString(R.string.op_synch), Toast.LENGTH_SHORT).show();
                return true;
            default:
                // per gestire eventuali voci di menù extra
                return super.onOptionsItemSelected(item);
        }
    }


    //classe interna per gestire i frammenti
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
                    return new FragmentHomeChat();
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
















