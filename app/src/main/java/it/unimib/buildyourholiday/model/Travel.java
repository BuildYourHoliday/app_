package it.unimib.buildyourholiday.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Travel {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @Embedded
    private Flight flight;
    @Embedded
    private Hotel hotel;
    private String city;
    private String country;
    private String beginDate;
    private String finishDate;
    private double totalPrice;
    @ColumnInfo(name = "is_synchronized")
    private boolean isSynchronized;

    public Travel() {}

    public Travel(Flight flight, Hotel hotel) {
        this.flight = flight;
        this.hotel = hotel;
        this.city = hotel.getHotelCity();
        this.beginDate = flight.getDepartureDate();
        if(flight.getReturnalDate()!=null)
            this.finishDate = flight.getReturnalDate();
        else
            this.finishDate = hotel.getCheckoutDate();
        this.totalPrice = flight.getPrice() + hotel.getTotal();

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Travel travel = (Travel) o;
        return Double.compare(travel.totalPrice, totalPrice) == 0 && Objects.equals(flight, travel.flight) && Objects.equals(hotel, travel.hotel) && Objects.equals(city, travel.city) && Objects.equals(country, travel.country) && Objects.equals(beginDate, travel.beginDate) && Objects.equals(finishDate, travel.finishDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flight, hotel, city, country, beginDate, finishDate, totalPrice);
    }
}
