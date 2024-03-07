package it.unimib.buildyourholiday;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.Serializable;
import java.util.List;

import it.unimib.buildyourholiday.model.Flight;

public class FlightListViewModel extends ViewModel {
    private MutableLiveData<List<Flight>> flightListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<String>> durationsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Boolean>> directFlightLiveData = new MutableLiveData<>();

    // private MediatorLiveData



    public void setFlightList(List<Flight> flightList) {
        flightListLiveData.setValue(flightList);
    }

    public MutableLiveData<List<Flight>> getFlightListLiveData() {
        return flightListLiveData;
    }

    public void setDurations(List<String> durations) {
        durationsLiveData.setValue(durations);
    }

    public MutableLiveData<List<String>> getDurationsLiveData() {
        return durationsLiveData;
    }

    public void setDirectFlight(List<Boolean> directFlight) {
        directFlightLiveData.setValue(directFlight);
    }

    public MutableLiveData<List<Boolean>> getDirectFlightLiveData() {
        return directFlightLiveData;
    }
}
