package it.unimib.buildyourholiday.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
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

    private boolean expandable;
    private double totalPrice;
    @ColumnInfo(name = "is_synchronized")
    private boolean isSynchronized;

    @ColumnInfo(name = "is_saved")
    private boolean isSaved;

    @ColumnInfo(name = "is_booked")
    private boolean isBooked;


    public Travel() {}

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

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
        //this.expandable = false;

    }

    public Travel(String city, String beginDate, String finishDate, double totalPrice){
        this.city = city;
        this.beginDate = beginDate;
        this.finishDate = finishDate;
        this.totalPrice = totalPrice;
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

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
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

    @Override
    public String toString() {
        return "Travel{" +
                "flight=" + flight +
                ", hotel=" + hotel +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", beginDate='" + beginDate + '\'' +
                ", finishDate='" + finishDate + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
