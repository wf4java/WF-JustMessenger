package wf.spring.justmessenger.repository.chat;

import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import wf.spring.justmessenger.entity.chat.Message;

@Repository
public interface MessageRepository extends MongoRepository<Message, ObjectId> {

    public boolean existsByIdAndChatId(ObjectId id, ObjectId chatId);

}
