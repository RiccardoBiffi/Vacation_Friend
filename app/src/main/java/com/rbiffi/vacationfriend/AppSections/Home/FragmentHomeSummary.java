package com.rbiffi.vacationfriend.AppSections.Home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.rbiffi.vacationfriend.Utils.NoFooterDividerItemDecoration;

public class FragmentHomeSummary extends Fragment {

    private VacationViewModel viewModel;

    private RecyclerView summaryList;
    private ReadFieldListAdapter vacationAdapter;
    private RecyclerView.LayoutManager vacationLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(getActivity()).get(VacationViewModel.class);
        return inflater.inflate(R.layout.fragment_home_summary, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupListWithAdapter();
    }

    public void setupListWithAdapter() {
        summaryList = getView().findViewById(R.id.summaryList);
        summaryList.addItemDecoration(new NoFooterDividerItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.simple_list_divider)));


        vacationAdapter = new ReadFieldListAdapter(getContext(), FieldLists.getReadFieldList(Vacation.class), viewModel);
        summaryList.setAdapter(vacationAdapter);

        vacationLayout = new LinearLayoutManager(getContext());
        summaryList.setLayoutManager(vacationLayout);
    }

}
