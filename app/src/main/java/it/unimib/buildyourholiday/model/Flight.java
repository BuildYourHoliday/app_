package it.unimib.buildyourholiday.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Flight implements Parcelable {
    @PrimaryKey @NonNull
    private String code;
    private String departureDate;
    private String departureTime;
    private String departureAirport;
    private String returnalDate;
    private String returnalTime;
    private String arrivalAirport;
    private double price;

    public Flight() {

    }

    public Flight(String code, String departureDate, String departureTime, String departureAirport, String returnalDate, String returnalTime, String arrivalAirport, double price) {
        this.code = code;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.departureAirport = departureAirport;
        this.returnalDate = returnalDate;
        this.returnalTime = returnalTime;
        this.arrivalAirport = arrivalAirport;
        this.price = price;
    }

    public Flight(String code, String departureDate, String departureTime, String departureAirport, String arrivalAirport, double price) {
        this.code = code;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getReturnalDate() {
        return returnalDate;
    }

    public void setReturnalDate(String returnalDate) {
        this.returnalDate = returnalDate;
    }

    public String getReturnalTime() {
        return returnalTime;
    }

    public void setReturnalTime(String returnalTime) {
        this.returnalTime = returnalTime;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String toString() {
        return getCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return code.equals(flight.code) && Objects.equals(departureDate, flight.departureDate) && Objects.equals(departureTime, flight.departureTime) && Objects.equals(departureAirport, flight.departureAirport) && Objects.equals(returnalDate, flight.returnalDate) && Objects.equals(returnalTime, flight.returnalTime) && Objects.equals(arrivalAirport, flight.arrivalAirport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, departureDate, departureTime, departureAirport, returnalDate, returnalTime, arrivalAirport);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.departureDate);
        dest.writeString(this.departureTime);
        dest.writeString(this.departureAirport);
        dest.writeString(this.returnalDate);
        dest.writeString(this.returnalTime);
        dest.writeString(this.arrivalAirport);
        dest.writeDouble(this.price);
    }

    public void readFromParcel(Parcel source) {
        this.code = source.readString();
        this.departureDate = source.readString();
        this.departureTime = source.readString();
        this.departureAirport = source.readString();
        this.returnalDate = source.readString();
        this.returnalTime = source.readString();
        this.arrivalAirport = source.readString();
        this.price = source.readDouble();
    }

    protected Flight(Parcel in) {
        this.code = in.readString();
        this.departureDate = in.readString();
        this.departureTime = in.readString();
        this.departureAirport = in.readString();
        this.returnalDate = in.readString();
        this.returnalTime = in.readString();
        this.arrivalAirport = in.readString();
        this.price = in.readDouble();
    }

    public static final Parcelable.Creator<Flight> CREATOR = new Parcelable.Creator<Flight>() {
        @Override
        public Flight createFromParcel(Parcel source) {
            return new Flight(source);
        }

        @Override
        public Flight[] newArray(int size) {
            return new Flight[size];
        }
    };
}
