package nl.kreditor.response;

import lombok.Data;
import nl.kreditor.component.transaction.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class TransactionResponse {
    private ContactResponse from, to;
    private BigDecimal amount;
    private String currency;

    public TransactionResponse(Transaction transaction) {
        from = new ContactResponse(transaction.getFrom());
        to = new ContactResponse(transaction.getTo());
        currency = transaction.getCurrency().getCurrencyCode();
        amount = transaction.getAmount().setScale(2, RoundingMode.HALF_EVEN);
    }
}
