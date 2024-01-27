package wf.spring.justmessenger.service.chat.group_chat;

import org.bson.types.ObjectId;
import wf.spring.justmessenger.controller.chat.group_chat.GroupChatDeleteRqDTO;
import wf.spring.justmessenger.dto.chat.group_chat.*;
import wf.spring.justmessenger.entity.chat.GroupChat;
import wf.spring.justmessenger.entity.person.Person;

import java.util.List;
import java.util.Optional;

public interface GroupChatService {
    GroupChat create(GroupChatCreateRqDTO groupChatCreateRqDTO, Person principal);

    Optional<GroupChat> findById(ObjectId chatId);

    GroupChat getById(ObjectId id);

    boolean exitsById(ObjectId id);

    void exitsByIdOrThrow(ObjectId id);

    void incrementParticipantCount(ObjectId id, int count);

    void leave(GroupChatLeaveRqDTO groupChatLeaveRqDTO, Person principal);

    List<GroupChatRsDTO> getNewGroupChats(GroupChatGetNewRqDTO groupChatGetNewRqDTO, Person principal);

    List<GroupChatRsDTO> getAllGroupChats(Person principal);

    GroupChatRsDTO getById(GroupChatGetRqDTO groupChatGetRqDTO, Person principal);

    void delete(GroupChatDeleteRqDTO groupChatDeleteRqDTO, Person principal);
}
