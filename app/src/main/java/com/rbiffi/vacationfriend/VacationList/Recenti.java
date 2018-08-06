package com.rbiffi.vacationfriend.VacationList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rbiffi.vacationfriend.R;

public class Recenti extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // non posso passare il layout con setContentView, devo usare i parametri in input
        // l'inflater trasforma xml in codice java a run time
        return inflater.inflate(R.layout.fragment_recenti, container, false);
        //container è il padre del fragment, si arrangia lui a sostituirlo
    }

    // logica del frammento qui sotto
}
