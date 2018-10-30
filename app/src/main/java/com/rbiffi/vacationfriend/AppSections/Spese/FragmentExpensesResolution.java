package com.rbiffi.vacationfriend.AppSections.Spese;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rbiffi.vacationfriend.AppSections.Spese.Adapters.Resolution;
import com.rbiffi.vacationfriend.AppSections.Spese.Adapters.ResolutionAdapter;
import com.rbiffi.vacationfriend.R;

import java.util.List;

public class FragmentExpensesResolution extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expenses_resolution, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Resolution> genres = getResolutions();
        RecyclerView recyclerView = view.findViewById(R.id.resolution_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        ResolutionAdapter adapter = new ResolutionAdapter(getContext(), genres);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private List<Resolution> getResolutions() {
        //TODO crea Resolution e ResolutionItem a tua scelta
        return null;
    }
}
