package nl.kreditor.model;

public interface Currency {
    String getCurrencyCode();
    String getSymbol();
    int getDefaultFractionDigits();
    int getNumericCode();
    String getDisplayName();
}
