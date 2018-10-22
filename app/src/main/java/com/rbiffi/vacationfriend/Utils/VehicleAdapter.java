package com.rbiffi.vacationfriend.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rbiffi.vacationfriend.R;

import java.util.List;

public class VehicleAdapter extends ArrayAdapter {

    public VehicleAdapter(@NonNull Context context, int resource, int textViewId, @NonNull List objects) {
        super(context, resource, objects);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getDropDownCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    private View getCustomView(int position, View convertView, ViewGroup parent) {
        // sostituisce il getview

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View row = inflater.inflate(R.layout.spinner_route_vehicle, parent, false);
        TextView label = row.findViewById(R.id.route_vehicle_label);
        String currentVehicle = (String) getItem(position);
        label.setText(currentVehicle);

        setVehicleIcon(row, currentVehicle);

        return row;
    }

    @NonNull
    private View getDropDownCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View row = inflater.inflate(R.layout.spinner_route_vehicle_dropdown, parent, false);
        TextView label = row.findViewById(R.id.route_vehicle_label);
        String currentVehicle = (String) getItem(position);
        label.setText(currentVehicle);

        setVehicleIcon(row, currentVehicle);

        return row;
    }

    private void setVehicleIcon(View row, String currentVehicle) {
        ImageView icon = row.findViewById(R.id.route_vehicle_icon);

        assert currentVehicle != null;
        if (currentVehicle.equals(getContext().getString(R.string.vehicle_foot)))
            icon.setImageResource(R.drawable.ic_directions_walk_white_24dp);
        else if (currentVehicle.equals(getContext().getString(R.string.vehicle_bike)))
            icon.setImageResource(R.drawable.ic_directions_bike_white_24dp);
        else if (currentVehicle.equals(getContext().getString(R.string.vehicle_car)))
            icon.setImageResource(R.drawable.ic_directions_car_white_24dp);
        else if (currentVehicle.equals(getContext().getString(R.string.vehicle_bus)))
            icon.setImageResource(R.drawable.ic_directions_bus_white_24dp);
        else if (currentVehicle.equals(getContext().getString(R.string.vehicle_train)))
            icon.setImageResource(R.drawable.ic_train_white_24dp);
        else if (currentVehicle.equals(getContext().getString(R.string.vehicle_plane)))
            icon.setImageResource(R.drawable.ic_directions_plane_white_24dp);
    }

}
