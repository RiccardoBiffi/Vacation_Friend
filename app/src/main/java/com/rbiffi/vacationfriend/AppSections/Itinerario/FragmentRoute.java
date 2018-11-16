package com.rbiffi.vacationfriend.AppSections.Itinerario;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rbiffi.vacationfriend.R;

public class FragmentRoute extends Fragment {

    private static final int NEW_VACATION_ACTIVITY_RCODE = 1;

    private FloatingActionButton floatingButton;

    public FragmentRoute() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // you should initialize essential components of the fragment that you want to retain when
        // the fragment is paused or stopped, then resumed.
        setHasOptionsMenu(true); // il frammento può aggiungere voci al menù chiamando onCreateOptionsMenu
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // The system calls this when it's time for the fragment to draw its user interface for the
        // first time. To draw a UI for your fragment, you must return a View from this method that
        // is the root of your fragment's layout. You can return null if the fragment does not provide a UI.
        return inflater.inflate(R.layout.navigation_fragment_route, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floatingButton = getActivity().findViewById(R.id.floatingActionButton);

        setupFloatingButton();
    }

    private void setupFloatingButton() {
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityNewStop.class);
                startActivityForResult(intent, NEW_VACATION_ACTIVITY_RCODE);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // todo salvare lo stato dal kill del processo
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.appbar_route_menu, menu);

        // per rendere visibile l'icona anche nell'overflow menù
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //nb l'evento passa prima all'activity e, se non gestito, al fragment
        switch (item.getItemId()) {
            case R.id.action_route_settings:
                Toast.makeText(getContext(), getString(R.string.op_settings) + " " + getString(R.string.route), Toast.LENGTH_SHORT).show();
                return true;
            default:
                // per gestire eventuali voci di menù extra
                return super.onOptionsItemSelected(item);
        }
    }

}
