package nl.kreditor.repository;

import nl.kreditor.model.Book;
import nl.kreditor.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {
    List<Category> findAllByBook(Book book);
    Category findById(int id);
}