package com.rbiffi.vacationfriend.AppSections.Itinerario.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Step;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private static final int VIEW_TYPE_STOP_VIEW = 0;
    private static final int VIEW_TYPE_VEHICLE_VIEW = 1;
    private static final int VIEW_TYPE_HEADER = 2;
    private static final int VIEW_TYPE_FOOTER = 3;
    private static final int VIEW_TYPE_DAY = 4;

    private int HEADERS_NUM;

    public RouteAdapter(Context context, List<Step> steps) {
        super();
    }

    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RouteViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RouteViewHolder extends RecyclerView.ViewHolder {
        public RouteViewHolder(View itemView) {
            super(itemView);
        }
    }
}
