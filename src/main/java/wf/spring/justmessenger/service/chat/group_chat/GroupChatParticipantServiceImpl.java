package wf.spring.justmessenger.service.chat.group_chat;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import wf.spring.justmessenger.dto.chat.group_chat.GroupChatCreateRsDTO;
import wf.spring.justmessenger.dto.chat.group_chat.GroupChatRemoveRsDTO;
import wf.spring.justmessenger.dto.chat.group_chat.participant.*;
import wf.spring.justmessenger.entity.chat.GroupChatParticipant;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.mapper.GroupChatParticipantMapper;
import wf.spring.justmessenger.model.MessengerMessagingTemplate;
import wf.spring.justmessenger.model.exception.NotFoundException;
import wf.spring.justmessenger.model.exception.basic.BasicException;
import wf.spring.justmessenger.repository.chat.GroupChatParticipantRepository;
import wf.spring.justmessenger.service.security.SubscribeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupChatParticipantServiceImpl implements GroupChatParticipantService {


    private final MongoTemplate mongoTemplate;
    private final SubscribeService subscribeService;
    private final GroupChatService groupChatService;
    private final GroupChatAccessService groupChatAccessService;
    private final GroupChatParticipantMapper groupChatParticipantMapper;
    private final MessengerMessagingTemplate messengerMessagingTemplate;
    private final GroupChatParticipantRepository groupChatParticipantRepository;


    @Override
    public GroupChatParticipant getById(ObjectId id) {
        return groupChatParticipantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("GroupChatParticipant with this id was not found"));
    }

    @Override
    public GroupChatParticipant getByGroupIdAndPersonId(ObjectId groupId, ObjectId personId) {
        return groupChatParticipantRepository.findByGroupIdAndPersonId(groupId, personId)
                .orElseThrow(() -> new NotFoundException("GroupChatParticipant with this (groupId and personId) was not found"));
    }



    @Override
    public void add(GroupChatParticipantAddRqDTO groupChatParticipantAddRqDTO, Person principal) {
        add(groupChatParticipantAddRqDTO.getChatId(), groupChatParticipantAddRqDTO.getPersonId(), principal);
    }


    @Override
    @Async
    public void add(ObjectId groupId, List<ObjectId> personIds, Person principal) {
        for(ObjectId personId : personIds) {
            try {add(groupId, personId, principal);}
            catch (BasicException ignore) { }
        }
    }



    @Override
    public void add(ObjectId groupId, ObjectId personId, Person principal) {
        groupChatAccessService.addParticipantAccess(principal, groupId, personId);

        add(groupId, personId);
    }



    @Override
    public void add(ObjectId groupId, ObjectId personId) {
        GroupChatParticipant groupParticipant = new GroupChatParticipant();

        groupParticipant.setGroupId(groupId);
        groupParticipant.setPersonId(personId);

        groupParticipant = groupChatParticipantRepository.save(groupParticipant);
        groupChatService.incrementParticipantCount(groupId, 1);

        messengerMessagingTemplate.convertAndSendToGroupChatParticipantAdd(
                groupId.toHexString(), groupChatParticipantMapper.toGroupChatParticipantRsDTO(groupParticipant));

        messengerMessagingTemplate.convertAndSendToUserNewGroupChat(
                personId.toHexString(), new GroupChatCreateRsDTO(groupId));
    }


    @Override
    public void remove(GroupChatParticipantRemoveRqDTO groupChatParticipantRemoveRqDTO, Person principal) {
        groupChatAccessService.removeParticipantAccess(principal, groupChatParticipantRemoveRqDTO.getChatId(), groupChatParticipantRemoveRqDTO.getPersonId());

        remove(groupChatParticipantRemoveRqDTO.getChatId(), groupChatParticipantRemoveRqDTO.getPersonId());
    }

    @Override
    public void remove(ObjectId groupId, ObjectId personId) {
        if(groupChatParticipantRepository.removeByGroupIdAndPersonId(groupId, personId) == 0) return;

        groupChatService.incrementParticipantCount(groupId, -1);

        messengerMessagingTemplate.convertAndSendToGroupChatParticipantRemove(
                groupId.toHexString(), new GroupChatParticipantRemoveRsDTO(groupId, personId));

        messengerMessagingTemplate.convertAndSendToUserRemoveGroupChat(
                personId.toHexString(), new GroupChatRemoveRsDTO(groupId));

        subscribeService.unsubscribeFromGroup(personId,groupId);
    }

    @Override
    public void removeByGroupIdAndPersonId(ObjectId groupId, ObjectId personId) {
        groupChatParticipantRepository.removeByGroupIdAndPersonId(groupId, personId);
    }


    @Override
    public boolean existsByGroupIdAndPersonId(ObjectId groupId, ObjectId personId) {
        return groupChatParticipantRepository.existsByGroupIdAndPersonId(groupId, personId);
    }

    @Override
    public List<ObjectId> getAllPersonGroupsIds(ObjectId personId) {
        Query query = new Query(Criteria.where("personId").is(personId));
        query.fields().include("groupId");

        return mongoTemplate.find(query, GroupChatParticipant.class).stream().map(GroupChatParticipant::getGroupId).toList();
    }


    @Override
    public List<GroupChatParticipantRsDTO> getAll(GroupChatParticipantGetAllRqDTO groupChatParticipantGetAllRqDTO, Person principal) {
        groupChatAccessService.getAllParticipantAccess(principal, groupChatParticipantGetAllRqDTO.getChatId());

        return groupChatParticipantMapper.toGroupChatParticipantRsDTOList
                (groupChatParticipantRepository.findAllByGroupId(groupChatParticipantGetAllRqDTO.getChatId()));
    }

    @Override
    public GroupChatParticipantRsDTO get(GroupChatParticipantGetRqDTO groupChatParticipantGetRqDTO, Person principal) {
        groupChatAccessService.getParticipantAccess(principal, groupChatParticipantGetRqDTO.getChatId(), groupChatParticipantGetRqDTO.getPersonId());

        return groupChatParticipantMapper.toGroupChatParticipantRsDTO
                (getByGroupIdAndPersonId(groupChatParticipantGetRqDTO.getChatId(), groupChatParticipantGetRqDTO.getPersonId()));
    }

    @Override
    @Async
    public void deleteAllFromGroupChat(ObjectId chatId) {
        for(GroupChatParticipant groupChatParticipant : groupChatParticipantRepository.findAllByGroupId(chatId)) {
            messengerMessagingTemplate.convertAndSendToUserRemoveGroupChat(
                    groupChatParticipant.getPersonId().toHexString(), new GroupChatRemoveRsDTO(groupChatParticipant.getGroupId()));
        }
        groupChatParticipantRepository.deleteAllByGroupId(chatId);
    }
}
