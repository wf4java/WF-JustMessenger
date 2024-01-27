package wf.spring.justmessenger.dto.chat.single_chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import wf.spring.justmessenger.dto.chat.message.MessageRsDTO;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SingleChatRsDTO {


    private ObjectId chatId;


    private ObjectId firstPersonId;


    private ObjectId secondPersonId;


    private MessageRsDTO lastMessage;

}
