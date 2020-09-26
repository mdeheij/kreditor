package nl.kreditor.service;

import javassist.NotFoundException;
import nl.kreditor.model.Book;
import nl.kreditor.model.User;
import nl.kreditor.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository repository;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public List<Book> findAll() {
        return (List<Book>) repository.findAll();
    }

    public List<Book> findAllOwned(User user) {
        return repository.findByOwnerOrderByCreatedDesc(user);
    }

    public Book findById(int id) throws NotFoundException {
        Optional<Book> result = repository.findById(id);

        if (result.isPresent()) {
            return result.get();
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find book");
    }

    public Long count() {
        return repository.count();
    }

    @Transactional
    public Book create(Book book) {
        entityManager.persist(book);
        entityManager.flush();
        return book;
    }
}