package nl.kreditor.controller.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import nl.kreditor.form.PaymentForm;
import nl.kreditor.model.Book;
import nl.kreditor.model.Operation;
import nl.kreditor.model.Payment;
import nl.kreditor.repository.AllotmentRepository;
import nl.kreditor.repository.CategoryRepository;
import nl.kreditor.repository.ContactRepository;
import nl.kreditor.repository.PaymentRepository;
import nl.kreditor.response.PaymentResponse;
import nl.kreditor.service.BookService;
import nl.kreditor.service.OperationService;
import nl.kreditor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/books/{bookId}/operations/{operationId}/payments")
public class PaymentController {
    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private OperationService operationService;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AllotmentRepository allotmentRepository;

    @GetMapping("/")
    @io.swagger.v3.oas.annotations.Operation(tags = {"app"})
    List<PaymentResponse> getPayments(
            @PathVariable("bookId") @Parameter(schema = @Schema(type = "integer")) Book book,
            @PathVariable("operationId") @Parameter(schema = @Schema(type = "integer")) Operation operation
    ) {
        return operation.getPayments()
                .stream()
                .map(PaymentResponse::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/")
    @io.swagger.v3.oas.annotations.Operation(tags = {"app"})
    PaymentResponse postPayment(
            @PathVariable("bookId") @Parameter(schema = @Schema(type = "integer")) Book book,
            @PathVariable("operationId") @Parameter(schema = @Schema(type = "integer")) Operation operation,
            @RequestBody @Parameter(name = "payment") PaymentForm paymentForm
    ) {
        Payment payment = new Payment(
                paymentForm.getAmount(),
                contactRepository.findById(paymentForm.getContactId()),
                operation
        );

        return new PaymentResponse(paymentRepository.save(payment));
    }


    @DeleteMapping("/{paymentId}")
    @io.swagger.v3.oas.annotations.Operation(tags = "app")
    ResponseEntity<Void> deletePayment(
            @PathVariable("bookId") @Parameter(schema = @Schema(type = "integer")) Book book,
            @PathVariable("operationId") @Parameter(schema = @Schema(type = "integer")) Operation operation,
            @PathVariable("paymentId") @Parameter(schema = @Schema(type = "integer")) Payment payment
    ) {
        paymentRepository.delete(payment);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
