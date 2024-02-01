package it.unimib.buildyourholiday.model;

public class Travel {
    private Flight flight;
    private Hotel hotel;
    private String city;
    private String country;
    private String beginDate;
    private String finishDate;
    private double totalPrice;
    private boolean isSynchronized;

    public Travel() {

    }

    public Travel(Flight flight, Hotel hotel) {
        this.flight = flight;
        this.hotel = hotel;
        this.city = hotel.getCity();
        this.beginDate = flight.getDepartureDate();
        if(flight.getReturnalDate()!=null)
            this.finishDate = flight.getReturnalDate();
        else
            this.finishDate = hotel.getCheckoutDate();
        this.totalPrice = flight.getPrice() + hotel.getTotal();

    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isSynchronized() {
        return isSynchronized;
    }

    public void setSynchronized(boolean aSynchronized) {
        isSynchronized = aSynchronized;
    }
}
