package nl.kreditor.component.currency;

import java.util.Currency;

public class SystemCurrency implements nl.kreditor.component.currency.Currency {
    private final Currency currency;

    public SystemCurrency(String currencyCode) {
        currency = Currency.getInstance(currencyCode);
    }

    @Override
    public String getCurrencyCode() {
        return currency.getCurrencyCode();
    }

    @Override
    public String getSymbol() {
        return currency.getSymbol();
    }

    @Override
    public int getDefaultFractionDigits() {
        return currency.getDefaultFractionDigits();
    }

    @Override
    public int getNumericCode() {
        return currency.getNumericCode();
    }

    @Override
    public String getDisplayName() {
        return currency.getDisplayName();
    }
}
