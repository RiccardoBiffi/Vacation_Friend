package com.rbiffi.vacationfriend.AppSections.Itinerario;


import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.rbiffi.vacationfriend.AppSections.Home.ViewModels.VacationViewModel;
import com.rbiffi.vacationfriend.AppSections.Itinerario.Adapters.RouteAdapter;
import com.rbiffi.vacationfriend.AppSections.Itinerario.Events.IRouteClickEvents;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Step;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Stop;

import java.util.List;

public class FragmentRouteList extends Fragment implements IRouteClickEvents {

    private VacationViewModel viewModel;

    private RecyclerView routeList;
    private RouteAdapter routeAdapter;
    private RecyclerView.LayoutManager routeLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(getActivity()).get(VacationViewModel.class);
        return inflater.inflate(R.layout.fragment_route_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListWithAdapter(view);
    }

    private void setupListWithAdapter(View view) {
        routeList = view.findViewById(R.id.routeList);
        routeList.addItemDecoration(new DividerItemDecoration(getContext(), 0));

        viewModel.getRoute().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable List<Step> steps) {
                routeAdapter = new RouteAdapter(getContext(), steps);
                routeList.setAdapter(routeAdapter);
                routeAdapter.setListener(FragmentRouteList.this);
                routeLayout = new StickyLayoutManager(getContext(), routeAdapter);
                routeList.setLayoutManager(routeLayout);
                routeAdapter.setListLayout(routeLayout);
            }
        });
    }

    @Override
    public void onOverflowClick(View v, Stop stop) {
        openPopupMenu(v, stop);
    }

    @SuppressLint("RestrictedApi")
    private void openPopupMenu(View view, Stop stop) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        setActionsOnOptions(stop, popup);

        Menu menu = popup.getMenu();
        popup.getMenuInflater().inflate(R.menu.route_stop_menu, menu);
        MenuPopupHelper menuHelper = showIconsOnMenu(view, (MenuBuilder) menu);

        menuHelper.show();
    }

    @SuppressLint("RestrictedApi")
    @NonNull
    private MenuPopupHelper showIconsOnMenu(View view, MenuBuilder menu) {
        // per rendere visibile l'icona anche nell'overflow menù
        MenuPopupHelper menuHelper = new MenuPopupHelper(getActivity(), menu, view);
        menuHelper.setForceShowIcon(true);
        return menuHelper;
    }

    private void setActionsOnOptions(final Stop stop, PopupMenu popup) {
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.actionReturn:
                        Toast.makeText(getActivity(), R.string.op_route_return, Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.actionModify:
                        Toast.makeText(getActivity(), R.string.op_modify, Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.actionElimina:
                        Toast.makeText(getActivity(), R.string.op_delete, Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.actionDiscuss:
                        Toast.makeText(getActivity(), R.string.op_discuss, Toast.LENGTH_SHORT).show();
                        return true;

                    default:
                        return false;
                }
            }
        });
    }
}
