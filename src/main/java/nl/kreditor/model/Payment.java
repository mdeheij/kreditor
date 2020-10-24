package nl.kreditor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @Setter
    private BigDecimal amount = new BigDecimal("0.00");

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contact_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Contact payer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "operation_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Operation operation;

    public Payment() {
    }

    public Payment(BigDecimal amount, Contact payer, Operation operation) {
        this.amount = amount;
        this.payer = payer;
        this.operation = operation;
    }
}
