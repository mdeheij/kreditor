package nl.kreditor.repository;

import nl.kreditor.model.Book;
import nl.kreditor.model.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Integer> {
    List<Contact> findAllByBook(Book book);
    Contact findById(int id);
}