package nl.kreditor.component.solver;

import nl.kreditor.component.transaction.Transaction;
import nl.kreditor.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("FieldCanBeLocal")
class TransferSimplifierTest {
    private final Contact henk = contact("Henk");
    private final Contact jan = contact("Jan");
    private final Contact ernst = contact("Ernst");
    private final Contact sofia = contact("Sofia");

    private Payment henksPayment;
    private Payment jansPayment;
    private Payment sofiasPayment;

    private Allotment ernstsMeal;
    private Allotment jansDrink;

    private int idCounter;

    Contact contact(String name) {
        Contact contact = new Contact();
        contact.setId(idCounter++);
        contact.setName(name);

        return contact;
    }


    @BeforeEach
    void setUp() {
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
    void simplify() {
        SystemCurrency c = new SystemCurrency("EUR");
        Transaction t1 = new Transaction(jan, henk, c, new BigDecimal("2.00"));
        Transaction t2 = new Transaction(henk, jan, c, new BigDecimal("3.00"));
        Transaction t3 = new Transaction(henk, sofia, c, new BigDecimal("1.10"));

        TransferSimplifier transferSimplifier = new TransferSimplifier(Arrays.asList(t1, t2, t3));

        List<Transaction> x = transferSimplifier.simplify();
        assertTrue(x.size() > 0);
    }

    @Test
    void simplifyCents() {
        SystemCurrency c = new SystemCurrency("EUR");
        Transaction t1 = new Transaction(jan, henk, c, new BigDecimal("1"));
        Transaction t2 = new Transaction(henk, sofia, c, new BigDecimal("1"));
        Transaction t3 = new Transaction(sofia, jan, c, new BigDecimal("1"));

        TransferSimplifier transferSimplifier = new TransferSimplifier(Arrays.asList(t1, t2, t3));

        List<Transaction> x = transferSimplifier.simplify();
        assertTrue(x.size() > 0);
    }

    @Test
    void createGraphForDebts() {
    }

    @Test
    void isValidEqual() {
        SystemCurrency c = new SystemCurrency("EUR");
        Transaction t1 = new Transaction(jan, henk, c, new BigDecimal("1"));
        Transaction t2 = new Transaction(henk, sofia, c, new BigDecimal("1"));
        Transaction t3 = new Transaction(sofia, jan, c, new BigDecimal("1"));

        assertFalse(TransferSimplifier.isValid(Arrays.asList(t1, t2, t3)));
    }

    @Test
    void isValidNonEqual() {
        SystemCurrency c = new SystemCurrency("EUR");
        Transaction t1 = new Transaction(jan, henk, c, new BigDecimal("2"));
        Transaction t2 = new Transaction(henk, sofia, c, new BigDecimal("1"));
        Transaction t3 = new Transaction(sofia, jan, c, new BigDecimal("1"));

        assertTrue(TransferSimplifier.isValid(Arrays.asList(t1, t2, t3)));
    }

    @Test
    void isValidNonEqualCents() {
        SystemCurrency c = new SystemCurrency("EUR");
        Transaction t1 = new Transaction(jan, henk, c, new BigDecimal("1.0100001"));
        Transaction t2 = new Transaction(henk, sofia, c, new BigDecimal("1"));
        Transaction t3 = new Transaction(sofia, jan, c, new BigDecimal("1"));

        assertTrue(TransferSimplifier.isValid(Arrays.asList(t1, t2, t3)));
    }
}