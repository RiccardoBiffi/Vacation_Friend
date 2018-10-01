package com.rbiffi.vacationfriend.AppSections.Itinerario.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.RouteDay;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.RouteElement;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Step;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Stop;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vehicle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RouteAdapter
        extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder>
        implements
        StickyHeaderHandler {

    private static final int VIEW_TYPE_STOP_VIEW = 0;
    private static final int VIEW_TYPE_VEHICLE_VIEW = 1;
    private static final int VIEW_TYPE_HEADER = 2;
    private static final int VIEW_TYPE_FOOTER = 3;
    private static final int VIEW_TYPE_DAY = 4;

    private int HEADERS_NUM;

    private final LayoutInflater inflater;
    private final Context context;
    private final List<RouteElement> route;

    public RouteAdapter(Context context, List<Step> steps) {
        super();
        inflater = LayoutInflater.from(context);

        this.context = context;
        this.route = buildRouteList(steps);
    }

    private List<RouteElement> buildRouteList(List<Step> steps) {
        // IP la lista degli step è ordinata temporalmente
        List<RouteElement> result = new ArrayList<>();
        Date currentStopDay = new Date(0);

        for (Step step : steps) {
            Stop stop = step.getStopPlace();
            Vehicle vehicle = step.getVehicleToNextStop();
            Date stopDay = stop.day;

            if (currentStopDay.before(stopDay)) { // todo il controllo è troppo preciso
                currentStopDay = stopDay;
                RouteElement day = new RouteDay(currentStopDay);
                result.add(day);
            }
            result.add(stop);
            result.add(vehicle);
        }

        return result;
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

    @Override
    public List<?> getAdapterData() { // stiky header
        return route;
    }

    public class RouteViewHolder extends RecyclerView.ViewHolder {

        public RouteViewHolder(View itemView) {
            super(itemView);
        }
    }
}
