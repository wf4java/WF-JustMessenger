package wf.spring.justmessenger.dto.chat.group_chat.edit;


import lombok.*;
import org.bson.types.ObjectId;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GroupChatNameChangeRsDTO {

    private ObjectId chatId;

    private String name;

}
