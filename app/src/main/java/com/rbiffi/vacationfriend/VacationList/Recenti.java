package com.rbiffi.vacationfriend.VacationList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.rbiffi.vacationfriend.R;
import java.util.ArrayList;

public class Recenti extends Fragment{

    private ListView vacationList;
    private ArrayList dataSource;
    private ArrayAdapter dataAdapter;

    private ImageButton vacationMenu;

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
        dataSource = new ArrayList();
        dataSource.add("Oggetto 1");
        dataSource.add("Oggetto 2");
        dataSource.add("Oggetto 3");
        dataSource.add("Oggetto 1");
        dataSource.add("Oggetto 2");
        dataSource.add("Oggetto 3");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vacationList = getView().findViewById(R.id.vacationElList);
        vacationList.setDivider(null); // rimuovo il divisore dalle liste (perché noi l'abbiamo fatto interno all'elemento)

        // gli dico all'adapter dove sono i dati (source) e dove metterli (layout, elemento)
        dataAdapter = new VacationListAdapter(getContext(), R.layout.vacation_list_row, R.id.rowText, dataSource);
        vacationList.setAdapter(dataAdapter); // connetto la lista e l'adapter
        vacationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                //todo switch per capire cosa è stato cliccato. 2 soli comportamenti.

                PopupMenu popup = new PopupMenu(getActivity(),view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.actionModifica:
                                //todo apri modifica vacanza
                                Toast.makeText(getActivity(), "Modifica"+i, Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.actionArchivia:
                                // todo archivia vacanza
                                Toast.makeText(getActivity(), "Archivia"+i, Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.actionElimina:
                                // todo elimina vacanza
                                Toast.makeText(getActivity(), "Elimina"+i, Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                Menu menu = popup.getMenu();
                popup.getMenuInflater().inflate(R.menu.vacation_menu, menu);

                // per rendere visibile l'icona anche nell'overflow menù
                MenuPopupHelper menuHelper = new MenuPopupHelper(getActivity(), (MenuBuilder) menu, view);
                menuHelper.setForceShowIcon(true);
                menuHelper.show();

            }
        });
/*
        vacationMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(),view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.actionModifica:
                                //todo apri modifica vacanza
                                Toast.makeText(getActivity(), "Modifica", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.actionArchivia:
                                // todo archivia vacanza
                                Toast.makeText(getActivity(), "Archivia", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.actionElimina:
                                // todo elimina vacanza
                                Toast.makeText(getActivity(), "Elimina", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.vacation_menu, popup.getMenu());
                popup.show();
            }
        });
*/
    }

    //todo devo fare un custom adapter per aggiungere al pulsante l'onclick listener
    class VacationListAdapter extends ArrayAdapter{

        public VacationListAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        public VacationListAdapter(Context context, int vacation_list_row, int rowText, ArrayList dataSource) {
            super(context, vacation_list_row, rowText, dataSource);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
            if( convertView == null ){
                //We must create a View:
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.vacation_list_row, parent, false);
            }
            //Operazioni da fare alla view
            ImageButton ib = convertView.findViewById(R.id.vacationMenu);
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // rimando l'evento del click al frammento.onItemCLick(). Lo gestirà lui a seconda dell'elemento cliccato
                    ((ListView) parent).performItemClick(view, position, 0);
                }
            });



            return convertView;
        }
    }

    // logica del frammento qui sotto
}
