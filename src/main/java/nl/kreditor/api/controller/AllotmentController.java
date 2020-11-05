package nl.kreditor.api.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import nl.kreditor.form.AllotmentForm;
import nl.kreditor.model.Allotment;
import nl.kreditor.model.Book;
import nl.kreditor.model.Contact;
import nl.kreditor.model.Operation;
import nl.kreditor.repository.AllotmentRepository;
import nl.kreditor.repository.ContactRepository;
import nl.kreditor.response.AllotmentResponse;
import nl.kreditor.security.Authenticatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/books/{bookId}/operations/{operationId}/allotments")
public class AllotmentController {
    private final ContactRepository contactRepository;

    private final AllotmentRepository allotmentRepository;

    private final Authenticatable auth;

    @Autowired
    public AllotmentController(
            ContactRepository contactRepository,
            AllotmentRepository allotmentRepository,
            Authenticatable auth
    ) {
        this.contactRepository = contactRepository;
        this.allotmentRepository = allotmentRepository;
        this.auth = auth;
    }

    @GetMapping("/")
    @io.swagger.v3.oas.annotations.Operation(tags = {"app"})
    List<AllotmentResponse> getAllotments(
            @PathVariable("bookId") @Parameter(schema = @Schema(type = "integer")) Book book,
            @PathVariable("operationId") @Parameter(schema = @Schema(type = "integer")) Operation operation
    ) {
        //TODO: replace these with some kind of (generic) voter
        if (book.getOwner() != auth.getUser()) {
            throw new AccessDeniedException("Book is not owned by current user");
        }

        return operation.getAllotments()
                .stream()
                .map(AllotmentResponse::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/")
    @io.swagger.v3.oas.annotations.Operation(tags = {"app"})
    AllotmentResponse postAllotment(
            @PathVariable("bookId") @Parameter(schema = @Schema(type = "integer")) Book book,
            @PathVariable("operationId") @Parameter(schema = @Schema(type = "integer")) Operation operation,
            @RequestBody @Parameter(name = "allotment") AllotmentForm allotmentForm
    ) {
        if (book.getOwner() != auth.getUser()) {
            throw new AccessDeniedException("Book is not owned by current user");
        }

        Contact contact = contactRepository.findById(allotmentForm.getContactId());
        if (contact == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown contact");
        }

        Allotment allotment = new Allotment(
                allotmentForm.getType(),
                allotmentForm.getCoefficient(),
                allotmentForm.getExpense(),
                contact,
                operation
        );

        return new AllotmentResponse(allotmentRepository.save(allotment));
    }

    @DeleteMapping("/{allotmentId}")
    @io.swagger.v3.oas.annotations.Operation(tags = {"app"})
    ResponseEntity<Void> deleteAllotment(
            @PathVariable("bookId") @Parameter(schema = @Schema(type = "integer")) Book book,
            @PathVariable("operationId") @Parameter(schema = @Schema(type = "integer")) Operation operation,
            @PathVariable("allotmentId") @Parameter(schema = @Schema(type = "integer")) Allotment allotment
    ) {
        if (book.getOwner() != auth.getUser()) {
            throw new AccessDeniedException("Book is not owned by current user");
        }

        allotmentRepository.delete(allotment);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
