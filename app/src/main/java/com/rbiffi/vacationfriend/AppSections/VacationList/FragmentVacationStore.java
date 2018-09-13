package com.rbiffi.vacationfriend.AppSections.VacationList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rbiffi.vacationfriend.AppSections.VacationList.Adapters.VacationListStoredAdapter;
import com.rbiffi.vacationfriend.AppSections.VacationList.Events.IVacationListClickEvents;
import com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels.VacationListViewModel;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.VacationLite;
import com.rbiffi.vacationfriend.Repository.VacationFriendRepository;

import java.util.List;

public class FragmentVacationStore
        extends Fragment
        implements
        IVacationListClickEvents,
        VacationFriendRepository.IRepositoryListener {

    // todo queste 2 costanti probabilmente non servono
    private static final int NEW_VACATION_ACTIVITY_RCODE = 1;
    private static final int UPDATE_VACATION_ACTIVITY_RCODE = 2;

    private VacationListViewModel viewModel;

    private View emptyListTutorial;
    private ProgressBar progressBar;

    private RecyclerView vacationList;
    private VacationListStoredAdapter vacationAdapter; // todo devo definirne un'altro
    private RecyclerView.LayoutManager vacationLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.vacationlist_fragment_archive, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emptyListTutorial = getActivity().findViewById(R.id.empty_stored_list);
        progressBar = getActivity().findViewById(R.id.fragment_stored_vacationlist_progressbar);

        setupListWithAdapter();
    }


    private void setupListWithAdapter() {
        vacationList = getView().findViewById(R.id.vacationlist_achieved_list);
        vacationList.setHasFixedSize(true); // più performance se il contenuto non modifica le dimensioni

        vacationAdapter = new VacationListStoredAdapter(getContext());
        vacationAdapter.setListener(this);
        vacationList.setAdapter(vacationAdapter);

        vacationLayout = new LinearLayoutManager(getContext());
        vacationList.setLayoutManager(vacationLayout);

        // recupero il viewmodel che preserverà i dati anche a seguito di cambi di configurazione della activity
        //todo devo salvare le vacanze archiviate
        viewModel = ViewModelProviders.of(this).get(VacationListViewModel.class);

        // osservo il livedata per reagire quando i dati cambiano
        //todo devo osservare le vacanze archiviate
        viewModel.getStoredVacation().observe(this, new Observer<List<VacationLite>>() {
            @Override
            public void onChanged(@Nullable List<VacationLite> vacationLites) {
                if (vacationLites != null && !vacationLites.isEmpty()) {
                    //todo loader
                    emptyListTutorial.setVisibility(View.GONE);
                } else {
                    emptyListTutorial.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
                vacationAdapter.setVacations(vacationLites);
            }
        });

        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onVacationClick(VacationLite vacation) {
        Toast.makeText(getContext(), "Apertura vacanza archiviata " + vacation.id, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOverflowClick(View v, VacationLite vacation) {
        openPopupMenu(v, vacation);
        viewModel.updateSelectedVacation(vacation.id, this);
    }

    @SuppressLint("RestrictedApi")
    private void openPopupMenu(View view, VacationLite vacation) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        setActionsOnOptions(vacation, popup);

        Menu menu = popup.getMenu();
        popup.getMenuInflater().inflate(R.menu.vacation_stored_menu, menu);
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

    private void setActionsOnOptions(final VacationLite vacation, PopupMenu popup) {
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_stored_dearchivia:
                        AlertDialog.Builder builderArchivia = new AlertDialog.Builder(getActivity());
                        builderArchivia.setTitle("Vuoi ripristinare " + vacation.title + "?");
                        builderArchivia.setPositiveButton(R.string.button_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //todo unstore vacanza
                                viewModel.unstore(vacation.id);
                                Toast.makeText(getContext(), vacation.title + " ripristinata", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builderArchivia.setNegativeButton(R.string.button_ignore, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        });
                        builderArchivia.create().show();
                        return true;

                    case R.id.action_stored_elimina:
                        AlertDialog.Builder builderElimina = new AlertDialog.Builder(getActivity());
                        builderElimina.setTitle("Vuoi eliminare " + vacation.title + "?");
                        builderElimina.setMessage(R.string.dialog_vacation_delete_message);
                        builderElimina.setPositiveButton(R.string.button_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                viewModel.delete(vacation.id);
                                Toast.makeText(getContext(), vacation.title + " eliminata", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builderElimina.setNegativeButton(R.string.button_ignore, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        });
                        builderElimina.create().show();
                        return true;

                    default:
                        return false;
                }
            }
        });
    }


    @Override
    public void onVacationOperationComplete(long rowId) {

    }

    @Override
    public void onGetVacationDetailsComplete(Vacation v) {
        viewModel.setSelectedVacation(v);
    }

    @Override
    public void onGetVacationParticipants(List<Participant> participants) {
        //todo non serve recuperare la lista dei partecipanti
    }
}
