package wf.spring.justmessenger.service.chat;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import wf.spring.justmessenger.entity.chat.Message;
import wf.spring.justmessenger.model.exception.NotFoundException;
import wf.spring.justmessenger.repository.chat.MessageRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MongoTemplate mongoTemplate;


    @Override
    public Optional<Message> findById(ObjectId id) {
        return messageRepository.findById(id);
    }

    @Override
    public Message getById(ObjectId id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("Message with this id was not found"));
    }

    @Override
    public List<Message> getNewMessagesByChatIdAndOffsetMessageId(ObjectId chatId, ObjectId offsetMessageId, int limit) {
        Query query = new Query(Criteria
                .where("chatId").is(chatId)
                .and("_id").gt(offsetMessageId))
                .with(Sort.by(Sort.Direction.ASC, "_id"))
                .limit(limit);

        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public List<Message> getOldMessagesByChatIdAndOffsetMessageId(ObjectId chatId, ObjectId offsetMessageId, int limit) {
        Query query = new Query(Criteria
                .where("chatId").is(chatId)
                .and("_id").lt(offsetMessageId))
                .with(Sort.by(Sort.Direction.DESC, "_id"))
                .limit(limit);

        return mongoTemplate.find(query, Message.class);
    }


    @Override
    public boolean existsByIdAndChatId(ObjectId id, ObjectId chatId) {
        return messageRepository.existsByIdAndChatId(id, chatId);
    }

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

}
