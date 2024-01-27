package wf.spring.justmessenger.dto.chat.group_chat;

import lombok.*;
import org.bson.types.ObjectId;
import wf.spring.justmessenger.dto.chat.message.MessageRsDTO;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GroupChatRsDTO {

    private ObjectId chatId;

    private String name;

    private Integer participantCount;

    private ObjectId ownerId;

    private MessageRsDTO lastMessage;

    private boolean hasProfilePhoto;

}
