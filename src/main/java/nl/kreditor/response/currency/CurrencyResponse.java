package nl.kreditor.response.currency;

import lombok.Data;
import nl.kreditor.model.Currency;
import nl.kreditor.model.CurrencySource;

@Data
public class CurrencyResponse {
    private String code;
    private String displayName;
    private CurrencySource source;

    public CurrencyResponse() {}

    public CurrencyResponse(Currency currency) {
        this.code = currency.getCurrencyCode();
        this.displayName = currency.getDisplayName();
        this.source = CurrencySource.BOOK;
    }

    public CurrencyResponse(java.util.Currency currency) {
        this.code = currency.getCurrencyCode();
        this.displayName = currency.getDisplayName();
        this.source = CurrencySource.SYSTEM;
    }
}
