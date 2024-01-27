package wf.spring.justmessenger.dto.chat.group_chat.participant;

import lombok.*;
import org.bson.types.ObjectId;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GroupChatParticipantRsDTO {

    private ObjectId chatId;

    private ObjectId personId;

}
