package wf.spring.justmessenger.service.chat.single_chat;

import org.bson.types.ObjectId;
import wf.spring.justmessenger.dto.chat.message.*;
import wf.spring.justmessenger.entity.chat.Message;
import wf.spring.justmessenger.entity.person.Person;

import java.util.List;

public interface SingleChatMessageService {
    void sendMessage(MessageSendRqDTO messageSendRqDTO, Person principal);

    void updateLastMessage(ObjectId id, Message message);

    Message getMessage(MessageGetRqDTO messageGetRqDTO, Person principal);

    List<Message> getNewMessages(MessageGetNewRqDTO messageGetNewRqDTO, Person principal);

    List<Message> getOldMessages(MessageGetOldRqDTO messageGetOldRqDTO, Person principal);
}
