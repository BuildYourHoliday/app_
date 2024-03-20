package it.unimib.buildyourholiday.data.service;

public class NoHotelsForPriceException extends Exception{
    public NoHotelsForPriceException() {
        super("No hotels found for the given price");
    }

    public NoHotelsForPriceException(String message) {
        super(message);
    }
}
