package com.rbiffi.vacationfriend.Repository.Entities_POJOs;

public class Step {

    private Stop stopPlace;
    private Vehicle vehicleToNextStop;


    public Stop getStopPlace() {
        return stopPlace;
    }

    public void setStopPlace(Stop stopPlace) {
        this.stopPlace = stopPlace;
    }

    public Vehicle getVehicleToNextStop() {
        return vehicleToNextStop;
    }

    public void setVehicleToNextStop(Vehicle vehicleToNextStop) {
        this.vehicleToNextStop = vehicleToNextStop;
    }
}
