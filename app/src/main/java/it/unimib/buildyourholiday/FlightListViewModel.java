package it.unimib.buildyourholiday;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.amadeus.resources.FlightOfferSearch;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.List;

import it.unimib.buildyourholiday.model.Flight;

public class FlightListViewModel extends ViewModel {
    private MutableLiveData<List<Flight>> flightListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<String>> durationsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Boolean>> directFlightLiveData = new MutableLiveData<>();
    private MutableLiveData<List<FlightOfferSearch>> flightOffersMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<FlightOfferSearch> selectedFlightOfferMutableLiveData = new MutableLiveData<>();



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

    public void setFlightOffers(List<FlightOfferSearch> flightOfferSearch) {
        flightOffersMutableLiveData.setValue(flightOfferSearch);
    }
    public MutableLiveData<List<FlightOfferSearch>> getFlightOffersMutableLiveData() {
        return flightOffersMutableLiveData;
    }

    public MutableLiveData<FlightOfferSearch> getSelectedFlightOfferMutableLiveData() {
        return selectedFlightOfferMutableLiveData;
    }

    public void setSelectedFlightOffer(FlightOfferSearch selectedFlightOffer) {
        selectedFlightOfferMutableLiveData.setValue(selectedFlightOffer);
    }
}
