package wf.spring.justmessenger.service.chat.single_chat;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import wf.spring.justmessenger.dto.chat.single_chat.SingleChatGetNewRqDTO;
import wf.spring.justmessenger.dto.chat.single_chat.SingleChatGetRqDTO;
import wf.spring.justmessenger.dto.chat.single_chat.SingleChatRsDTO;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.entity.chat.SingleChat;
import wf.spring.justmessenger.mapper.SingleChatMapper;
import wf.spring.justmessenger.model.exception.NotFoundException;
import wf.spring.justmessenger.repository.chat.SingleChatRepository;
import wf.spring.justmessenger.service.chat.ChatService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SingleChatServiceImpl implements SingleChatService {


    private final ChatService chatService;
    private final MongoTemplate mongoTemplate;
    private final SingleChatMapper singleChatMapper;
    private final SingleChatRepository singleChatRepository;
    private final SingleChatAccessService singleChatAccessService;



    @Override
    public Optional<SingleChat> findByFirstPersonIdAndSecondPersonId(ObjectId firstPersonId, ObjectId secondPersonId) {
        DoubleObjectId doubleId = new DoubleObjectId(firstPersonId, secondPersonId);
        return singleChatRepository.findByFirstPersonIdAndSecondPersonId(doubleId.getFirstId(), doubleId.getSecondId());
    }

    @Override
    public SingleChat getByFirstPersonIdAndSecondPersonId(ObjectId firstPersonId, ObjectId secondPersonId) {
        return findByFirstPersonIdAndSecondPersonId(firstPersonId, secondPersonId)
                .orElseThrow(() -> new NotFoundException("SingleChat with this person ids was not found"));
    }

    @Override
    public SingleChat getById(ObjectId chatId) {
        return singleChatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException("SingleChat with this chat id was not found"));
    }

    @Override
    public SingleChat getSingleChatOrCreateIfNotExists(ObjectId firstPersonId, ObjectId secondPersonId) {
        Optional<SingleChat> optionalSingleChat = findByFirstPersonIdAndSecondPersonId(firstPersonId, secondPersonId);
        if(optionalSingleChat.isPresent())
            return optionalSingleChat.get();

        DoubleObjectId doubleId = new DoubleObjectId(firstPersonId, secondPersonId);

        SingleChat singleChat = new SingleChat();

        singleChat.setChatId(chatService.createChat().getId());
        singleChat.setFirstPersonId(doubleId.getFirstId());
        singleChat.setSecondPersonId(doubleId.getSecondId());

        return singleChatRepository.save(singleChat);
    }



    @Override
    public List<SingleChatRsDTO> getNewSingleChats(SingleChatGetNewRqDTO singleChatGetNewRqDTO, Person principal) {
        Query query = new Query(Criteria
                .where("lastMessageId").gt(singleChatGetNewRqDTO.getOffSetMessageId())
                .andOperator(
                        new Criteria().orOperator(
                                Criteria.where("firstPersonId").is(principal.getId()),
                                Criteria.where("secondPersonId").is(principal.getId())
                        )))
                .with(Sort.by(Sort.Direction.ASC, "lastMessageId"));

        return singleChatMapper.toSingleChatRsDTOList(mongoTemplate.find(query, SingleChat.class));
    }

    @Override
    public boolean existsByChatIdAndFirstPersonIdOrSecondPersonId(ObjectId chatId, ObjectId personId) {
        Query query = new Query(Criteria
                .where("chatId").is(chatId)
                .andOperator(
                        new Criteria().orOperator(
                                Criteria.where("firstPersonId").is(personId),
                                Criteria.where("secondPersonId").is(personId)
                        )
                ));

        return mongoTemplate.exists(query, SingleChat.class);
    }


    @Override
    public List<SingleChatRsDTO> getAllSingleChats(Person principal) {
        return singleChatMapper.toSingleChatRsDTOList(singleChatRepository.findByFirstPersonIdOrSecondPersonId(principal.getId(), principal.getId()));
    }

    @Override
    public SingleChatRsDTO getById(SingleChatGetRqDTO singleChatGetRqDTO, Person principal) {
        singleChatAccessService.getSingleChatAccess(principal, singleChatGetRqDTO.getChatId());

        return singleChatMapper.toSingleChatRsDTO(getById(singleChatGetRqDTO.getChatId()));
    }


    @Getter
    @Setter
    @NoArgsConstructor
    private static class DoubleObjectId {
        private ObjectId firstId;
        private ObjectId secondId;

        public DoubleObjectId(ObjectId firstId, ObjectId secondId) {
            if (firstId.compareTo(secondId) < 0) {
                this.firstId = secondId;
                this.secondId = firstId;
            } else {
                this.firstId = firstId;
                this.secondId = secondId;
            }
        }
    }

}
