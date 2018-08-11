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
import android.widget.ImageView;
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

        setupListWithAdapter();
        setupRowClickListeners();
        setupListFooter();
    }

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

    private void setupListWithAdapter() {
        vacationList = getView().findViewById(R.id.vacationElList);
        vacationList.setDivider(null); // rimuovo il divisore dalle liste, è interno all'elemento
        // gli dico all'adapter dove sono i dati (source) e dove metterli (layout, elemento)
        dataAdapter = new VacationListAdapter(getContext(), R.layout.vacation_list_row, R.id.rowText, dataSource);
        vacationList.setAdapter(dataAdapter); // connetto la lista e l'adapter
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

    //Custom adapter per aggiungere l'onclick listener al pulsante ed al resto
    class VacationListAdapter extends ArrayAdapter{

        public VacationListAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        VacationListAdapter(Context context, int vacation_list_row, int rowText, ArrayList dataSource) {
            super(context, vacation_list_row, rowText, dataSource);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
            convertView = maybeRecycleView(convertView, parent);

            //Operazioni agguntive da fare alla view
            setOverflowCLickListener(position, convertView, (ListView) parent, R.id.vacationMenu);
            setImageClickListener(position, convertView, (ListView) parent);

            return convertView;
        }

        private void setImageClickListener(final int position, @NonNull View convertView, @NonNull final ListView parent) {
            ImageView iv = convertView.findViewById(R.id.vacationImage);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // rimando l'evento del click al frammento.onItemCLick(). Lo gestirà lui a seconda dell'elemento cliccato
                    parent.performItemClick(view, position, 0);
                }
            });
        }

        private void setOverflowCLickListener(final int position, @NonNull View convertView, @NonNull final ListView parent, int vacationMenu) {
            ImageButton ib = convertView.findViewById(vacationMenu);
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // rimando l'evento del click al frammento.onItemCLick(). Lo gestirà lui a seconda dell'elemento cliccato
                    parent.performItemClick(view, position, 0);
                }
            });
        }

        private View maybeRecycleView(@Nullable View convertView, @NonNull ViewGroup parent) {
            if( convertView == null ){
                //We must create a View:
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.vacation_list_row, parent, false);
            }
            return convertView;
        }
    }

}
