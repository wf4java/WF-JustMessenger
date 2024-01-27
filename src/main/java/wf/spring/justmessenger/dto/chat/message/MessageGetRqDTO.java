package wf.spring.justmessenger.dto.chat.message;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageGetRqDTO {

    @NotNull
    private ObjectId chatId;

    @NotNull
    private ObjectId messageId;

}
