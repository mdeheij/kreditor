package nl.kreditor.repository;

import nl.kreditor.model.Book;
import nl.kreditor.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
    List<Book> findByOwnerOrderByCreatedDesc(User user);
}