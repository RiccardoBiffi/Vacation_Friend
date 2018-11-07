package com.rbiffi.vacationfriend.AppSections.Itinerario.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler;
import com.rbiffi.vacationfriend.AppSections.Itinerario.Events.IRouteClickEvents;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.RouteDay;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.RouteElement;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Step;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Stop;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vehicle;
import com.rbiffi.vacationfriend.Utils.Converters;
import com.rbiffi.vacationfriend.Utils.VehicleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RouteAdapter
        extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder>
        implements
        StickyHeaderHandler {
    private static final int VIEW_TYPE_STOP_VIEW = 0;

    private static final int VIEW_TYPE_VEHICLE_VIEW = 1;
    private static final int VIEW_TYPE_STOP_HEADER = 3;
    private static final int VIEW_TYPE_STOP_FOOTER = 4;
    private static final int VIEW_TYPE_DAY = 5;
    private static final int VIEW_TYPE_FOOTER = 6;

    private final LayoutInflater inflater;
    private final Context context;
    private final List<RouteElement> route;
    private final List<String> vehicles;
    private final List<String> vehicles_missing;

    private RecyclerView.LayoutManager recyclerLayout;
    private IRouteClickEvents listener;

    public RouteAdapter(Context context, List<Step> steps) {
        super();
        inflater = LayoutInflater.from(context);

        this.context = context;
        this.route = buildRouteList(steps);
        // lo wrappo in un ArrayList perchè altrimenti è una lista non modificabile
        this.vehicles = Arrays.asList(context.getResources().getStringArray(R.array.route_vehicles));
        this.vehicles_missing = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.route_vehicles_missing)));

    }

    public void setListLayout(RecyclerView.LayoutManager routeLayout) {
        this.recyclerLayout = routeLayout;
    }

    public void setListener(IRouteClickEvents listener) {
        this.listener = listener;
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
    public void onBindViewHolder(final RouteViewHolder holder, int position) {
        //todo prendi l'elemento in posizione position e controlla il suo tipo per capire cosa fare.
        // inoltre se è in posizione 1 e size(), sono header e footer

        if (position >= route.size()) {
            ViewGroup.LayoutParams layoutParams = holder.spaceFooter.getLayoutParams();
            layoutParams.height = (int) Converters.convertDpToPixel(88);
            return;
        }

        RouteElement re = route.get(position);
        if (re instanceof Stop) {
            final Stop stop = (Stop) re;
            holder.stopIcon.setImageURI(stop.stopIcon);
            holder.stopLabel.setText(stop.title);
            String arrival = stop.stopTime.arrivalTime == null ? "" : Converters.timeToUserInterface(stop.stopTime.arrivalTime);
            String departure = stop.stopTime.departureTime == null ? "" : Converters.timeToUserInterface(stop.stopTime.departureTime);

            holder.stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, context.getString(R.string.stop_details), Toast.LENGTH_SHORT).show();
                }
            });

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
                    if (listener != null) {
                        listener.onOverflowClick(v, stop);
                    }
                }
            });
        }
        if (re instanceof RouteDay) {
            RouteDay day = (RouteDay) re;
            holder.routeDay.setText(day.currentDay);
            //todo scroll al giorno odierno. Non so se è questo il posto giusto.
        }
        if (re instanceof Vehicle) {
            final Vehicle vehicle = (Vehicle) re;

            ArrayAdapter vehicleAdapter = new VehicleAdapter(context, R.layout.spinner_route_vehicle,
                    R.id.route_vehicle_label, vehicles);
            vehicleAdapter.setDropDownViewResource(R.layout.spinner_route_vehicle_dropdown);
            holder.vehicleSpinner.setAdapter(vehicleAdapter);
            holder.vehicleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    updateTimeAndDistance(holder, position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            if (vehicle.id != 0) { // il veicolo è specificato
                holder.vehicleViewGroup.setBackgroundResource(R.drawable.shape_route_vehicle);
                holder.vehicleMissingSpinner.setVisibility(View.GONE);
                holder.vehiclePresentGroup.setVisibility(View.VISIBLE);

                holder.vehicleSpinner.setSelection((int) vehicle.id - 1);
                updateTimeAndDistance(holder, (int) vehicle.id - 1);

            } else { // il veicolo non è specificato
                ArrayAdapter<String> vehicleMissingAdapter = new VehicleAdapter(context, R.layout.spinner_route_vehicle,
                        R.id.route_vehicle_label, vehicles_missing) {

                    @Override
                    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View v = super.getDropDownView(position, convertView, parent);
                        if (position == 0) {
                            v.setBackgroundColor(ResourcesCompat.getColor(context.getResources(),
                                    R.color.colorSecondary, null));
                        }
                        return v;
                    }
                };

                vehicleMissingAdapter.setDropDownViewResource(R.layout.spinner_route_vehicle_dropdown);
                holder.vehicleMissingSpinner.setAdapter(vehicleMissingAdapter);

                holder.vehicleMissingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            holder.vehicleViewGroup.setBackgroundResource(R.drawable.shape_route_vehicle);
                            holder.vehicleMissingSpinner.setVisibility(View.GONE);
                            holder.vehiclePresentGroup.setVisibility(View.VISIBLE);
                            holder.vehicleSpinner.setSelection(position - 1);
                            updateTimeAndDistance(holder, position - 1);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        }
    }

    private void updateTimeAndDistance(RouteViewHolder holder, int position) {
        switch (position) {
            case 0:
                holder.vehicleTime.setText("1:20 min");
                holder.vehicleDistance.setText("1.5 km");
                break;
            case 1:
                holder.vehicleTime.setText("30 min");
                holder.vehicleDistance.setText("1.7 km");
                break;
            case 2:
                holder.vehicleTime.setText("15 min");
                holder.vehicleDistance.setText("2.4 km");
                break;
            case 3:
                holder.vehicleTime.setText("20 min");
                holder.vehicleDistance.setText("2.9 km");
                break;
            case 4:
                holder.vehicleTime.setText("10 min");
                holder.vehicleDistance.setText("3.4 km");
                break;
            default:
                holder.vehicleTime.setText("15 min");
                holder.vehicleDistance.setText("1.5 km");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return route.size() + 1; // +1 per space footer
    }

    @Override
    public int getItemViewType(int position) {
        if (position == route.size()) return VIEW_TYPE_FOOTER;

        RouteElement re = route.get(position);
        if (re instanceof Stop) {
            Stop stop = (Stop) re;
            if (stop.stopTime.arrivalTime == null) return VIEW_TYPE_STOP_HEADER;
            if (stop.stopTime.departureTime == null) {
                return VIEW_TYPE_STOP_FOOTER;
            }
            return VIEW_TYPE_STOP_VIEW;
        }

        if (re instanceof Vehicle) {
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
        private final ViewGroup stop;
        private final ImageView stopIcon;
        private final TextView stopLabel;
        private final TextView stopTime;
        private final ImageView stopMoreButton;
        private final TextView stopPlace;

        // Vehicle
        private final ViewGroup vehicleViewGroup;
        private final Spinner vehicleMissingSpinner;
        private final ViewGroup vehiclePresentGroup;
        private final Spinner vehicleSpinner;
        private final TextView vehicleDistance;
        private final TextView vehicleTime;

        // Day
        private final TextView routeDay;

        // Space footer
        private final View spaceFooter;


        RouteViewHolder(View itemView) {
            super(itemView);

            stop = itemView.findViewById(R.id.route_stop);
            stopIcon = itemView.findViewById(R.id.route_stop_icon);
            stopLabel = itemView.findViewById(R.id.route_stop_label);
            stopTime = itemView.findViewById(R.id.route_stop_time);
            stopMoreButton = itemView.findViewById(R.id.route_more_icon);
            stopPlace = itemView.findViewById(R.id.route_stop_location);

            vehicleViewGroup = itemView.findViewById(R.id.route_vehicle_group);
            vehicleMissingSpinner = itemView.findViewById(R.id.spinner_vehicle_missing);
            vehiclePresentGroup = itemView.findViewById(R.id.route_vehicle_present_group);
            vehicleSpinner = itemView.findViewById(R.id.spinner_vehicle);
            vehicleDistance = itemView.findViewById(R.id.route_vehicle_distance);
            vehicleTime = itemView.findViewById(R.id.route_vehicle_time);

            routeDay = itemView.findViewById(R.id.route_day);
            spaceFooter = itemView.findViewById((R.id.list_space_footer));
        }
    }
}
