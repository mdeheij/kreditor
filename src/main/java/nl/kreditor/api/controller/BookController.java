package nl.kreditor.api.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import nl.kreditor.component.solver.TransferSimplifier;
import nl.kreditor.component.transaction.Transaction;
import nl.kreditor.form.BookForm;
import nl.kreditor.model.Book;
import nl.kreditor.response.BookResponse;
import nl.kreditor.response.TransactionResponse;
import nl.kreditor.security.Authenticatable;
import nl.kreditor.service.BookService;
import nl.kreditor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    private final Authenticatable auth;

    @Autowired
    public BookController(BookService bookService, Authenticatable auth) {
        this.bookService = bookService;
        this.auth = auth;
    }

    @GetMapping("/")
    @io.swagger.v3.oas.annotations.Operation(tags = {"app"})
    List<BookResponse> getBooks() {
        return bookService.findAllOwned(auth.getUser()).stream().map(BookResponse::new).collect(Collectors.toList());
    }

    @PostMapping("/")
    @io.swagger.v3.oas.annotations.Operation(tags = {"app"})
    BookResponse postBook(@Valid @RequestBody BookForm bookForm) {
        Book book = new Book(bookForm, auth.getUser());
        return new BookResponse(bookService.create(book));
    }

    @GetMapping("/{bookId}")
    @io.swagger.v3.oas.annotations.Operation(tags = {"app"})
    BookResponse getBook(@PathVariable("bookId") @Parameter(schema = @Schema(type = "integer")) Book book) {
        return new BookResponse(book);
    }

    @GetMapping("/{bookId}/solve")
    @io.swagger.v3.oas.annotations.Operation(tags = {"app"})
    List<TransactionResponse> solve(
            @PathVariable("bookId") @Parameter(schema = @Schema(type = "integer")) Book book
    ) {
        List<Transaction> transactionList = new ArrayList<>();
        book.getOperations().forEach(operation -> transactionList.addAll(operation.transactions()));

        return new TransferSimplifier(transactionList)
                .simplify()
                .stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
    }
}
