package nl.kreditor.service;

import nl.kreditor.model.Book;
import nl.kreditor.model.Category;
import nl.kreditor.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public List<Category> findByBook(Book book) {
        return repository.findAllByBook(book);
    }

    public Category findById(int id) {
        return repository.findById(id);
    }
}