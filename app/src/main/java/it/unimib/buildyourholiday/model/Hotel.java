package it.unimib.buildyourholiday.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Hotel implements Parcelable {
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

    @Override
    public String toString() {
        return "Hotel{" +
                "hotel='" + hotel + '\'' +
                ", hotelCode='" + hotelCode + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", hotelCity='" + hotelCity + '\'' +
                ", checkinDate='" + checkinDate + '\'' +
                ", checkoutDate='" + checkoutDate + '\'' +
                ", adults=" + adults +
                ", total=" + total +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return adults == hotel.adults && hotelCode.equals(hotel.hotelCode) && Objects.equals(checkinDate, hotel.checkinDate) && Objects.equals(checkoutDate, hotel.checkoutDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hotelCode, checkinDate, checkoutDate, adults);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.hotel);
        dest.writeString(this.hotelCode);
        dest.writeString(this.cityCode);
        dest.writeString(this.hotelCity);
        dest.writeString(this.checkinDate);
        dest.writeString(this.checkoutDate);
        dest.writeInt(this.adults);
        dest.writeDouble(this.total);
    }

    public void readFromParcel(Parcel source) {
        this.hotel = source.readString();
        this.hotelCode = source.readString();
        this.cityCode = source.readString();
        this.hotelCity = source.readString();
        this.checkinDate = source.readString();
        this.checkoutDate = source.readString();
        this.adults = source.readInt();
        this.total = source.readDouble();
    }

    protected Hotel(Parcel in) {
        this.hotel = in.readString();
        this.hotelCode = in.readString();
        this.cityCode = in.readString();
        this.hotelCity = in.readString();
        this.checkinDate = in.readString();
        this.checkoutDate = in.readString();
        this.adults = in.readInt();
        this.total = in.readDouble();
    }

    public static final Parcelable.Creator<Hotel> CREATOR = new Parcelable.Creator<Hotel>() {
        @Override
        public Hotel createFromParcel(Parcel source) {
            return new Hotel(source);
        }

        @Override
        public Hotel[] newArray(int size) {
            return new Hotel[size];
        }
    };
}
