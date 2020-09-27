package nl.kreditor.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import nl.kreditor.component.transaction.PaymentCalculation;
import nl.kreditor.component.transaction.Transaction;
import nl.kreditor.form.OperationForm;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "operations")
public class Operation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private Book book;

    @LastModifiedBy
    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime created;

    @LastModifiedDate
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime modified;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private OperationType type = OperationType.EXPENSE;

    @Column(nullable = false)
    private PaymentType paymentType = PaymentType.UNKNOWN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "operation_id")
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "operation_id")
    private List<Allotment> allotments = new ArrayList<>();

    public Operation() {
    }

    public Operation(Book book, String name) {
        super();
        this.book = book;
        this.name = name;
        this.type = OperationType.REFUND;
    }

    public Operation(OperationForm operationForm, Book book) {
        super();
        this.book = book;
        this.created = LocalDateTime.now();
        this.modified = this.created;
        this.name = operationForm.getName();
        this.type = OperationType.EXPENSE;
    }

    //TODO
    public void setCategory(Category category) {
        if (!category.getBook().equals(this.book)) {
            throw new RuntimeException("Not your book!");
        }

        this.category = category;
    }

    public List<Transaction> transactions() {
        BigDecimal totalDue = new BigDecimal("0.00");

        List<Transaction> transactions = new ArrayList<>();

        for (Payment payment : payments) {
            totalDue = totalDue.add(payment.getAmount());
            System.out.printf("%s found: Adding '€ %s'%n", payment, payment.getAmount());
        }

        //je moet als 2 mensen x1.00 coeff hebben, totalCoeffs = 2.00 --> krijg naar 1 -> die factor mee dividen [later: doubt]

        BigDecimal alreadySettled = new BigDecimal("0.00").setScale(64, RoundingMode.HALF_EVEN);
        BigDecimal totalCoefficients = new BigDecimal("00000000").setScale(64, RoundingMode.HALF_EVEN);

        for (Payment payment : payments) {
            BigDecimal portionShareMultiplier = payment.getAmount().divide(totalDue, 64, RoundingMode.HALF_EVEN);

            for (Allotment allotment : allotments) {
                if (allotment.getType().equals(AllotmentType.FIXED)) {
                    Transaction t = allotment.payFixed(new PaymentCalculation(payment, portionShareMultiplier));
                    alreadySettled = alreadySettled.add(t.getAmount());
                    transactions.add(t);
                } else if (allotment.getType().equals(AllotmentType.COEFFICIENT)) {
                    totalCoefficients = totalCoefficients.add(allotment.getCoefficient());
                    System.out.println("[COEFFICIENT] Adding totalCoefficients " + totalCoefficients + " with " + allotment.getCoefficient());
                }
            }
        }

        System.out.println(alreadySettled);
        System.out.println(totalCoefficients);

        for (Payment payment : payments) {
            BigDecimal portionShareMultiplier;

            if (totalCoefficients.longValue() == 0) {
                System.out.println("GELIJK AAN NUL");
                portionShareMultiplier = payment.getAmount().divide(totalDue, 64, RoundingMode.HALF_EVEN);
            } else {
                BigDecimal correctedCoefficients = totalCoefficients.divide(BigDecimal.valueOf(payments.size()), RoundingMode.HALF_EVEN);

                portionShareMultiplier = payment.getAmount()
                        .divide(correctedCoefficients, 64, RoundingMode.HALF_EVEN)
                        .divide(totalDue, 64, RoundingMode.HALF_EVEN);

            } //todo mooi maken

            for (Allotment allotment : allotments) {
                if (allotment.getType().equals(AllotmentType.COEFFICIENT)) {
                    transactions.add(allotment.payCoefficient(
                            new PaymentCalculation(payment, portionShareMultiplier),
                            totalDue.subtract(alreadySettled)
                    ));
                }

                System.out.println("Left to due: " +totalDue);
            }
        }

        return transactions;
    }
}