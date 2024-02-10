package it.unimib.buildyourholiday.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;

@Entity
public class Travel implements Parcelable {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeParcelable(this.flight, flags);
        dest.writeParcelable(this.hotel, flags);
        dest.writeString(this.city);
        dest.writeString(this.country);
        dest.writeString(this.beginDate);
        dest.writeString(this.finishDate);
        dest.writeByte(this.expandable ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.totalPrice);
        dest.writeByte(this.isSynchronized ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSaved ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isBooked ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readLong();
        this.flight = source.readParcelable(Flight.class.getClassLoader());
        this.hotel = source.readParcelable(Hotel.class.getClassLoader());
        this.city = source.readString();
        this.country = source.readString();
        this.beginDate = source.readString();
        this.finishDate = source.readString();
        this.expandable = source.readByte() != 0;
        this.totalPrice = source.readDouble();
        this.isSynchronized = source.readByte() != 0;
        this.isSaved = source.readByte() != 0;
        this.isBooked = source.readByte() != 0;
    }

    protected Travel(Parcel in) {
        this.id = in.readLong();
        this.flight = in.readParcelable(Flight.class.getClassLoader());
        this.hotel = in.readParcelable(Hotel.class.getClassLoader());
        this.city = in.readString();
        this.country = in.readString();
        this.beginDate = in.readString();
        this.finishDate = in.readString();
        this.expandable = in.readByte() != 0;
        this.totalPrice = in.readDouble();
        this.isSynchronized = in.readByte() != 0;
        this.isSaved = in.readByte() != 0;
        this.isBooked = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Travel> CREATOR = new Parcelable.Creator<Travel>() {
        @Override
        public Travel createFromParcel(Parcel source) {
            return new Travel(source);
        }

        @Override
        public Travel[] newArray(int size) {
            return new Travel[size];
        }
    };
}
