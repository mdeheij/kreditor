package nl.kreditor.repository;

import nl.kreditor.model.Book;
import nl.kreditor.model.Operation;
import nl.kreditor.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends CrudRepository<Operation, Integer> {
    List<Operation> findByBookOrderByCreatedDesc(Book book);
}