package nl.kreditor.component.transaction;

import lombok.Data;
import nl.kreditor.model.Payment;

import java.math.BigDecimal;

@Data
public class PaymentCalculation {
    private Payment payment;
    private BigDecimal ratio;

    public PaymentCalculation(Payment payment, BigDecimal ratio) {
        this.payment = payment;
        this.ratio = ratio;
    }
}
