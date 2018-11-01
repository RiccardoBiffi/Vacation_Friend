package com.rbiffi.vacationfriend.AppSections.Spese;

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

public class FragmentExpensesList extends Fragment {

    private FloatingActionButton floatingButton;

    private ImageView moreActions1;
    private ImageView moreActions2;
    private ImageView moreActions3;
    private ImageView moreActions4;
    private ImageView moreActions5;
    private ImageView moreActions6;

    private ViewGroup expense1;
    private ViewGroup expense2;
    private ViewGroup expense3;
    private ViewGroup expense4;
    private ViewGroup expense5;
    private ViewGroup expense6;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expenses_expenses, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        floatingButton = view.findViewById(R.id.floatingActionButton);
        setupFloatingButton();

        moreActions1 = view.findViewById(R.id.actions1);
        moreActions2 = view.findViewById(R.id.actions2);
        moreActions3 = view.findViewById(R.id.actions3);
        moreActions4 = view.findViewById(R.id.actions4);
        moreActions5 = view.findViewById(R.id.actions5);
        moreActions6 = view.findViewById(R.id.actions6);

        View.OnClickListener actionsPopup = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupMenu(v);
            }
        };

        View.OnClickListener actionsSharedPopup = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSharedPopupMenu(v);
            }
        };

        moreActions1.setOnClickListener(actionsPopup);
        moreActions2.setOnClickListener(actionsPopup);
        moreActions3.setOnClickListener(actionsSharedPopup);
        moreActions4.setOnClickListener(actionsPopup);
        moreActions5.setOnClickListener(actionsPopup);
        moreActions6.setOnClickListener(actionsSharedPopup);

        expense1 = view.findViewById(R.id.expense1);
        expense2 = view.findViewById(R.id.expense2);
        expense3 = view.findViewById(R.id.expense3);
        expense4 = view.findViewById(R.id.expense4);
        expense5 = view.findViewById(R.id.expense5);
        expense6 = view.findViewById(R.id.expense6);

        View.OnClickListener expenseDetails = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Dettaglio spesa", Toast.LENGTH_SHORT).show();
            }
        };

        expense1.setOnClickListener(expenseDetails);
        expense2.setOnClickListener(expenseDetails);
        expense3.setOnClickListener(expenseDetails);
        expense4.setOnClickListener(expenseDetails);
        expense5.setOnClickListener(expenseDetails);
        expense6.setOnClickListener(expenseDetails);
    }

    private void setupFloatingButton() {
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Nuova spesa", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void openPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        setActionsOnOptions(popup);

        Menu menu = popup.getMenu();
        popup.getMenuInflater().inflate(R.menu.expense_menu, menu);
        MenuPopupHelper menuHelper = showIconsOnMenu(view, (MenuBuilder) menu);

        menuHelper.show();
    }

    @SuppressLint("RestrictedApi")
    private void openSharedPopupMenu(View view) {
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
