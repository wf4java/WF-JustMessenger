package wf.spring.justmessenger.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorMessageRsDTO {


    private String message;

    private long timestamp;

    public ErrorMessageRsDTO(String message) {
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public ErrorMessageRsDTO(Exception exception) {
        this.message = exception.getMessage();
        this.timestamp = System.currentTimeMillis();
    }

    public ErrorMessageRsDTO(Exception exception, long timestamp) {
        this.message = exception.getMessage();
        this.timestamp = timestamp;
    }
}
