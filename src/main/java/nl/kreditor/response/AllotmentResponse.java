package nl.kreditor.response;

import lombok.Data;
import nl.kreditor.model.Allotment;
import nl.kreditor.model.AllotmentType;

import java.math.BigDecimal;

@Data
public class AllotmentResponse {
    private Integer id;
    private int contactId;
    private AllotmentType type;
    private BigDecimal coefficient;
    private BigDecimal expense;
    private boolean valid;

    public AllotmentResponse(Allotment allotment) {
        this.id = allotment.getId();
        this.contactId = allotment.getContact().getId();
        this.type = allotment.getType();
        this.coefficient = allotment.getCoefficient();
        this.expense = allotment.getExpense();
        this.valid = allotment.isValid();
    }
}
