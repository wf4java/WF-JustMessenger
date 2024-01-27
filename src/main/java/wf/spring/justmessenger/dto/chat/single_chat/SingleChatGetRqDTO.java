package wf.spring.justmessenger.dto.chat.single_chat;

import lombok.*;
import org.bson.types.ObjectId;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SingleChatGetRqDTO {

    private ObjectId chatId;

}
