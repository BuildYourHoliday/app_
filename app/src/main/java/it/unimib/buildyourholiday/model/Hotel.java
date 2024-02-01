package it.unimib.buildyourholiday.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Hotel {
    private String hotel;
    @PrimaryKey @NonNull
    private String hotelCode;
    private String cityCode;
    private String hotelCity;
    private String checkinDate;
    private String checkoutDate;
    private int adults;
    private double total;

    public Hotel() {

    }

    public Hotel(String hotel, String hotelCode, String cityCode, String hotelCity, String checkinDate, String checkoutDate, int adults, double total) {
        this.hotel = hotel;
        this.hotelCode = hotelCode;
        this.cityCode = cityCode;
        this.hotelCity = hotelCity;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.adults = adults;
        this.total = total;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getHotelCity() {
        return hotelCity;
    }

    public void setHotelCity(String hotelCity) {
        this.hotelCity = hotelCity;
    }

    public String getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(String checkinDate) {
        this.checkinDate = checkinDate;
    }

    public String getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
