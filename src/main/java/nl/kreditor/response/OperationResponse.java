package nl.kreditor.response;

import lombok.Data;
import nl.kreditor.model.Operation;
import nl.kreditor.model.OperationType;
import nl.kreditor.model.PaymentType;

import java.time.LocalDateTime;

@Data
public class OperationResponse {
    private Integer id;
    private String name;
    private LocalDateTime created;
    private LocalDateTime modified;
    private OperationType operationType;
    private PaymentType paymentType;

    public OperationResponse(Operation operation) {
        this.name = operation.getName();
        this.created = operation.getCreated();
        this.modified = operation.getModified();
        this.id = operation.getId();
        this.name = operation.getName();
        this.operationType = operation.getType();
        this.paymentType = operation.getPaymentType();
    }
}
