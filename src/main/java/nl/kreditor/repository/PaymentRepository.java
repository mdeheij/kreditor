package nl.kreditor.repository;

import nl.kreditor.model.Contact;
import nl.kreditor.model.Operation;
import nl.kreditor.model.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Integer> {
    List<Contact> findAllByOperation(Operation operation);
    Payment findById(int id);
}