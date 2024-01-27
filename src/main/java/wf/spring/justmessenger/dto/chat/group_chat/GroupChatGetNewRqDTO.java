package wf.spring.justmessenger.dto.chat.group_chat;

import lombok.*;
import org.bson.types.ObjectId;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GroupChatGetNewRqDTO {

    private ObjectId offsetMessageId;

}
