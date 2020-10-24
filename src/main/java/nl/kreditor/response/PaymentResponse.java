package nl.kreditor.response;

import lombok.Data;
import nl.kreditor.model.Payment;

import java.math.BigDecimal;

@Data
public class PaymentResponse {
    private Integer id;
    private int contactId;
    private BigDecimal amount;

    public PaymentResponse(int id, int contactId, BigDecimal amount) {
        this.id = id;
        this.contactId = contactId;
        this.amount = amount;
    }

    public PaymentResponse(Payment payment) {
        this.id = payment.getId();
        this.contactId = payment.getPayer().getId();
        this.amount = payment.getAmount();
    }
}
