package nl.kreditor.model;

import nl.kreditor.component.currency.Currency;

public class PersistedCurrency implements Currency {
    private String code;
    private String symbol;
    private String name;

    public PersistedCurrency() {
        super();
    }
    public PersistedCurrency(String code, String symbol, String name) {
        this.code = code;
        this.symbol = symbol;
        this.name = name;
    }

    @Override
    public String getCurrencyCode() {
        return code;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public int getDefaultFractionDigits() {
        return 0;
    }

    @Override
    public int getNumericCode() {
        return 0;
    }

    @Override
    public String getDisplayName() {
        return name;
    }
}
