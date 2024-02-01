package it.unimib.buildyourholiday.model;

public class Hotel {
    private String hotel;
    private String hotelCode;
    private String cityCode;
    private String city;
    private String checkinDate;
    private String checkoutDate;
    private int adults;
    private double total;

    public Hotel() {

    }

    public Hotel(String hotel, String hotelCode, String cityCode, String city, String checkinDate, String checkoutDate, int adults, double total) {
        this.hotel = hotel;
        this.hotelCode = hotelCode;
        this.cityCode = cityCode;
        this.city = city;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
