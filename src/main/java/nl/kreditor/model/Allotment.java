package nl.kreditor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import nl.kreditor.component.currency.SystemCurrency;
import nl.kreditor.component.transaction.PaymentCalculation;
import nl.kreditor.component.transaction.Transaction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "allotments")
public class Allotment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private AllotmentType type = AllotmentType.COEFFICIENT;

    @Column
    private BigDecimal coefficient = new BigDecimal("0.00000000000000000");

    @Column
    private BigDecimal expense = new BigDecimal("0.00");

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contact_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Contact contact;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "operation_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties("operation")
    @JsonIgnore
    private Operation operation;

    @LastModifiedBy
    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime created;

    @LastModifiedDate
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime modified;

    private boolean valid = false; //TODO

    public Allotment() {
        this.created = LocalDateTime.now();
        this.modified = LocalDateTime.now();
    }

    public Allotment(AllotmentType type, BigDecimal coefficient, BigDecimal expense, Contact contact, Operation operation) {
        super();
        this.type = type;
        this.coefficient = coefficient;
        this.expense = expense;
        this.contact = contact;
        this.operation = operation;
    }

    public Transaction payFixed(PaymentCalculation paymentCalculation) {
        BigDecimal amount = this.getExpense().multiply(paymentCalculation.getRatio());

        return new Transaction(
                this.getContact(),
                paymentCalculation.getPayment().getPayer(),
                new SystemCurrency("EUR"),
                amount
        );
    }

    public Transaction payCoefficient(PaymentCalculation calculation, BigDecimal leftToSettle) {
        BigDecimal amount = this.getCoefficient().multiply((leftToSettle.multiply(calculation.getRatio())));

        return new Transaction(
                this.getContact(),
                calculation.getPayment().getPayer(),
                new SystemCurrency("EUR"),
                amount
        );
    }

    @Override
    public String toString() {
        return "Allotment{" +
                "type=" + type +
                ", coefficient=" + coefficient +
                ", expense=" + expense +
                ", contact=" + contact.getName() +
                '}';
    }

}
