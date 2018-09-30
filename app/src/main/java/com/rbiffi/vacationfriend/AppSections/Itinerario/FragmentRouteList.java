package com.rbiffi.vacationfriend.AppSections.Itinerario;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.rbiffi.vacationfriend.AppSections.Home.ViewModels.VacationViewModel;
import com.rbiffi.vacationfriend.AppSections.Itinerario.Adapters.RouteAdapter;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Step;
import com.rbiffi.vacationfriend.Utils.NoHeaderDividerItemDecoration;

import java.util.List;

public class FragmentRouteList extends Fragment {

    private VacationViewModel viewModel;

    private RecyclerView routeList;
    private RouteAdapter routeAdapter;
    private RecyclerView.LayoutManager routeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(getActivity()).get(VacationViewModel.class);
        return inflater.inflate(R.layout.fragment_route_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListWithAdapter();
    }

    private void setupListWithAdapter() {
        routeList = getView().findViewById(R.id.routeList);
        routeList.addItemDecoration(new NoHeaderDividerItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.list_divider)));
        routeLayout = new StickyLayoutManager(getContext(), routeAdapter);
        routeList.setLayoutManager(routeLayout);

        viewModel.getRoute().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable List<Step> steps) {
                // todo devo elaborare la lista degli step prima di darla all'adapter
                // in particolare devo aggiungere i giorni e marcarli come stickyheader
                // e creare una riga per l'elemento stop + una per l'elemento vehicle
                routeAdapter = new RouteAdapter(getContext(), steps);
                routeList.setAdapter(routeAdapter);
            }
        });
    }
}
