package wf.spring.justmessenger.dto.chat.message;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.bson.types.ObjectId;
import wf.spring.justmessenger.utils.validators.annotation.CollectionElementNotNull;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageSendRqDTO {

    @NotNull
    private ObjectId receiverId;

    @Size(max = 4096, message = "Text size must be less than or equal to 4096")
    private String text;

    @Size(max = 5, message = "Max attachment count is 5")
    @CollectionElementNotNull
    private Set<ObjectId> attachments;

    private ObjectId replyMessageId;


}
