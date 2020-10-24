package nl.kreditor.response;

import lombok.Data;
import nl.kreditor.component.solver.Member;

@Data
public class ContactResponse {
    private Integer id;
    private String name;

    public ContactResponse(Member member) {
        this.id = member.getId();
        this.name = member.getName();
    }
}
