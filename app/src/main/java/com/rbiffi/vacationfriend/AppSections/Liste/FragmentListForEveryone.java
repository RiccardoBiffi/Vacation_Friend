package com.rbiffi.vacationfriend.AppSections.Liste;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.rbiffi.vacationfriend.R;

public class FragmentListForEveryone extends Fragment {

    private ViewGroup list1;
    private ViewGroup list2;
    private ImageView more1;
    private ImageView more2;
    private FloatingActionButton floatingButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lists_foreveryone, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        floatingButton = view.findViewById(R.id.floatingActionButton);
        setupFloatingButton();

        list1 = view.findViewById(R.id.list_fe1);
        list2 = view.findViewById(R.id.list_fe2);
        more1 = view.findViewById(R.id.actions1);
        more2 = view.findViewById(R.id.actions2);

        View.OnClickListener listDetails = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.list_details, Toast.LENGTH_SHORT).show();
            }
        };

        list1.setOnClickListener(listDetails);
        list2.setOnClickListener(listDetails);

        View.OnClickListener actionsPopup = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupMenu(v);
            }
        };

        more1.setOnClickListener(actionsPopup);
        more2.setOnClickListener(actionsPopup);

    }

    private void setupFloatingButton() {
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), R.string.new_list_fe, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void openPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        setActionsOnOptions(popup);

        Menu menu = popup.getMenu();
        popup.getMenuInflater().inflate(R.menu.expense_shared_menu, menu);
        MenuPopupHelper menuHelper = showIconsOnMenu(view, (MenuBuilder) menu);

        menuHelper.show();
    }

    private void setActionsOnOptions(PopupMenu popup) {
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.actionModifica:
                        Toast.makeText(getContext(), getString(R.string.op_modify), Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.actionElimina:
                        Toast.makeText(getContext(), getString(R.string.op_delete), Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.actionDiscuss:
                        Toast.makeText(getContext(), getString(R.string.op_discuss), Toast.LENGTH_SHORT).show();
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @NonNull
    private MenuPopupHelper showIconsOnMenu(View view, MenuBuilder menu) {
        // per rendere visibile l'icona anche nell'overflow menù
        MenuPopupHelper menuHelper = new MenuPopupHelper(getActivity(), menu, view);
        menuHelper.setForceShowIcon(true);
        return menuHelper;
    }
}
