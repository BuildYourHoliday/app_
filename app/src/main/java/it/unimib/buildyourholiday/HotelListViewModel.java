package it.unimib.buildyourholiday;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.amadeus.resources.HotelOfferSearch;

import java.util.List;

import it.unimib.buildyourholiday.model.Hotel;

public class HotelListViewModel extends ViewModel {
    private MutableLiveData<List<Hotel>> hotelListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<String>> hotelDescriptionLiveData = new MutableLiveData<>();
    private MutableLiveData<List<String>> hotelLinksLiveData = new MutableLiveData<>();
    private MutableLiveData<List<HotelOfferSearch>> hotelOffersLiveData = new MutableLiveData<>();
    private MutableLiveData<HotelOfferSearch> selectedHotelOfferLiveData = new MutableLiveData<>();

    private MutableLiveData<Boolean> resultsPriceError = new MutableLiveData<>(false);

    public MutableLiveData<List<Hotel>> getHotelListLiveData() {
        return hotelListLiveData;
    }

    public void setHotelListLiveData(List<Hotel> hotelList) {
        hotelListLiveData.setValue(hotelList);
    }

    public MutableLiveData<List<String>> getHotelDescriptionLiveData() {
        return hotelDescriptionLiveData;
    }

    public void setHotelDescriptionLiveData(List<String> hotelDescription) {
        hotelDescriptionLiveData.setValue(hotelDescription);
    }

    public MutableLiveData<List<String>> getHotelLinksLiveData() {
        return hotelLinksLiveData;
    }

    public void setHotelLinksLiveData(List<String> hotelLinks) {
        hotelLinksLiveData.setValue(hotelLinks);
    }

    public MutableLiveData<Boolean> getResultsPriceError() {
        return resultsPriceError;
    }

    public void setResultsPriceError(Boolean bool) {
        resultsPriceError.setValue(bool);
    }

    public MutableLiveData<List<HotelOfferSearch>> getHotelOffersLiveData() {
        return hotelOffersLiveData;
    }

    public void setHotelOffersLiveData(List<HotelOfferSearch> hotelOffers) {
        hotelOffersLiveData.setValue(hotelOffers);
    }

    public MutableLiveData<HotelOfferSearch> getSelectedHotelOfferLiveData() {
        return selectedHotelOfferLiveData;
    }

    public void setSelectedHotelOfferLiveData(HotelOfferSearch selectedHotelOffer) {
        selectedHotelOfferLiveData.setValue(selectedHotelOffer);
    }
}
