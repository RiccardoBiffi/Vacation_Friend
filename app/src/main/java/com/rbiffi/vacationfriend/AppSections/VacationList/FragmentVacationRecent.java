package com.rbiffi.vacationfriend.AppSections.VacationList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rbiffi.vacationfriend.AppSections.VacationList.Adapters.VacationListAdapter;
import com.rbiffi.vacationfriend.AppSections.VacationList.Events.IVacationListClickEvents;
import com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels.VacationListViewModel;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.VacationLite;
import com.rbiffi.vacationfriend.Repository.VacationFriendRepository;

import java.util.List;

public class FragmentVacationRecent
        extends Fragment
        implements
        IVacationListClickEvents,
        VacationFriendRepository.IRepositoryListener {

    private static final int NEW_VACATION_ACTIVITY_RCODE = 1;
    private static final int UPDATE_VACATION_ACTIVITY_RCODE = 2;

    private VacationListViewModel viewModel;

    private View emptyListTutorial;
    private ProgressBar progressBar;
    private FloatingActionButton floatingButton;

    private RecyclerView vacationList;
    private VacationListAdapter vacationAdapter;
    private RecyclerView.LayoutManager vacationLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // non posso passare il layout con setContentView, devo usare i parametri in input
        // l'inflater trasforma xml in codice java a run time
        //container è il padre del fragment, si arrangia lui a sostituirlo
        return inflater.inflate(R.layout.vacationlist_fragment_recent, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emptyListTutorial = getActivity().findViewById(R.id.empty_vacation_list);
        progressBar = getActivity().findViewById(R.id.fragment_recent_vacationlist_progressbar);
        floatingButton = getActivity().findViewById(R.id.floatingActionButton);

        setupListWithAdapter();
        setupFloatingButton();
    }

    private void setupFloatingButton() {
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityNewVacation.class);
                startActivityForResult(intent, NEW_VACATION_ACTIVITY_RCODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_VACATION_ACTIVITY_RCODE && resultCode == Activity.RESULT_OK) {
            // todo accedi direttamente alla vacanza appena creata
        }
        if (requestCode == UPDATE_VACATION_ACTIVITY_RCODE && resultCode == Activity.RESULT_OK) {
            // todo la vacanza è stata modificata, non ci accedo
        }
    }

    private void setupListWithAdapter() {
        vacationList = getView().findViewById(R.id.vacationList);
        vacationList.setHasFixedSize(true); // più performance se il contenuto non modifica le dimensioni

        vacationAdapter = new VacationListAdapter(getContext());
        vacationAdapter.setListener(this);
        vacationList.setAdapter(vacationAdapter);

        vacationLayout = new LinearLayoutManager(getContext());
        vacationList.setLayoutManager(vacationLayout);

        // recupero il viewmodel che preserverà i dati anche a seguito di cambi di configurazione della activity
        viewModel = ViewModelProviders.of(this).get(VacationListViewModel.class);

        // osservo il livedata per reagire quando i dati cambiano
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

        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onVacationClick(VacationLite vacation) {
        Toast.makeText(getContext(), "Vacanza" + vacation.id, Toast.LENGTH_SHORT).show();
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

    private void setActionsOnOptions(final VacationLite vacation, PopupMenu popup) {
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.actionModifica:
                        Intent intent = new Intent(getActivity(), ActivityModifyVacation.class);
                        intent.putExtra("selectedVacation", viewModel.getSelectedVacation());
                        startActivityForResult(intent, UPDATE_VACATION_ACTIVITY_RCODE);
                        return true;

                    case R.id.actionArchivia:
                        AlertDialog.Builder builderArchivia = new AlertDialog.Builder(getActivity());
                        builderArchivia.setTitle("Vuoi archiviare " + vacation.title + "?");
                        builderArchivia.setMessage(R.string.dialog_vacation_store_message);
                        builderArchivia.setPositiveButton(R.string.button_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                viewModel.store(vacation.id);
                                Toast.makeText(getContext(), vacation.title + " archiviata", Toast.LENGTH_SHORT).show();
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

                    case R.id.actionElimina:
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
