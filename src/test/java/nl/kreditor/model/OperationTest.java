package nl.kreditor.model;

import nl.kreditor.component.currency.SystemCurrency;
import nl.kreditor.component.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OperationTest {
    private Contact henk;
    private Contact jan;
    private Contact ernst;
    private Contact sofia;

    private Payment henksPayment;
    private Payment jansPayment;
    private Payment sofiasPayment;

    private Allotment ernstsMeal;
    private Allotment jansDrink;

    @BeforeEach
    void setUp() {
        henk = new Contact();
        henk.setId(1);
        henk.setName("Henk");

        jan = new Contact();
        jan.setId(2);
        jan.setName("Jan");

        ernst = new Contact();
        ernst.setId(3);
        ernst.setName("Ernst");

        sofia = new Contact();
        sofia.setId(4);
        sofia.setName("Sofia");

        henksPayment = new Payment();
        henksPayment.setPayer(henk);
        henksPayment.setAmount(BigDecimal.valueOf(5));

        jansPayment = new Payment();
        jansPayment.setPayer(jan);
        jansPayment.setAmount(BigDecimal.valueOf(10));

        sofiasPayment = new Payment();
        sofiasPayment.setPayer(sofia);
        sofiasPayment.setAmount(BigDecimal.valueOf(30));

        ernstsMeal = new Allotment();
        ernstsMeal.setContact(ernst);
        ernstsMeal.setType(AllotmentType.COEFFICIENT);
        ernstsMeal.setCoefficient(new BigDecimal("1.0"));

        jansDrink = new Allotment();
        jansDrink.setContact(jan);
        jansDrink.setType(AllotmentType.FIXED);
        jansDrink.setExpense(new BigDecimal("3.00"));
    }

    @Test
    void calculateTransactionsSignleAllotment() {
        Operation operation = new Operation();
        operation.setPayments(Arrays.asList(henksPayment, jansPayment));
        operation.setAllotments(Collections.singletonList(ernstsMeal));

        List<Transaction> transactions = operation.transactions();

        SystemCurrency currencyTodo = new SystemCurrency("EUR");
        
        for (Transaction transaction : transactions) {
            System.out.println(transaction.toString());
        }
    }

    @Test
    void calculateTransactionsFixedPart() {
        Operation operation = new Operation();
        operation.setPayments(Arrays.asList(henksPayment, jansPayment));
        operation.setAllotments(Arrays.asList(ernstsMeal, jansDrink));

        List<Transaction> transactions = operation.transactions();

        SystemCurrency currencyTodo = new SystemCurrency("EUR");

        for (Transaction transaction : transactions) {
            System.out.println(transaction.toString());
        }

        // TODO: ernst
        assertEquals(12, transactions.stream().filter(x -> ernst.equals(x.getFrom()))
                .map(transaction -> transaction.getAmount().doubleValue())
                .reduce((double) 0, Double::sum));

        assertEquals(3, transactions.stream().filter(x -> jan.equals(x.getFrom()))
                .map(transaction -> transaction.getAmount().doubleValue())
                .reduce((double) 0, Double::sum));

        assertEquals(10, transactions.stream().filter(x -> jan.equals(x.getTo()))
                .map(transaction -> transaction.getAmount().doubleValue())
                .reduce((double) 0, Double::sum));

        assertEquals(5, transactions.stream().filter(x -> henk.equals(x.getTo()))
                .map(transaction -> transaction.getAmount().doubleValue())
                .reduce((double) 0, Double::sum));
    }

    @Test
    void calculateTransactionsFixedPartAdvanced() {
        Operation operation = new Operation();
        operation.setPayments(Arrays.asList(henksPayment, jansPayment, sofiasPayment));
        operation.setAllotments(Arrays.asList(ernstsMeal, jansDrink));

        List<Transaction> transactions = operation.transactions();

        BigDecimal expectedPaid = new BigDecimal(0);
        BigDecimal actuallyPaid = new BigDecimal(0);

        for (Payment payment : operation.getPayments()) {
            expectedPaid = expectedPaid.add(payment.getAmount());
        }

        for (Transaction transaction : transactions) {
            System.out.println(transaction.toString());
            actuallyPaid = actuallyPaid.add(transaction.getAmount());
        }
        assertEquals(expectedPaid.setScale(2, RoundingMode.CEILING), actuallyPaid.setScale(2, RoundingMode.CEILING));

        // Jan should only spend '3' at most, because of his fixed participation
        assertEquals(3, transactions.stream().filter(x -> jan.equals(x.getFrom()))
                .map(transaction -> transaction.getAmount().doubleValue())
                .reduce((double) 0, Double::sum));
//
        // Sofia should expect her 30 back
        assertEquals(30, transactions.stream().filter(x -> sofia.equals(x.getTo()))
                .map(transaction -> transaction.getAmount().doubleValue())
                .reduce((double) 0, Double::sum));

        assertTrue(almost(0.33, transactions.get(0).getAmount()));
        assertTrue(almost(0.66, transactions.get(1).getAmount()));
        assertTrue(almost(2, transactions.get(2).getAmount()));

        assertTrue(almost(4.66, transactions.get(3).getAmount()));
        assertTrue(almost(9.33, transactions.get(4).getAmount()));
        assertTrue(almost(28.0, transactions.get(5).getAmount()));
    }

    private static boolean almost(double a, BigDecimal b) {
        return b.subtract(BigDecimal.valueOf(a)).doubleValue() < 0.01;
    }
}