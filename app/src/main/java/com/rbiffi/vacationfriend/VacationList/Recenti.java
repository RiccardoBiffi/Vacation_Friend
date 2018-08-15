package com.rbiffi.vacationfriend.VacationList;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Vacation;
import com.rbiffi.vacationfriend.Repository.VacationListAdapter;
import com.rbiffi.vacationfriend.Utils.VacationLite;
import com.rbiffi.vacationfriend.Utils.VacationViewModel;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Recenti extends Fragment{

    private static final int NEW_VACATION_ACTIVITY_RCODE = 1;
    private VacationViewModel viewModel;

    private FloatingActionButton floatingButton;

    private RecyclerView vacationList;
    private RecyclerView.Adapter vacationAdapter;
    private RecyclerView.LayoutManager vacationLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // non posso passare il layout con setContentView, devo usare i parametri in input
        // l'inflater trasforma xml in codice java a run time
        //container è il padre del fragment, si arrangia lui a sostituirlo
        return inflater.inflate(R.layout.fragment_recenti, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // uso un adapter per recuperare i dati da una sorgente e posizionarli nelle giuste posizioni dell'interfaccia
        // è l'intermediario che visualizza i dati
        // tipo l'array adapter ha per sorgente un array. (a me serve sia salvarli che leggerli)
        /*
        dataSource = new ArrayList();
        dataSource.add("Oggetto 1");
        dataSource.add("Oggetto 2");
        dataSource.add("Oggetto 3");
        dataSource.add("Oggetto 1");
        dataSource.add("Oggetto 2");
        dataSource.add("Oggetto 3");
        */
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupListWithAdapter();
        setupFloatingButton();
        //setupRowClickListeners();
        //setupListFooter();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_VACATION_ACTIVITY_RCODE && resultCode == RESULT_OK){
            Vacation v = new Vacation();
            v.name = data.getStringExtra(NewVacationActivity.EXTRA_REPLY + NewVacationActivity.TITLE_FIELD);
            v.note = data.getStringExtra(NewVacationActivity.EXTRA_REPLY + NewVacationActivity.NOTES_FIELD);
            viewModel.insert(v);
        } else {
            Toast.makeText(getContext(), "Elemento non salvato perché vuoto", Toast.LENGTH_SHORT).show();
        }

    }

    private void setupFloatingButton() {
        floatingButton = getActivity().findViewById(R.id.floatingActionButton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),NewVacationActivity.class);
                startActivityForResult(intent, NEW_VACATION_ACTIVITY_RCODE);
            }
        });
    }

/*
    private void setupListFooter() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View footer = inflater.inflate(R.layout.listview_footer,vacationList,false);
        vacationList.addFooterView(footer,null,false);
    }

    private void setupRowClickListeners() {
        vacationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                long clickedViewId = view.getId();

                if(clickedViewId == R.id.vacationImage) {
                    //todo start activity della vacanza corrente
                    Toast.makeText(getContext(), "Start activity"+i, Toast.LENGTH_SHORT).show();
                } else if (clickedViewId == R.id.vacationMenu){
                    openPopupMenu(view, i);
                } else {
                    Toast.makeText(getContext(), "Unknown view clicked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
*/

    private void setupListWithAdapter() {
        vacationList = getView().findViewById(R.id.vacationElList);
        vacationList.setHasFixedSize(true); // più performance se il contenuto non modifica le dimensioni

        final VacationListAdapter adapter = new VacationListAdapter(getContext());
        vacationList.setAdapter(adapter);

        vacationLayout = new LinearLayoutManager(getContext());
        vacationList.setLayoutManager(vacationLayout);

        // recupero il viewmodel che preserverà i dati anche a seguito di cambi di configurazione della activity
        viewModel = ViewModelProviders.of(this).get(VacationViewModel.class);

        // osservo il livedata per reagire quando i dati cambiano
        viewModel.getAllVacations().observe(this, new Observer<List<VacationLite>>() {
            @Override
            public void onChanged(@Nullable List<VacationLite> vacationLites) {
                // aggiorno la cache delle vacanze nell'adapter
                adapter.setVacations(vacationLites);
            }
        });

    }

    @SuppressLint("RestrictedApi")
    private void openPopupMenu(View view, final int i) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        setActionsOnOptions(i, popup);

        Menu menu = popup.getMenu();
        popup.getMenuInflater().inflate(R.menu.vacation_menu, menu);
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

    private void setActionsOnOptions(final int i, PopupMenu popup) {
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.actionModifica:
                        //todo apri modifica vacanza
                        Toast.makeText(getContext(), "Modifica" + i, Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.actionArchivia:
                        // todo archivia vacanza
                        Toast.makeText(getContext(), "Archivia" + i, Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.actionElimina:
                        // todo elimina vacanza
                        Toast.makeText(getContext(), "Elimina" + i, Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

}
