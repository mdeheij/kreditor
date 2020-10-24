package nl.kreditor.response;

import lombok.Data;

@Data
public class FormHandledResponse {
    private Boolean successful;
    private String message;

    public FormHandledResponse() {
        successful = true;
    }

    public FormHandledResponse(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }
}
