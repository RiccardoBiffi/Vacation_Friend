package com.rbiffi.vacationfriend.AppSections.Home;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.rbiffi.vacationfriend.AppSections.Home.ViewModels.VacationViewModel;
import com.rbiffi.vacationfriend.AppSections.VacationList.ActivityModifyVacation;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;

import java.util.List;

public class FragmentHome extends Fragment {

    private static final int UPDATE_VACATION_ACTIVITY_RCODE = 2;

    private VacationViewModel viewModel;

    private FragmentHomeSummary fhs;

    private FragmentAdapter fragmentAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public FragmentHome() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // you should initialize essential components of the fragment that you want to retain when
        // the fragment is paused or stopped, then resumed.
        viewModel = ViewModelProviders.of(getActivity()).get(VacationViewModel.class);

        setHasOptionsMenu(true); // il frammento può aggiungere voci al menù chiamando onCreateOptionsMenu
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // The system calls this when it's time for the fragment to draw its user interface for the
        // first time. To draw a UI for your fragment, you must return a View from this method that
        // is the root of your fragment's layout. You can return null if the fragment does not provide a UI.
        return inflater.inflate(R.layout.navigation_fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentAdapter = new FragmentAdapter(getChildFragmentManager());
        viewPager = view.findViewById(R.id.tabs_home_viewpager);
        tabLayout = view.findViewById(R.id.tabs_home);

        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        getActivity().findViewById(R.id.fragment_home_vacation_progressbar).setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // todo salvare lo stato dal kill del processo
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        viewModel.getCurrentParticipants().removeObservers(this);
        viewModel.getCurrentParticipants().observe(this, new Observer<List<Participant>>() {
            @Override
            public void onChanged(@Nullable List<Participant> participants) {
                if (fhs.getView() != null)
                    fhs.setupListWithAdapter(fhs.getView());
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.appbar_home_menu, menu);

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
            case R.id.action_home_modifica:
                Intent intent = new Intent(getActivity(), ActivityModifyVacation.class);
                intent.putExtra("selectedVacation", viewModel.getCurrentVacation().getValue());
                startActivityForResult(intent, UPDATE_VACATION_ACTIVITY_RCODE);
                return true;
            case R.id.action_home_synch:
                Toast.makeText(getContext(), getString(R.string.op_synch) + " " + getString(R.string.home), Toast.LENGTH_SHORT).show();
                return true;
            default:
                // per gestire eventuali voci di menù extra
                return super.onOptionsItemSelected(item);
        }
    }

    //classe interna per gestire i frammenti
    class FragmentAdapter extends FragmentPagerAdapter {

        FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    fhs = new FragmentHomeSummary();
                    return fhs;
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
