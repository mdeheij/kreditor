package nl.kreditor.model;

import java.util.Locale;

public interface Currency {
    String getCurrencyCode();
    String getSymbol();
    int getDefaultFractionDigits();
    int getNumericCode();
    String getDisplayName();
}
