package com.rbiffi.vacationfriend.AppSections.Home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rbiffi.vacationfriend.AppSections.Home.Adapters.LogListAdapter;
import com.rbiffi.vacationfriend.AppSections.Home.ViewModels.VacationViewModel;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.ActivityLog;
import com.rbiffi.vacationfriend.Utils.NoHeaderDividerItemDecoration;

import java.util.List;

public class FragmentHomeActivityLog extends Fragment {

    private VacationViewModel viewModel;

    private RecyclerView logList;
    private LogListAdapter logAdapter;
    private RecyclerView.LayoutManager logLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(getActivity()).get(VacationViewModel.class);
        return inflater.inflate(R.layout.fragment_home_activitylog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListWithAdapter(view);
    }

    private void setupListWithAdapter(View view) {
        logList = view.findViewById(R.id.logList);
        logList.addItemDecoration(new NoHeaderDividerItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.simple_list_divider)));
        logLayout = new LinearLayoutManager(getContext());
        logList.setLayoutManager(logLayout);

        viewModel.getActivityLog().observe(this, new Observer<List<ActivityLog>>() {
            @Override
            public void onChanged(@Nullable List<ActivityLog> activityLogs) {
                logAdapter = new LogListAdapter(getContext(), activityLogs);
                logList.setAdapter(logAdapter);
            }
        });
    }
}
