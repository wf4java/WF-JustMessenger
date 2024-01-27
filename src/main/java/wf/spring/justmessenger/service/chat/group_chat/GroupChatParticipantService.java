package wf.spring.justmessenger.service.chat.group_chat;

import org.bson.types.ObjectId;
import org.springframework.scheduling.annotation.Async;
import wf.spring.justmessenger.dto.chat.group_chat.participant.*;
import wf.spring.justmessenger.entity.chat.GroupChatParticipant;
import wf.spring.justmessenger.entity.person.Person;

import java.util.List;

public interface GroupChatParticipantService {
    GroupChatParticipant getById(ObjectId id);

    GroupChatParticipant getByGroupIdAndPersonId(ObjectId groupId, ObjectId personId);

    void add(GroupChatParticipantAddRqDTO groupChatParticipantAddRqDTO, Person principal);

    @Async
    void add(ObjectId groupId, List<ObjectId> personIds, Person principal);

    void add(ObjectId groupId, ObjectId personId, Person principal);

    void add(ObjectId groupId, ObjectId personId);

    void remove(GroupChatParticipantRemoveRqDTO groupChatParticipantRemoveRqDTO, Person principal);

    void remove(ObjectId groupId, ObjectId personId);

    void removeByGroupIdAndPersonId(ObjectId groupId, ObjectId personId);

    boolean existsByGroupIdAndPersonId(ObjectId groupId, ObjectId personId);

    List<ObjectId> getAllPersonGroupsIds(ObjectId personId);

    List<GroupChatParticipantRsDTO> getAll(GroupChatParticipantGetAllRqDTO groupChatParticipantGetAllRqDTO, Person principal);

    GroupChatParticipantRsDTO get(GroupChatParticipantGetRqDTO groupChatParticipantGetRqDTO, Person principal);

    @Async
    void deleteAllFromGroupChat(ObjectId chatId);
}
