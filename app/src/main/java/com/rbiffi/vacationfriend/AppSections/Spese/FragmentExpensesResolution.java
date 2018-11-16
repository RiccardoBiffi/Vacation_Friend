package com.rbiffi.vacationfriend.AppSections.Spese;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.ResolutionItem;
import com.rbiffi.vacationfriend.Utils.Converters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentExpensesResolution extends Fragment {

    private RecyclerView recyclerView;
    private ResolutionAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expenses_resolution, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Resolution> genres = getResolutions();
        RecyclerView recyclerView = view.findViewById(R.id.resolution_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        adapter = new ResolutionAdapter(getContext(), genres);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }

    private List<Resolution> getResolutions() {
        Uri myself = Uri.parse("android.resource://com.rbiffi.vacationfriend/" + R.drawable.average_man);
        Uri sara = Uri.parse("android.resource://com.rbiffi.vacationfriend/" + R.drawable.blonde_woman);

        Date ago22 = Converters.timestampToDate("2018-08-22");
        ResolutionItem resItemSara1 = new ResolutionItem(myself, getString(R.string.lunch_mahnic), "+", "10,00€", ago22);

        Date ago20 = Converters.timestampToDate("2018-08-20");
        ResolutionItem resItemSara2 = new ResolutionItem(sara, getString(R.string.parking_rovigno), "-", "2,50€", ago20);

        Date ago19 = Converters.timestampToDate("2018-08-19");
        ResolutionItem resItemSara3 = new ResolutionItem(sara, getString(R.string.breakfast), "-", "1,00€", ago19);

        List<ResolutionItem> rilSara = new ArrayList<>();
        rilSara.add(resItemSara1);
        rilSara.add(resItemSara2);
        rilSara.add(resItemSara3);

        Resolution r1 = new Resolution("Test", rilSara);
        r1.setAction(getString(R.string.resolution_receive));
        r1.setGroupValue("8,50€");
        r1.setActionDirection(getString(R.string.resolution_from));
        r1.setPersonIcon(sara);
        r1.setPersonName(getString(R.string.sara));


        Uri andre = Uri.parse("android.resource://com.rbiffi.vacationfriend/" + R.drawable.black_man);

        ResolutionItem resItemAndre1 = new ResolutionItem(andre, getString(R.string.dinner_pola), "-", "18,50€", ago22);
        ResolutionItem resItemAndre2 = new ResolutionItem(myself, getString(R.string.popcorn), "+", "3,00€", ago20);

        List<ResolutionItem> rilAndre = new ArrayList<>();
        rilAndre.add(resItemAndre1);
        rilAndre.add(resItemAndre2);

        Resolution r2 = new Resolution("Test", rilAndre);
        r2.setAction(getString(R.string.resolution_give));
        r2.setGroupValue("15,50€");
        r2.setActionDirection(getString(R.string.resolution_to));
        r2.setPersonIcon(andre);
        r2.setPersonName(getString(R.string.andre));


        Uri marco = Uri.parse("android.resource://com.rbiffi.vacationfriend/" + R.drawable.businnes_man);

        ResolutionItem resItemMarco1 = new ResolutionItem(marco, getString(R.string.concert_rovigno), "-", "25,00€", ago20);
        ResolutionItem resItemMarco2 = new ResolutionItem(myself, getString(R.string.lunch_mahnic), "+", "10,00€", ago20);
        ResolutionItem resItemMarco3 = new ResolutionItem(myself, getString(R.string.umbrella_beach), "+", "9,75€", ago19);

        List<ResolutionItem> rilMarco = new ArrayList<>();
        rilMarco.add(resItemMarco1);
        rilMarco.add(resItemMarco2);
        rilMarco.add(resItemMarco3);

        Resolution r3 = new Resolution("Test", rilMarco);
        r3.setAction(getString(R.string.resolution_give));
        r3.setGroupValue("5,25€");
        r3.setActionDirection(getString(R.string.resolution_to));
        r3.setPersonIcon(marco);
        r3.setPersonName(getString(R.string.marco));

        List<Resolution> rl = new ArrayList<>();
        rl.add(r1);
        rl.add(r2);
        rl.add(r3);

        return rl;
    }
}
