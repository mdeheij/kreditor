package nl.kreditor.response;

import lombok.Data;
import nl.kreditor.model.Book;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Data
public class BookResponse {
    private Integer id;
    private String name;
    private LocalDateTime created;

    public BookResponse() {}

    public BookResponse(Book book) {
        if (book == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown book");
        }

        this.id = book.getId();
        this.name = book.getName();
        this.created = book.getCreated();
    }
}
