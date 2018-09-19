package com.rbiffi.vacationfriend.AppSections.Home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rbiffi.vacationfriend.AppSections.Home.Adapters.ReadFieldListAdapter;
import com.rbiffi.vacationfriend.AppSections.Home.ViewModels.VacationViewModel;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;
import com.rbiffi.vacationfriend.Utils.FieldLists;

public class FragmentHomeSummary extends Fragment {

    private VacationViewModel viewModel;

    private RecyclerView summaryList;
    private ReadFieldListAdapter vacationAdapter;
    private RecyclerView.LayoutManager vacationLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(VacationViewModel.class);
        return inflater.inflate(R.layout.fragment_home_summary, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupListWithAdapter();
    }

    private void setupListWithAdapter() {
        summaryList = getView().findViewById(R.id.summaryList);

        vacationAdapter = new ReadFieldListAdapter(getContext(), FieldLists.getReadFieldList(Vacation.class), viewModel);
        //vacationAdapter.setListener(this);
        summaryList.setAdapter(vacationAdapter);

        vacationLayout = new LinearLayoutManager(getContext());
        summaryList.setLayoutManager(vacationLayout);

        // recupero il viewmodel che preserverà i dati anche a seguito di cambi di configurazione della activity
        //viewModel = ViewModelProviders.of(this).get(summaryListViewModel.class);

        // osservo il livedata per reagire quando i dati cambiano
        /*
        viewModel.getVacationsNow().observe(this, new Observer<List<VacationLite>>() {
            @Override
            public void onChanged(@Nullable final List<VacationLite> vacationsNow) {
                viewModel.getVacationsNext().observe(FragmentVacationRecent.this, new Observer<List<VacationLite>>() {
                    @Override
                    public void onChanged(@Nullable final List<VacationLite> vacationsNext) {
                        viewModel.getVacationsPrevious().observe(FragmentVacationRecent.this, new Observer<List<VacationLite>>() {
                            @Override
                            public void onChanged(@Nullable List<VacationLite> vacationsPrevious) {
                                if (vacationsNow != null && !vacationsNow.isEmpty() ||
                                        vacationsNext != null && !vacationsNext.isEmpty() ||
                                        vacationsPrevious != null && !vacationsPrevious.isEmpty()) {
                                    emptyListTutorial.setVisibility(View.GONE);
                                } else {
                                    emptyListTutorial.setVisibility(View.VISIBLE);
                                    Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_arrow);
                                    rotation.setRepeatCount(Animation.START_ON_FIRST_FRAME);
                                    View arrow = emptyListTutorial.findViewById(R.id.ptutorial_arrow);
                                    arrow.startAnimation(rotation);
                                }
                                progressBar.setVisibility(View.GONE);
                                vacationAdapter.setAllVacations(vacationsNow, vacationsNext, vacationsPrevious);
                            }
                        });
                    }
                });

            }
        });
        */

        //progressBar.setVisibility(View.VISIBLE);
    }


}
