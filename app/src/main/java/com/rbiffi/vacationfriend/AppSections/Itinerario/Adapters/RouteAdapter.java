package com.rbiffi.vacationfriend.AppSections.Itinerario.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.RouteDay;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.RouteElement;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Step;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Stop;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vehicle;
import com.rbiffi.vacationfriend.Utils.Converters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RouteAdapter
        extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder>
        implements
        StickyHeaderHandler {
    private static final int VIEW_TYPE_STOP_VIEW = 0;

    private static final int VIEW_TYPE_VEHICLE_VIEW = 1;
    private static final int VIEW_TYPE_VEHICLE_MISSING_VIEW = 2;
    private static final int VIEW_TYPE_STOP_HEADER = 3;
    private static final int VIEW_TYPE_STOP_FOOTER = 4;
    private static final int VIEW_TYPE_DAY = 5;
    private static final int VIEW_TYPE_FOOTER = 6;

    private final LayoutInflater inflater;
    private final Context context;
    private final List<RouteElement> route;
    private RecyclerView.LayoutManager recyclerLayout;

    public RouteAdapter(Context context, List<Step> steps) {
        super();
        inflater = LayoutInflater.from(context);

        this.context = context;
        this.route = buildRouteList(steps);
    }

    public void setListLayout(RecyclerView.LayoutManager routeLayout) {
        this.recyclerLayout = routeLayout;
    }

    private List<RouteElement> buildRouteList(List<Step> steps) {
        // IP la lista degli step è ordinata temporalmente
        List<RouteElement> result = new ArrayList<>();
        Date currentStopDay = new Date(0);

        for (Step step : steps) {
            Stop stop = step.getStopPlace();
            Vehicle vehicle = step.getVehicleToNextStop();
            Date stopDay = stop.day;

            if (currentStopDay.before(stopDay)) {
                currentStopDay = stopDay;
                RouteElement day = new RouteDay(currentStopDay);
                result.add(day);
            }
            result.add(stop);
            if (vehicle != null) result.add(vehicle);
        }

        return result;
    }

    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_STOP_VIEW:
                view = inflater.inflate(R.layout.fragment_route_stop_row, parent, false);
                break;
            case VIEW_TYPE_VEHICLE_VIEW:
                view = inflater.inflate(R.layout.fragment_route_vehicle_row, parent, false);
                break;
            case VIEW_TYPE_VEHICLE_MISSING_VIEW:
                view = inflater.inflate(R.layout.fragment_route_vehicle_missing_row, parent, false);
                break;
            case VIEW_TYPE_STOP_HEADER:
                view = inflater.inflate(R.layout.fragment_route_start_row, parent, false);
                break;
            case VIEW_TYPE_STOP_FOOTER:
                view = inflater.inflate(R.layout.fragment_route_end_row, parent, false);
                break;
            case VIEW_TYPE_DAY:
                view = inflater.inflate(R.layout.fragment_route_day_row, parent, false);
                break;
            case VIEW_TYPE_FOOTER:
                view = inflater.inflate(R.layout.lists_space_footer, parent, false);
                break;
            default:
                view = inflater.inflate(R.layout.fragment_route_stop_row, parent, false);
                break;
        }
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RouteViewHolder holder, int position) {
        //todo prendi l'elemento in posizione position e controlla il suo tipo per capire cosa fare.
        // inoltre se è in posizione 1 e size(), sono header e footer

        if (position >= route.size()) {
            ViewGroup.LayoutParams layoutParams = holder.spaceFooter.getLayoutParams();
            layoutParams.height = (int) Converters.convertDpToPixel(88);
            return;
        }

        RouteElement re = route.get(position);
        if (re instanceof Stop) {
            Stop stop = (Stop) re;
            holder.stopIcon.setImageURI(stop.stopIcon);
            holder.stopLabel.setText(stop.title);
            String arrival = stop.stopTime.arrivalTime == null ? "" : Converters.timeToUserInterface(stop.stopTime.arrivalTime);
            String departure = stop.stopTime.departureTime == null ? "" : Converters.timeToUserInterface(stop.stopTime.departureTime);

            if (arrival.isEmpty())
                holder.stopTime.setText(String.format(context.getString(R.string.route_stop_departure), departure));
            else if (departure.isEmpty())
                holder.stopTime.setText(String.format(context.getString(R.string.route_stop_arrival), arrival));
            else
                holder.stopTime.setText(String.format("%s ~ %s", arrival, departure));

            holder.stopPlace.setText(stop.place);
            holder.stopMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo popup menù. Serve un listener e prendi ispirazione da lista vacanze
                }
            });
        }
        if (re instanceof RouteDay) {
            RouteDay day = (RouteDay) re;
            holder.routeDay.setText(day.currentDay);
            //todo scroll al giorno odierno. Non so se è questo il posto giusto.
        }
        if (re instanceof Vehicle) {
            Vehicle vehicle = (Vehicle) re;
            if (vehicle.id != 0) {
                holder.vehicleIcon.setImageURI(vehicle.icon);
                holder.vehicleLabel.setText(vehicle.title);

                // todo da leggere/calcolare in qualche modo
                holder.vehicleTime.setText("15 min");
                holder.vehicleDistance.setText("1.5 km");
            }
        }
    }

    @Override
    public int getItemCount() {
        return route.size() + 1; // space footer
    }

    @Override
    public int getItemViewType(int position) {
        if (position == route.size()) return VIEW_TYPE_FOOTER;

        RouteElement re = route.get(position);
        if (re instanceof Stop) {
            Stop stop = (Stop) re;
            if (stop.stopTime.arrivalTime == null) return VIEW_TYPE_STOP_HEADER;
            if (stop.stopTime.departureTime == null) {
                return VIEW_TYPE_STOP_FOOTER; // todo ogni giorno ha un footer?
            }
            return VIEW_TYPE_STOP_VIEW;
        }
        if (re instanceof Vehicle) {
            Vehicle vehicle = (Vehicle) re;
            if (vehicle.id == 0)
                return VIEW_TYPE_VEHICLE_MISSING_VIEW;
            return VIEW_TYPE_VEHICLE_VIEW;
        }

        return VIEW_TYPE_DAY;
    }

    @Override
    public List<?> getAdapterData() { // stiky header
        return route;
    }

    class RouteViewHolder extends RecyclerView.ViewHolder {

        // Stop
        private final ImageView stopIcon;
        private final TextView stopLabel;
        private final TextView stopTime;
        private final ImageView stopMoreButton;
        private final TextView stopPlace;
        // todo ci possono essere problemi di id multipli tra stop, header e footer

        // Vehicle
        private final ImageView vehicleIcon;
        private final TextView vehicleLabel;
        private final TextView vehicleDistance;
        private final TextView vehicleTime;

        // Day
        private final TextView routeDay;

        // Space footer
        private final View spaceFooter;


        RouteViewHolder(View itemView) {
            super(itemView);

            stopIcon = itemView.findViewById(R.id.route_stop_icon);
            stopLabel = itemView.findViewById(R.id.route_stop_label);
            stopTime = itemView.findViewById(R.id.route_stop_time);
            stopMoreButton = itemView.findViewById(R.id.route_more_icon);
            stopPlace = itemView.findViewById(R.id.route_stop_location);

            vehicleIcon = itemView.findViewById(R.id.route_vehicle_icon);
            vehicleLabel = itemView.findViewById(R.id.route_vehicle_label);
            vehicleDistance = itemView.findViewById(R.id.route_vehicle_distance);
            vehicleTime = itemView.findViewById(R.id.route_vehicle_time);

            routeDay = itemView.findViewById(R.id.route_day);
            spaceFooter = itemView.findViewById((R.id.list_space_footer));
        }
    }
}