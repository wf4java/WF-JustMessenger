package wf.spring.justmessenger.service.chat.single_chat;

import org.bson.types.ObjectId;
import wf.spring.justmessenger.dto.chat.single_chat.SingleChatGetNewRqDTO;
import wf.spring.justmessenger.dto.chat.single_chat.SingleChatGetRqDTO;
import wf.spring.justmessenger.dto.chat.single_chat.SingleChatRsDTO;
import wf.spring.justmessenger.entity.chat.SingleChat;
import wf.spring.justmessenger.entity.person.Person;

import java.util.List;
import java.util.Optional;

public interface SingleChatService {
    Optional<SingleChat> findByFirstPersonIdAndSecondPersonId(ObjectId firstPersonId, ObjectId secondPersonId);

    SingleChat getByFirstPersonIdAndSecondPersonId(ObjectId firstPersonId, ObjectId secondPersonId);

    SingleChat getById(ObjectId chatId);

    SingleChat getSingleChatOrCreateIfNotExists(ObjectId firstPersonId, ObjectId secondPersonId);

    List<SingleChatRsDTO> getNewSingleChats(SingleChatGetNewRqDTO singleChatGetNewRqDTO, Person principal);

    boolean existsByChatIdAndFirstPersonIdOrSecondPersonId(ObjectId chatId, ObjectId personId);

    List<SingleChatRsDTO> getAllSingleChats(Person principal);

    SingleChatRsDTO getById(SingleChatGetRqDTO singleChatGetRqDTO, Person principal);
}
