package com.rbiffi.vacationfriend.AppSections.Spese;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rbiffi.vacationfriend.R;

public class FragmentExpensesDebts extends Fragment {

    private ViewGroup debt1;
    private ViewGroup debt2;
    private ViewGroup debt3;
    private ViewGroup debt4;
    private ViewGroup debt5;
    private ViewGroup debt6;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expenses_debts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        debt1 = view.findViewById(R.id.debt1);
        debt2 = view.findViewById(R.id.debt2);
        debt3 = view.findViewById(R.id.debt3);
        debt4 = view.findViewById(R.id.debt4);
        debt5 = view.findViewById(R.id.debt5);
        debt6 = view.findViewById(R.id.debt6);

        View.OnClickListener debtDetails = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Dettaglio debito", Toast.LENGTH_SHORT).show();
            }
        };

        debt1.setOnClickListener(debtDetails);
        debt2.setOnClickListener(debtDetails);
        debt3.setOnClickListener(debtDetails);
        debt4.setOnClickListener(debtDetails);
        debt5.setOnClickListener(debtDetails);
        debt6.setOnClickListener(debtDetails);

    }
}
