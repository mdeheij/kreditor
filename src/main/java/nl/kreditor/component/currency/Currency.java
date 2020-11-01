package nl.kreditor.component.currency;

public interface Currency {
    String getCurrencyCode();
    String getSymbol();
    int getDefaultFractionDigits();
    int getNumericCode();
    String getDisplayName();
}
