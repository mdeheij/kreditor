package nl.kreditor.component.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.kreditor.component.solver.Member;
import nl.kreditor.component.currency.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class Transaction {

    @JsonIgnore
    private Member from;

    @JsonIgnore
    private Member to;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Currency currency;

    private BigDecimal amount;

    public Transaction(Member from, Member to, Currency currency, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.currency = currency;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "from=" + from.getName() +
                ", to=" + to.getName() +
                ", currency=" + currency.getDisplayName() +
                ", amount=" + amount.setScale(2, RoundingMode.HALF_EVEN) +
                '}';
    }
}
