package nl.kreditor.component.currency;

import lombok.Getter;

public enum CurrencyEnum {
    EUR("Euro", "€"),
    GBP("Pound sterling", "£"),
    PLN("Poland złoty", "zł"),
    GEL("Georgian Lari", "ლ"),
    USD("Dollar", "$");

    @Getter
    final
    String name;

    @Getter
    final
    String symbol;

    CurrencyEnum(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }
}
