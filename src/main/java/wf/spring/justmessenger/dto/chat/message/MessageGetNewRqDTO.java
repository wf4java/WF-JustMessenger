package wf.spring.justmessenger.dto.chat.message;

import jakarta.validation.constraints.*;
import lombok.*;
import org.bson.types.ObjectId;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageGetNewRqDTO {

    @NotNull
    private ObjectId chatId;

    @NotNull
    private ObjectId offsetMessageId;

    @Min(1)
    @Max(100)
    private int limit = 25;


}
