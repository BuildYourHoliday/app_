package it.unimib.buildyourholiday;

public class NoHotelsForPriceException extends Exception{
    public NoHotelsForPriceException() {
        super("No hotels found for the given price");
    }

    public NoHotelsForPriceException(String message) {
        super(message);
    }
}
