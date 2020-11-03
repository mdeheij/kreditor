package nl.kreditor.api.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import javassist.NotFoundException;
import nl.kreditor.component.solver.TransferSimplifier;
import nl.kreditor.form.OperationForm;
import nl.kreditor.model.Book;
import nl.kreditor.model.Operation;
import nl.kreditor.model.PaymentType;
import nl.kreditor.repository.CategoryRepository;
import nl.kreditor.response.OperationResponse;
import nl.kreditor.response.TransactionResponse;
import nl.kreditor.service.BookService;
import nl.kreditor.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/books/{bookId}/operations")
public class OperationController {
    @Autowired
    private BookService bookService;

    @Autowired
    private OperationService operationService;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/")
    @io.swagger.v3.oas.annotations.Operation(tags = {"app"})
    List<OperationResponse> getOperations(@PathVariable Integer bookId) throws NotFoundException {
        return operationService
                .findByBook(bookService.findById(bookId))
                .stream()
                .map(OperationResponse::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/")
    @io.swagger.v3.oas.annotations.Operation(tags = {"app"})
    OperationResponse postOperation(
            @PathVariable("bookId") @Parameter(schema = @Schema(type = "integer")) Book book,
            @Valid @RequestBody OperationForm operationRequest
    ) {
        Operation operation = new Operation(operationRequest, book);
        return new OperationResponse(operationService.persist(operation));
    }

    @PutMapping("/{operationId}")
    @io.swagger.v3.oas.annotations.Operation(tags = {"app"})
    OperationResponse putOperation(
            @PathVariable("bookId") @Parameter(schema = @Schema(type = "integer")) Book book,
            @PathVariable("operationId") @Parameter(schema = @Schema(type = "integer")) Operation operation,
            @Valid @RequestBody OperationForm operationForm
    ) {
        operation.setBook(book);
        operation.setModified(LocalDateTime.now());
        operation.setName(operationForm.getName());
        operation.setCategory(categoryRepository.findById(operationForm.getCategoryId()));
        operation.setType(operationForm.getType());
        operation.setPaymentType(PaymentType.UNKNOWN);

        return new OperationResponse(operationService.persist(operation));
    }

    @GetMapping("/{operationId}")
    @io.swagger.v3.oas.annotations.Operation(tags = {"app"})
    OperationResponse getOperation(
            @PathVariable("bookId") @Parameter(schema = @Schema(type = "integer")) Book book,
            @PathVariable("operationId") @Parameter(schema = @Schema(type = "integer")) Operation operation
    ) {
        if (!operation.getBook().equals(book)) {
            throw new RuntimeException("Not implemented"); //TODO
        }

        return new OperationResponse(operation);
    }

    @GetMapping("/{operationId}/transactions")
    @io.swagger.v3.oas.annotations.Operation(tags = {"app"})
    List<TransactionResponse> transactions(
            @PathVariable("bookId") @Parameter(schema = @Schema(type = "integer")) Book book,
            @PathVariable("operationId") @Parameter(schema = @Schema(type = "integer")) Operation operation
    ) {
        return operation.transactions().stream().map(TransactionResponse::new).collect(Collectors.toList());
    }

    @GetMapping("/{operationId}/transactions/simplify")
    @io.swagger.v3.oas.annotations.Operation(tags = {"app"})
    List<TransactionResponse> simplify(
            @PathVariable("bookId") @Parameter(schema = @Schema(type = "integer")) Book book,
            @PathVariable("operationId") @Parameter(schema = @Schema(type = "integer")) Operation operation
    ) {
        TransferSimplifier transferSimplifier = new TransferSimplifier(operation.transactions());
        return transferSimplifier.simplify().stream().map(TransactionResponse::new).collect(Collectors.toList());
    }
}
