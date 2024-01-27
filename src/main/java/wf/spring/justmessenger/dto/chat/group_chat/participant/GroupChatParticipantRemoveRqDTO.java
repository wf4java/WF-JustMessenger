package wf.spring.justmessenger.dto.chat.group_chat.participant;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GroupChatParticipantRemoveRqDTO {

    @NotNull
    private ObjectId chatId;

    @NotNull
    private ObjectId personId;

}
