package wf.spring.justmessenger.service.chat.group_chat;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import wf.spring.justmessenger.controller.chat.group_chat.GroupChatDeleteRqDTO;
import wf.spring.justmessenger.dto.chat.group_chat.*;
import wf.spring.justmessenger.entity.chat.GroupChat;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.mapper.GroupChatMapper;
import wf.spring.justmessenger.model.exception.NotFoundException;
import wf.spring.justmessenger.repository.chat.GroupChatRepository;
import wf.spring.justmessenger.service.chat.ChatService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupChatServiceImpl implements GroupChatService {

    private final ChatService chatService;
    private final MongoTemplate mongoTemplate;
    private final GroupChatMapper groupChatMapper;
    private final GroupChatRepository groupChatRepository;
    private final GroupChatAccessService groupChatAccessService;
    private final GroupChatParticipantService groupChatParticipantService;


    @Override
    public GroupChat create(GroupChatCreateRqDTO groupChatCreateRqDTO, Person principal) {
        groupChatAccessService.createAccess(principal);

        GroupChat groupChat = groupChatMapper.toGroupChat(groupChatCreateRqDTO);

        groupChat.setChatId(chatService.createChat().getId());
        groupChat.setOwnerId(principal.getId());
        groupChat.setParticipantCount(0);
        groupChat = groupChatRepository.save(groupChat);

        groupChatParticipantService.add(groupChat.getChatId(), principal.getId());
        if(groupChatCreateRqDTO.getMembers() != null)
            groupChatParticipantService.add(groupChat.getChatId(), groupChatCreateRqDTO.getMembers(), principal);


        return getById(groupChat.getChatId());
    }


    @Override
    public Optional<GroupChat> findById(ObjectId chatId) {
        return groupChatRepository.findById(chatId);
    }

    @Override
    public GroupChat getById(ObjectId id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("GroupChat with this chat id was not found"));
    }

    @Override
    public boolean exitsById(ObjectId id) {
        return groupChatRepository.existsById(id);
    }

    @Override
    public void exitsByIdOrThrow(ObjectId id) {
        if(!groupChatRepository.existsById(id))
            throw new NotFoundException("GroupChat with this chat id was not found");
    }

    @Override
    public void incrementParticipantCount(ObjectId id, int count) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().inc("participantCount", count);
        mongoTemplate.updateFirst(query, update, GroupChat.class);
    }



    @Override
    public void leave(GroupChatLeaveRqDTO groupChatLeaveRqDTO, Person principal) {
        groupChatAccessService.leaveAccess(principal, groupChatLeaveRqDTO.getChatId());

        groupChatParticipantService.remove(groupChatLeaveRqDTO.getChatId(), principal.getId());
    }


    @Override
    public List<GroupChatRsDTO> getNewGroupChats(GroupChatGetNewRqDTO groupChatGetNewRqDTO, Person principal) {
        return groupChatMapper.toGroupChatRsDTOList(
                groupChatRepository.findAllByChatIdIsIn(
                        groupChatParticipantService.getAllPersonGroupsIds(principal.getId()))
                        .stream()
                        .filter((gc) -> (gc.getChatId() == null || gc.getChatId().compareTo(groupChatGetNewRqDTO.getOffsetMessageId()) > 0))
                        .toList()
        );
    }

    @Override
    public List<GroupChatRsDTO> getAllGroupChats(Person principal) {
        return groupChatMapper.toGroupChatRsDTOList(groupChatRepository.findAllByChatIdIsIn
                (groupChatParticipantService.getAllPersonGroupsIds(principal.getId())));
    }


    @Override
    public GroupChatRsDTO getById(GroupChatGetRqDTO groupChatGetRqDTO, Person principal) {
        groupChatAccessService.getGroupChatAccess(principal, groupChatGetRqDTO.getChatId());

        return groupChatMapper.toGroupChatRsDTO(getById(groupChatGetRqDTO.getChatId()));
    }

    @Override
    public void delete(GroupChatDeleteRqDTO groupChatDeleteRqDTO, Person principal) {
        groupChatAccessService.getGroupChatDeleteAccess(principal, groupChatDeleteRqDTO.getChatId());

        groupChatRepository.deleteById(groupChatDeleteRqDTO.getChatId());
        groupChatParticipantService.deleteAllFromGroupChat(groupChatDeleteRqDTO.getChatId());
    }
}
